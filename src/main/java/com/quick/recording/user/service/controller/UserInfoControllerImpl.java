package com.quick.recording.user.service.controller;

import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.user.UserServiceUserInfoApi;
import com.quick.recording.user.service.entity.UserInfoEntity;
import com.quick.recording.user.service.repository.dto.UserInfoDtoRepository;
import com.quick.recording.user.service.service.local.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserInfoControllerImpl
        extends CacheableMainControllerAbstract<UserInfoDto, UserInfoEntity, UserInfoDtoRepository, UserInfoService>
        implements UserServiceUserInfoApi {

    @Autowired
    public UserInfoControllerImpl(UserInfoService activityService, UserInfoDtoRepository repository) {
        super(activityService, repository);
    }

}
