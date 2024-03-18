package org.example.avoris.controller;

import org.example.avoris.dto.SearchResultResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.avoris.dto.SearchRequest;
import org.example.avoris.exception.SearchException;
import org.example.avoris.service.SearchService;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private SearchService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Mock
  private KafkaTemplate<String, SearchRequest> kafkaTemplate;

  @Test
  public void testProcessSearchRequest_Success() {

    SearchRequest request = createSampleSearchRequest();
    given(service.processSearchRequest(request)).willReturn("123456789");

    String searchId = service.processSearchRequest(request);
    assertEquals("123456789", searchId);
  }

  @Test
  public void testProcessSearchRequest_Exception() throws Exception {
    // Configurar el servicio para lanzar una excepción cuando se llame al método processSearchRequest
    doThrow(new SearchException("Test exception")).when(service).processSearchRequest(any(SearchRequest.class));

    // Construir el controlador y realizar la solicitud simulada
    SearchController searchController = new SearchController(service);
    mvc = MockMvcBuilders.standaloneSetup(searchController).build();

    mvc.perform(post("/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"hotelId\": \"1234aBc\",\n"
                + "\"checkIn\": \"29/12/2024\",\n"
                + "\"checkOut\": \"31/12/2024\",\n"
                + "\"ages\": [30, 29, 1, 3]}"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void testGetCount_SearchResultExists() throws Exception {
    SearchResultResponse mockResponse = new SearchResultResponse();
    when(service.getSearchRequestBySearchId("20240317151249628977bfcb3ab344e15a215bc8235609b6c")).thenReturn(mockResponse);

    mvc.perform(MockMvcRequestBuilders.get("/search/count?searchId=20240317151249628977bfcb3ab344e15a215bc8235609b6c")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetCount_NotFound() throws Exception {
    String searchId = "nonExistingSearchId";

    given(service.getSearchRequestBySearchId(searchId)).willReturn(null);

    mvc.perform(MockMvcRequestBuilders.get("/search/count?searchId=" + searchId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  // En tu método de prueba testGetCount_InternalServerError()
  @Test
  public void testGetCount_InternalServerError() throws Exception {
    String searchId = "exampleSearchId";
    String errorMessage = "Test error message";

    given(service.getSearchRequestBySearchId(searchId)).willThrow(new SearchException(errorMessage));

    mvc.perform(MockMvcRequestBuilders.get("/search/count?searchId=" + searchId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
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
