package com.sahil.service.highlight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HighlightApplication {

  public static void main(String[] args) {
    SpringApplication.run(HighlightApplication.class, args);
  }

}
