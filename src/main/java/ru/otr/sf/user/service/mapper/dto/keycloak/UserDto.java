package ru.otr.sf.user.service.mapper.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDto> roles;
    private List<GroupDto> groups;

}
