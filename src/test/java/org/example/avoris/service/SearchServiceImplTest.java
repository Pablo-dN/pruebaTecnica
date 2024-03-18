package org.example.avoris.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.avoris.dto.SearchRequest;
import org.example.avoris.dto.SearchResultResponse;
import org.example.avoris.exception.SearchException;
import org.example.avoris.model.MongoDBSearchRequest;
import org.example.avoris.repository.SearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceImplTest {


  @MockBean
  private KafkaTemplate<String, SearchRequest> kafkaTemplateMock;

  @Autowired
  private SearchServiceImpl searchService;

  @Autowired
  private KafkaTemplate<String, SearchRequest> kafkaTemplateReal;

  @MockBean
  private SearchRepository repository;


  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  public void processSearchRequest_Success() {
    SearchRequest request = createSampleSearchRequest();
    when(kafkaTemplateMock.send(anyString(), anyString(), any(SearchRequest.class))).thenReturn(null);

    String searchId = searchService.processSearchRequest(request);

    verify(kafkaTemplateMock).send("hotel_availability_searches", searchId, request);
  }

  @Test(expected = SearchException.class)
  public void processSearchRequest_Error() {
    SearchRequest request = createSampleSearchRequest();
    when(kafkaTemplateMock.send(anyString(), anyString(), any(SearchRequest.class))).thenThrow(new RuntimeException());

    searchService.processSearchRequest(request);
  }

  @Test
  public void getSearchRequestBySearchId_Found() {
    String searchId = "someSearchId";
    MongoDBSearchRequest mongoDBSearchRequest = new MongoDBSearchRequest();
    mongoDBSearchRequest.setSearchId(searchId);

    SearchRequest searchRequest = new SearchRequest("123abcD", LocalDate.of(2024, 3, 20),
        LocalDate.of(2024, 3, 25), Arrays.asList(25, 30));
    mongoDBSearchRequest.setRequest(searchRequest);
    mongoDBSearchRequest.setCount(5);

    SearchResultResponse expectedResponse = new SearchResultResponse();
    expectedResponse.setSearchId(mongoDBSearchRequest.getSearchId());
    expectedResponse.setRequest(mongoDBSearchRequest.getRequest());
    expectedResponse.setCount(mongoDBSearchRequest.getCount());

    Mockito.when(repository.findBySearchId(searchId))
        .thenReturn(mongoDBSearchRequest);

    SearchResultResponse result = searchService.getSearchRequestBySearchId(searchId);

    assertEquals(expectedResponse, result);
  }

  @Test(expected = SearchException.class)
  public void getSearchRequestBySearchId_NotFound() {

    String searchId = "nonExistentSearchId";
    when(repository.findBySearchId(searchId)).thenReturn(null);

    searchService.getSearchRequestBySearchId(searchId);
  }

  @Test
  public void testGenerateSearchID() {
    SearchServiceImpl searchService = new SearchServiceImpl(null);

    String searchId = searchService.generateSearchID();

    assertTrue(searchId.matches("\\d{17}\\w{32}"));

    String anotherSearchId = searchService.generateSearchID();
    assertTrue(!searchId.equals(anotherSearchId));
  }

  private static SearchRequest createSampleSearchRequest() {
    List<Integer> ages = new ArrayList<>();
    ages.add(25);
    ages.add(30);

    LocalDate checkIn = LocalDate.of(2024, 3, 20);
    LocalDate checkOut = LocalDate.of(2024, 3, 25);

    return new SearchRequest("123abcD", checkIn, checkOut, ages);
  }
}
