package org.example.avoris.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.example.avoris.dto.SearchRequest;
import org.example.avoris.dto.SearchResultResponse;
import org.example.avoris.exception.SearchException;
import org.example.avoris.model.MongoDBSearchRequest;
import org.example.avoris.repository.SearchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

  private final KafkaTemplate<String, SearchRequest> kafkaTemplate;

  @Autowired
  private SearchRepository repository;

  private ModelMapper modelMapper;

  @Autowired
  private MongoTemplate mongoTemplate;


  public SearchServiceImpl(KafkaTemplate<String, SearchRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.modelMapper = new ModelMapper();
  }

  public String processSearchRequest(SearchRequest request) {
    try {
      String searchId = generateSearchID();
      kafkaTemplate.send("hotel_availability_searches", searchId, request);
      return searchId;
    } catch (Exception e) {
      throw new SearchException("Error processing search request: " + e.getMessage());
    }
  }

  @Override
  public SearchResultResponse getSearchRequestBySearchId(String searchId) {
    MongoDBSearchRequest mongoDBSearchRequest = repository.findBySearchId(searchId);

    if (mongoDBSearchRequest != null) {
      return modelMapper.map(mongoDBSearchRequest, SearchResultResponse.class);
    } else {
      throw new SearchException("Search request not found for ID: " + searchId);
    }
  }

  @Override
  public String generateSearchID() {
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    String uniqueId = UUID.randomUUID().toString().replace("-", "");
    return formattedDateTime + uniqueId;
  }
}
