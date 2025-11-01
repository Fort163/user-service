package com.quick.recording.user.service.repository.dto;

import com.quick.recording.gateway.dto.company.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeDtoRepository extends JpaRepository<EmployeeDto, UUID> {
}
