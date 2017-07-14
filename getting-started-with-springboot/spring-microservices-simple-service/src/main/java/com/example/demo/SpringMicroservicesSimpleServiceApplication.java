package com.example.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tailf.jnc.Element;
import com.tailf.jnc.JNCException;

@SpringBootApplication
@RestController
public class SpringMicroservicesSimpleServiceApplication {

	@RequestMapping("/service")
	public Map execute(@RequestParam("nodeIp") String ipaddr) throws IOException, JNCException{
		Map<String, String> map = new HashMap<>();
		/*map.put("test1", "test2");
		map.put("test3", "test4");
		map.put("test4", "test5");*/
		
		System.out.println("Ip Addr: "+ ipaddr);
		
		NEClient neClient = new NEClient();
		Element element = neClient.getMeConfigViaRpc();
		printElements(element, map);
		
		return map;
	}
	
	private static void printElements(Element element, Map map) {
		if (element.getChildren() != null) {

			for (Element element1 : element.getChildren()) {
				printElements(element1, map);

			}
			System.out.println("\n");
		} else {
			System.out.println(element.name + " = " + element.getValue());
			map.put(element.name, element.getValue());
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringMicroservicesSimpleServiceApplication.class, args);
	}
}
