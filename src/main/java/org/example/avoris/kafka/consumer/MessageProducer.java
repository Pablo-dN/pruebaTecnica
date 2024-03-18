package org.example.avoris.kafka.consumer;

import org.example.avoris.dto.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

  @Autowired
  private KafkaTemplate<String, SearchRequest> kafkaTemplate;

  public void sendMessage(String topic, SearchRequest searchRequest) {
    kafkaTemplate.send(topic, searchRequest);
  }

}
