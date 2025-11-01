package com.quick.recording.user.service.repository.dto;

import com.quick.recording.gateway.dto.user.UserInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserInfoDtoRepository extends JpaRepository<UserInfoDto, UUID> {
}
