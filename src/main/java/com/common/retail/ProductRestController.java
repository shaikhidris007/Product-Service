package com.common.retail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(value="/rest/" , method=RequestMethod.GET)
public class ProductRestController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	
	@GetMapping("/client")
	public String welcomeProduct() {
		return "welcome Client";
	}
	
	@HystrixCommand(fallbackMethod = "welcomeFallback")
	@GetMapping("/product")
	public String welcome() {
		String url="http://SHOPPINGCART-SERVICE/shopping";
		ServiceInstance serviceInstance=loadBalancer.choose("SHOPPINGCART-SERVICE");
		System.out.println("Which port is it connecting to"+"      " +serviceInstance.getUri());
		
		return restTemplate.getForObject(url, String.class);
	}
	
	public String welcomeFallback() {
		return "Welcome to Fallback method";
	}

}
