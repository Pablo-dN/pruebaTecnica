package org.example.avoris.kafka.producer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.avoris.dto.SearchRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  @Bean
  public ConsumerFactory<String, SearchRequest> consumerFactory() {
    JsonDeserializer<SearchRequest> deserializer = new JsonDeserializer<>(SearchRequest.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);

    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, SearchRequest> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, SearchRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

}
