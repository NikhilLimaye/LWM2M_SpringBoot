package com.cmpe273.lwm2mClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.cmpe273.lwm2mClient.model.Device;

@SpringBootApplication
public class Lwm2mClientApplication {

	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(Lwm2mClientApplication.class, args);
	}

}
