package com.quick.recording.user.service.service.local;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.config.error.exeption.QRInternalServerErrorException;
import com.quick.recording.gateway.config.error.exeption.RemoteDateException;
import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import com.quick.recording.user.service.entity.UserInfoEntity;
import com.quick.recording.user.service.mapper.UserInfoMapper;
import com.quick.recording.user.service.repository.entity.UserInfoEntityRepository;
import com.quick.recording.user.service.service.remote.RemoteUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Log4j2
public class UserInfoServiceImpl
        extends CacheableMainServiceAbstract<UserInfoEntity, UserInfoDto, UserInfoEntityRepository, UserInfoMapper>
        implements UserInfoService {

    private final RemoteUserService remoteUserService;
    private final RoleService roleService;

    @Autowired
    public UserInfoServiceImpl(UserInfoEntityRepository repository, UserInfoMapper mapper,
                               MessageUtil messageUtil, StreamBridge streamBridge,
                               RemoteUserService remoteUserService, RoleService roleService) {
        super(repository, mapper, messageUtil, UserInfoEntity.class, streamBridge);
        this.remoteUserService = remoteUserService;
        this.roleService = roleService;
    }

    @Override
    protected void beforePost(UserInfoEntity entity, UserInfoDto dto) {
        entity.setUuid(dto.getUserId());
    }

    @Override
    public UserInfoDto byUuid(UUID uuid) {
        try {
            return super.byUuid(uuid);
        }catch (NotFoundException exception){
            if(remoteUserService.byUuid(uuid).getStatusCode().is2xxSuccessful()) {
                UserInfoDto userInfoDto = new UserInfoDto();
                userInfoDto.setUuid(uuid);
                userInfoDto.setUserId(uuid);
                return super.post(userInfoDto);
            }
            else {
                throw exception;
            }
        }
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void afterPatch(UserInfoDto oldDto, UserInfoDto newDto) {
        try {
            roleService.changeRoleIfNeeded(oldDto, newDto);
        } catch (RemoteDateException e) {
            log.error("Error when update role user rollback UserInfoEntity update.");
            throw new QRInternalServerErrorException(getMessageUtil().create("error.when.update.user"));
        }
    }


    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void afterPut(UserInfoDto oldDto, UserInfoDto newDto) {
        try {
            roleService.changeRoleIfNeeded(oldDto, newDto);
        } catch (RemoteDateException e) {
            log.error("Error when update role user rollback UserInfoEntity update.");
            throw new QRInternalServerErrorException(getMessageUtil().create("error.when.update.user"));
        }
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

    @Override
    public Class<UserInfoDto> getType() {
        return UserInfoDto.class;
    }
}
