package com.quick.recording.user.service.service.remote;

import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceEmployeeApi;
import com.quick.recording.user.service.repository.dto.EmployeeDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteEmployeeServiceImpl extends
        CacheableMainRemoteServiceAbstract<EmployeeDto, EmployeeDtoRepository, CompanyServiceEmployeeApi>
        implements RemoteEmployeeService {

    public RemoteEmployeeServiceImpl() {
    }

    @Autowired
    public RemoteEmployeeServiceImpl(EmployeeDtoRepository repository, CompanyServiceEmployeeApi service) {
        super(repository, service);
    }

    @Override
    public Class<EmployeeDto> getType() {
        return EmployeeDto.class;
    }

}
