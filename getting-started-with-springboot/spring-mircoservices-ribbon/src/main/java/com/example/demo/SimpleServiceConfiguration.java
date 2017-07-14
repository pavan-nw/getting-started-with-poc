package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.WeightedResponseTimeRule;

public class SimpleServiceConfiguration {

	@Autowired
	private IClientConfig ribbonClientConfig;
	
	@Bean
	public IPing ping(IClientConfig config){
		return new PingUrl();
	}
	
	
	@Bean
	public IRule rule(IClientConfig config){
		return new WeightedResponseTimeRule();
	}
	
	
}
