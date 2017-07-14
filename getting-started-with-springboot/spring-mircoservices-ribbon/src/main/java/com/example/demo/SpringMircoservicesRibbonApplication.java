package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@RibbonClient(name="simple-service-2", configuration=SimpleServiceConfiguration.class)
public class SpringMircoservicesRibbonApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/startClient")
	public String startClient(){
		return this.restTemplate.getForObject("http://simple-service-2/execute", String.class);
	}
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringMircoservicesRibbonApplication.class, args);
	}
}
