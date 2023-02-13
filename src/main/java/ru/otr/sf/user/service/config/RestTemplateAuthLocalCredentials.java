package ru.otr.sf.user.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otr.sf.user.service.mapper.dto.keycloak.AuthResponse;
import ru.otr.sf.user.service.property.KeyCloakProperty;

import java.util.List;


@EnableConfigurationProperties(KeyCloakProperty.class)
@Component("restTemplateLocalCredentials")
@Slf4j
public class RestTemplateAuthLocalCredentials extends RestTemplate {
    private KeyCloakProperty keyCloakProperty;

    @Autowired
    public RestTemplateAuthLocalCredentials(KeyCloakProperty keyCloakProperty) {
        this.keyCloakProperty = keyCloakProperty;
        List<ClientHttpRequestInterceptor> interceptors = this.getInterceptors();
        ClientHttpRequestInterceptor clientHttpRequestInterceptor = (request, body, execution) -> {
           if(checkLocalCredentials()) {
               request.getHeaders().setBearerAuth(getToken());
            }
            return execution.execute(request, body);
        };

        interceptors.add(clientHttpRequestInterceptor);
        this.setInterceptors(interceptors);
    }

    /**
     * Проверка настроек
     * @return
     */
    private Boolean checkLocalCredentials(){
        return checkCredential(keyCloakProperty.getUrl().getBase()) && checkCredential(keyCloakProperty.getUrl().getLogin()) && checkCredential(keyCloakProperty.getAuth().getUser()) && checkCredential(keyCloakProperty.getAuth().getPassword()) && checkCredential(keyCloakProperty.getAuth().getClientId());
    }

    /**
     * Проверка поля
     * @param credential
     * @return
     */
    private Boolean checkCredential(String credential){
        return !(credential == null || credential.isEmpty());
    }

    /**
     * Получение токена доступа
     * @return
     */
    private String getToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<AuthResponse> objectResponseEntity = restTemplate.postForEntity(
                keyCloakProperty.getUrl().getBase() + keyCloakProperty.getUrl().getLogin(),
                new HttpEntity<>(keyCloakProperty.getAuth().getCredentials(), headers), AuthResponse.class);

        return objectResponseEntity.getBody().getAccessToken();
    }
}
