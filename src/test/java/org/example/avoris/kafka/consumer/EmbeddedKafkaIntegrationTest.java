package org.example.avoris.kafka.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.avoris.dto.SearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class EmbeddedKafkaIntegrationTest {

  @Autowired
  private MessageConsumer consumer;

  @Autowired
  private KafkaTemplate<String, SearchRequest> kafkaTemplate;

  @Value("${test.topic}")
  private String topic;

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
      throws Exception {
    SearchRequest request = createSampleSearchRequest();
    String searchId = "123456789"; // Aquí establece el ID de búsqueda

    kafkaTemplate.send(new ProducerRecord<>(topic, searchId, request));

    boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);

    ConsumerRecord<String, SearchRequest> receivedRecord = consumer.getReceivedRecord();
    assertNotNull(receivedRecord);
    assertEquals(searchId, receivedRecord.key());
    assertEquals(request, receivedRecord.value());
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
