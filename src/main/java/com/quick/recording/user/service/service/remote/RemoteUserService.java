package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.auth.Role2UserDto;
import com.quick.recording.gateway.dto.auth.RoleDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RemoteUserService extends MainRemoteService<AuthUserDto> {

    ResponseEntity<List<RoleDto>> addRole(Role2UserDto dto);

    ResponseEntity<List<RoleDto>> removeRole(Role2UserDto dto);

}
