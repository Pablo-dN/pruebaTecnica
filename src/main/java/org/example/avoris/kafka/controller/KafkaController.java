package org.example.avoris.kafka.controller;

import org.example.avoris.dto.SearchRequest;
import org.example.avoris.kafka.consumer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

  @Autowired
  private MessageProducer messageProducer;

  @PostMapping("/send")
  public String sendMessage(@RequestParam("message") SearchRequest searchRequest) {
    messageProducer.sendMessage("my-topic", searchRequest);
    return "Message sent: " + searchRequest;
  }

  @RequestMapping("/")
  public @ResponseBody String greeting() {
    return "Hello, World";
  }

}
