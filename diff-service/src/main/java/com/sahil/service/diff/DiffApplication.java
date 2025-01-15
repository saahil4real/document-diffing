package com.sahil.service.diff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DiffApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiffApplication.class, args);
  }

}
