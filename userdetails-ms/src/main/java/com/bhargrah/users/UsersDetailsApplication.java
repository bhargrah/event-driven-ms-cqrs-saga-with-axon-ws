package com.bhargrah.users;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UsersDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersDetailsApplication.class, args);
	}

}
