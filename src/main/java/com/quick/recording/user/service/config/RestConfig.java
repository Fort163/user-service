package com.quick.recording.user.service.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig {

    /*@Bean
    public WebClient webClientCompany(){
        return WebClient.builder()
                .baseUrl("http://localhost:8097/")
                .build();
    }*/

    /*@Bean
    @Autowired
    @Qualifier("webClientCompany")
    public TestController serviceCompany(WebClient webClient){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(TestController.class);
    }*/

}
