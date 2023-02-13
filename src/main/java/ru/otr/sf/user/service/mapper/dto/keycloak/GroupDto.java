package ru.otr.sf.user.service.mapper.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDto {

    private String id;
    private String name;

}
