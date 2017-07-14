package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;

public class AnimalConfigBean {

	@Bean
	public List<String> animals() {
		return Arrays.asList("Lion", "Elephant");
	}
}
