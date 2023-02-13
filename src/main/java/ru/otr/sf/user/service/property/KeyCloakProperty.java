package ru.otr.sf.user.service.property;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;

@ConfigurationProperties(
        prefix = "org.keycloak"
)
@Getter
@Setter
@Slf4j
public class KeyCloakProperty {

    public KeyCloakProperty() {
        this.url = new Url();
        this.auth = new Auth();
    }

    @NestedConfigurationProperty
    private Url url;

    @NestedConfigurationProperty
    private Auth auth;

    @Getter
    @Setter
    public static class Url {
        private String base;
        private String login;
        private String users;
        private String roles;
        private String groups;
        private String usersByRole;
        private String groupByUser;
        private String groupMember;
        private String rolesByUser;
    }

    @Getter
    @Setter
    public static class Auth {
        private String user;
        private String password;
        private String clientId;
        /**
         * Учетные данные для получения токена
         * @return
         */
        public MultiValueMap<String, String> getCredentials(){
            MultiValueMap<String, String> credentials = new LinkedMultiValueMap();
            credentials.add("client_id", getClientId());
            credentials.add("username", getUser());
            credentials.add("password", getPassword());
            credentials.add("grant_type", "password");
            return credentials;
        }
    }

    @PostConstruct
    private void checkProperty(){
        if(!checkCredential(getUrl().getBase())) {
            log.warn("Setting <org.keycloak.url.base> not found! \n Add property <org.keycloak.url.base> to property file (application.yml or application.property)");
        }
        if(!checkCredential(getUrl().getLogin())) {
            log.warn("Setting <org.keycloak.url.login> not found! \n Add property <org.keycloak.url.login> to property file (application.yml or application.property)");
        }
        if(!checkCredential(getAuth().getUser())) {
            log.warn("Setting <org.keycloak.auth.user> not found! \n Add property <org.keycloak.auth.user> to property file (application.yml or application.property)");
        }
        if(!checkCredential(getAuth().getPassword())) {
            log.warn("Setting <org.keycloak.auth.password> not found! \n Add property <org.keycloak.auth.password> to property file (application.yml or application.property)");
        }
        if(!checkCredential(getAuth().getClientId())) {
            log.warn("Setting <org.keycloak.auth.client_id> not found! \n Add property <org.keycloak.auth.client_id> to property file (application.yml or application.property)");
        }
    }

    private Boolean checkCredential(String credential){
        return !(credential == null || credential.isEmpty());
    }

}
