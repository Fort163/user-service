package com.quick.recording.user.service.mapper;

import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.mapper.MainMapper;
import com.quick.recording.user.service.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserInfoMapper extends MainMapper<UserInfoEntity, UserInfoDto> {
}
