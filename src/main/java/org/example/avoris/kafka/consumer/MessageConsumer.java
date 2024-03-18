package org.example.avoris.kafka.consumer;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.avoris.dto.SearchRequest;
import org.example.avoris.exception.SearchException;
import org.example.avoris.model.MongoDBSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MessageConsumer {

  @Autowired
  private MongoTemplate mongoTemplate;
  private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

  private final CountDownLatch latch = new CountDownLatch(1);
  private ConsumerRecord<String, SearchRequest> receivedRecord;

  public CountDownLatch getLatch() {
    return latch;
  }

  public ConsumerRecord<String, SearchRequest> getReceivedRecord() {
    return receivedRecord;
  }

  @KafkaListener(topics = "hotel_availability_searches", groupId = "my-group-id")
  public void consume(ConsumerRecord<String, SearchRequest> record) {
    try {
      log.info("Received raw record: {}", record.toString());      String searchId = record.key();
      MongoDBSearchRequest mongoDBSearchRequest = new MongoDBSearchRequest();
      mongoDBSearchRequest.setSearchId(searchId);
      mongoDBSearchRequest.setRequest(record.value());
      Optional<MongoDBSearchRequest> savedRequest = Optional.of(mongoTemplate.save(mongoDBSearchRequest));
      MongoDBSearchRequest saved = savedRequest.orElseThrow(() -> new SearchException("Failed to save search request in MongoDB"));
      log.info("Search request persisted in MongoDB with ID: {}", saved.getSearchId());      int count = 100;
      mongoTemplate.updateFirst(query(where("searchId").is(searchId)), update("count", count), MongoDBSearchRequest.class);
      receivedRecord = record;
      latch.countDown();
    } catch (Exception e) {
      throw new SearchException("Error consuming Kafka message: " + e.getMessage());
    }
  }
}


