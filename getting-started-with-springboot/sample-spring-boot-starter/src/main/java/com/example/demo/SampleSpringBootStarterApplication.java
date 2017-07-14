package com.example.demo;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import(AnimalConfigBean.class)
@ImportResource("/simple-bean.xml")
public class SampleSpringBootStarterApplication {

	@Autowired
	@Resource(name="animals")
	List<String> animalList;
	
	@Autowired
	@Resource(name="cats")
	List<String> catList;
	
	@Autowired
	@Resource(name="pet-animals")
	List<String> petList;
	
	@Bean
	public List<String> cats() {
		return Arrays.asList("Bengal", "Tiger");
	}

	@RequestMapping("/cats")
	public String showCats() {
		return String.join(",", catList);
	}
	
	@RequestMapping("/animals")
	public String showAnimals() {
		return String.join(",", animalList);
	}
	
	@RequestMapping("/pets")
	public String showPets() {
		return String.join(",", petList);
	}
	
	@RequestMapping("/")
	public String message() {
		return "It works..!";
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleSpringBootStarterApplication.class, args);
	}
}
