package com.quick.recording.user.service;

import com.quick.recording.gateway.config.cache.CacheStreamConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication(scanBasePackages = {"com.quick.recording"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.quick.recording.gateway.service")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@RefreshScope
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(UserServiceApplication.class).build(args);
		application.addInitializers(new CacheStreamConfigurer());
		application.run();
	}

}
