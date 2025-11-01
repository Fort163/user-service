package com.quick.recording.user.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEntity extends SmartEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "company_id")
    private UUID companyId;
    @Column(name = "employee_id")
    private UUID employeeId;

}
