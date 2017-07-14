package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class SpringMicroservicesEurekaClient2Application {

	@Autowired
	private EurekaClient client;

	@RequestMapping("/serviceinfo")
	public String serviceInfo() {

		InstanceInfo info = client.getNextServerFromEureka("myOtherClient", false);
		return "This message from eureka client 2 and my home page is " + info.getHomePageUrl() + " and Ip is "
				+ info.getIPAddr();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringMicroservicesEurekaClient2Application.class, args);
	}
}
