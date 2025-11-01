package com.quick.recording.user.service.service.local;

import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.user.service.entity.UserInfoEntity;

public interface UserInfoService extends MainService<UserInfoEntity, UserInfoDto> {
}
