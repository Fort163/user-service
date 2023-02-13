package ru.otr.sf.user.service.service;

import ru.otr.sf.user.service.mapper.dto.keycloak.GroupDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.RoleDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.UserDto;

import java.util.List;

public interface KeyCloakService {

    List<UserDto> getUser(boolean withRole, boolean withGroup);

    List<UserDto> getUserByGroup(String groupId,boolean withRole, boolean withGroup);

    List<UserDto> getUserByRole(String roleName, boolean withGroup);

    List<RoleDto> getRole();

    List<RoleDto> getRoleByUser(String userId);

    List<GroupDto> getGroup();

    List<GroupDto> getGroupByUser(String userId);



}
