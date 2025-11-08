package com.quick.recording.user.service.service.local;

import com.quick.recording.gateway.config.error.exeption.RemoteDateException;
import com.quick.recording.gateway.dto.user.UserInfoDto;

public interface RoleService {

    void changeRoleIfNeeded(UserInfoDto oldDto, UserInfoDto newDto) throws RemoteDateException;

}
