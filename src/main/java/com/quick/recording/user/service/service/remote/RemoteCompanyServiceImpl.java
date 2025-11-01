package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceCompanyApi;
import com.quick.recording.user.service.repository.dto.CompanyDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteCompanyServiceImpl extends
        CacheableMainRemoteServiceAbstract<CompanyDto, CompanyDtoRepository, CompanyServiceCompanyApi>
        implements RemoteCompanyService {

    public RemoteCompanyServiceImpl() {
    }

    @Autowired
    public RemoteCompanyServiceImpl(CompanyDtoRepository repository, CompanyServiceCompanyApi service) {
        super(repository, service);
    }

    @Override
    public Class<CompanyDto> getType() {
        return CompanyDto.class;
    }

}
