package org.apawaskar.vehiclelocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@EnableBinding(Sink.class)
@SpringBootApplication
@EnableEurekaClient
public class WebsocketSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSubscriber.class);

	@Autowired
	private RestTemplate restTemplate;

	

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketSubscriber.class, args);
	}

	@StreamListener(Sink.INPUT)
	public void log(String msg) {
		LOGGER.info("Message recieved at websocket subscriber: {}", msg);

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> request = new HttpEntity<>(msg, headers);

		restTemplate.exchange("http://log-websocket-service/log", HttpMethod.POST, request, String.class);
	}
}
