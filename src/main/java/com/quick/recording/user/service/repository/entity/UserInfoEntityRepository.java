package com.quick.recording.user.service.repository.entity;

import com.quick.recording.user.service.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserInfoEntityRepository extends JpaRepository<UserInfoEntity, UUID> {
}
