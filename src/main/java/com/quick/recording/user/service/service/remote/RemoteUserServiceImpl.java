package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.service.auth.AuthServiceUserApi;
import com.quick.recording.user.service.repository.dto.AuthUserDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteUserServiceImpl extends
        CacheableMainRemoteServiceAbstract<AuthUserDto, AuthUserDtoRepository, AuthServiceUserApi>
        implements RemoteUserService {

    public RemoteUserServiceImpl() {
    }

    @Autowired
    public RemoteUserServiceImpl(AuthUserDtoRepository repository, AuthServiceUserApi service) {
        super(repository, service);
    }

    @Override
    public Class<AuthUserDto> getType() {
        return AuthUserDto.class;
    }
}
