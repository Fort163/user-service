package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.auth.RoleDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteServiceAbstract;
import com.quick.recording.gateway.service.auth.AuthServiceRoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteRoleServiceImpl extends MainRemoteServiceAbstract<RoleDto, AuthServiceRoleApi>
        implements RemoteRoleService {

    public RemoteRoleServiceImpl() {
    }

    @Autowired
    public RemoteRoleServiceImpl( AuthServiceRoleApi service) {
        super(service);
    }

    @Override
    public Class<RoleDto> getType() {
        return RoleDto.class;
    }
}
