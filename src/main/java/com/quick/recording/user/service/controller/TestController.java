package com.quick.recording.user.service.controller;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.ScheduleDto;
import com.quick.recording.gateway.service.company.CompanyController;
import com.quick.recording.gateway.service.company.ScheduleController;
import com.quick.recording.resource.service.anatation.CurrentUser;
import com.quick.recording.resource.service.security.QROAuth2AuthenticatedPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ScheduleController scheduleController;

    @Autowired
    private CompanyController companyController;

    @GetMapping("/show")
    public ResponseEntity<List<Object>> show(@CurrentUser QROAuth2AuthenticatedPrincipal user){
        ResponseEntity<CompanyDto> company = companyController.getCompany(user);
        /*
        ResponseEntity<ScheduleDto> scheduleDtoResponseEntity = scheduleController.scheduleByCompanyUuid(company.getBody().getUuid().toString());
        ResponseEntity<List<CompanyDto>> companyList = companyController.getCompanyList();*/
        return  ResponseEntity.ok(List.of(company.getBody())) ;
    }

}
