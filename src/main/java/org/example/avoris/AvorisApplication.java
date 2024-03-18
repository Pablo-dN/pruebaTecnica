package org.example.avoris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.example.avoris", "org.example.avoris.kafka.producer"})
public class AvorisApplication {

  public static void main(String[] args) {
    SpringApplication.run(AvorisApplication.class, args);
  }

}
