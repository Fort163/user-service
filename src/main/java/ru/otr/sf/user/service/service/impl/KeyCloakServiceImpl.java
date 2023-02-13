package ru.otr.sf.user.service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otr.sf.user.service.mapper.dto.keycloak.GroupDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.RoleDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.UserDto;
import ru.otr.sf.user.service.property.KeyCloakProperty;
import ru.otr.sf.user.service.service.KeyCloakService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {

    @Autowired
    private final KeyCloakProperty keyCloakProperty;

    @Autowired
    @Qualifier("restTemplateLocalCredentials")
    private final RestTemplate restTemplate;

    @Override
    public List<UserDto> getUser(boolean withRole, boolean withGroup) {
        URI uri = createUri(this.keyCloakProperty.getUrl().getUsers());
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<UserDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<UserDto>>() {
                });
        List<UserDto> resultList = result.getBody();
        if(withRole){
            addRole(resultList);
        }
        if(withGroup){
            addGroup(resultList);
        }
        return resultList;
    }

    @Override
    public List<UserDto> getUserByGroup(String groupId, boolean withRole, boolean withGroup) {
        Map<String, String> uriVariables = new HashMap<>(){{
            put("group",groupId);
        }};
        URI uri = createUri(this.keyCloakProperty.getUrl().getGroupMember(),uriVariables);
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<UserDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<UserDto>>() {
                });
        List<UserDto> resultList = result.getBody();
        if(withRole){
            addRole(resultList);
        }
        if(withGroup){
            addGroup(resultList);
        }
        return resultList;
    }

    @Override
    public List<UserDto> getUserByRole(String roleName, boolean withGroup) {
        List<UserDto> userList = getUser(true, withGroup);
        List<UserDto> filter = userList.stream().filter(item -> item.getRoles().stream().filter(role -> role.getName().equals(roleName)).count() > 0).collect(Collectors.toList());
        return filter;
    }

    @Override
    public List<RoleDto> getRole() {
        URI uri = createUri(this.keyCloakProperty.getUrl().getRoles());
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<RoleDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<RoleDto>>() {
                });
        return result.getBody();
    }

    @Override
    public List<RoleDto> getRoleByUser(String userId) {
        Map<String, String> uriVariables = new HashMap<>(){{
            put("user",userId);
        }};
        URI uri = createUri(this.keyCloakProperty.getUrl().getRolesByUser(),uriVariables);
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<RoleDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<RoleDto>>() {
                });
        List<RoleDto> resultList = result.getBody();
        return resultList;
    }

    @Override
    public List<GroupDto> getGroup() {
        URI uri = createUri(this.keyCloakProperty.getUrl().getGroups());
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<GroupDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<GroupDto>>() {
                });
        return result.getBody();
    }

    @Override
    public List<GroupDto> getGroupByUser(String userId) {
        Map<String, String> uriVariables = new HashMap<>(){{
            put("user",userId);
        }};
        URI uri = createUri(this.keyCloakProperty.getUrl().getGroupByUser(),uriVariables);
        RequestEntity request = request(HttpMethod.GET, uri);
        ResponseEntity<List<GroupDto>> result = restTemplate.exchange(request,
                new ParameterizedTypeReference<List<GroupDto>>() {
                });
        return result.getBody();
    }

    private void addRole(List<UserDto> list){
        list.stream().forEach(item -> {
            item.setRoles(getRoleByUser(item.getId()));
        });
    }

    private void addGroup(List<UserDto> list){
        list.stream().forEach(item -> {
            item.setGroups(getGroupByUser(item.getId()));
        });
    }

    private RequestEntity request(HttpMethod method, URI url){
        return new RequestEntity(method,url);
    }

    private URI createUri(String url){
        return restTemplate.getUriTemplateHandler().expand(keyCloakProperty.getUrl().getBase() + url);
    }

    private URI createUri(String url, Map<String, String> uriVariables){
        return restTemplate.getUriTemplateHandler().expand(keyCloakProperty.getUrl().getBase() + url, uriVariables);
    }

}
