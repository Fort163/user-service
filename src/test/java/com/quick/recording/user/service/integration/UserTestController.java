package com.quick.recording.user.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import com.quick.recording.user.service.service.local.RoleServiceImpl;
import com.quick.recording.user.service.service.local.UserInfoService;
import com.quick.recording.user.service.service.remote.RemoteCompanyServiceImpl;
import com.quick.recording.user.service.service.remote.RemoteEmployeeServiceImpl;
import com.quick.recording.user.service.service.remote.RemoteUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Test Api User Info in user service")
public class UserTestController extends MainTestController<UserInfoDto> {

    private TypeReference<UserInfoDto> typeDto = new TypeReference<UserInfoDto>() {
    };
    private TypeReference<Page<UserInfoDto>> typePageDto = new TypeReference<Page<UserInfoDto>>() {
    };
    private final UUID presentUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UUID notFoundUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    private UserInfoService userInfoService;

    @MockitoBean
    private RemoteUserServiceImpl remoteUserService;

    @MockitoBean
    private RemoteCompanyServiceImpl remoteCompanyService;

    @MockitoBean
    private RemoteEmployeeServiceImpl remoteEmployeeService;

    @MockitoBean
    private RoleServiceImpl roleService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(userInfoService);
    }

    @Override
    public String uri() {
        return "/api/v1/user";
    }

    @Test
    @Order(1)
    public void loadContext() {
        assertThat(userInfoService).isNotNull();
        assertThat(remoteUserService).isNotNull();
        assertThat(remoteCompanyService).isNotNull();
        assertThat(remoteEmployeeService).isNotNull();
    }

    @BeforeEach
    void setUp() {
        ApiError apiError = new ApiError();

        AuthUserDto userDto = new AuthUserDto();
        userDto.setUuid(presentUUID);
        userDto.setUsername("Test");

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUuid(presentUUID);
        companyDto.setName("Test");

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setUuid(presentUUID);
        employeeDto.setCompany(companyDto);

        //User service
        when(remoteUserService.byUuid(presentUUID))
                .thenReturn(ResponseEntity.ok(userDto));

        when(remoteUserService.byUuid(notFoundUUID))
                .thenAnswer(invocationOnMock -> ResponseEntity.status(500).body(apiError));

        when(remoteUserService.getType())
                .thenReturn(AuthUserDto.class);

        //Company service
        when(remoteCompanyService.byUuid(presentUUID))
                .thenReturn(ResponseEntity.ok(companyDto));

        when(remoteCompanyService.getType())
                .thenReturn(CompanyDto.class);

        when(remoteCompanyService.byUuid(notFoundUUID))
                .thenAnswer(invocationOnMock -> ResponseEntity.status(500).body(apiError));

        //Employee service
        when(remoteEmployeeService.byUuid(presentUUID))
                .thenReturn(ResponseEntity.ok(employeeDto));

        when(remoteEmployeeService.getType())
                .thenReturn(EmployeeDto.class);

        when(remoteEmployeeService.byUuid(notFoundUUID))
                .thenAnswer(invocationOnMock -> ResponseEntity.status(500).body(apiError));

    }

    @Override
    public List<TestCase<UserInfoDto, ?>> postGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.getCode().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("userId"))
        ).isTrue());

        UserInfoDto fail2 = new UserInfoDto();
        fail2.setUserId(notFoundUUID);
        fail2.setCompanyId(notFoundUUID);
        fail2.setEmployeeId(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.getCode().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("userId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("companyId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employeeId"))
        ).isTrue());

        UserInfoDto success = new UserInfoDto();
        success.setUserId(presentUUID);
        success.setCompanyId(presentUUID);
        success.setEmployeeId(presentUUID);
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.getCode().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(result.getResult().getUserId()));
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUser()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployee()).isNotNull());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        fail1.setUuid(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("UserInfoEntity")).isTrue());

        UserInfoDto success = this.getLastCreateObjectClone();
        TestCase<UserInfoDto, UserInfoDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getUser()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getCompany()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getEmployee()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> deleteHardGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        fail1.setUuid(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("UserInfoEntity")).isTrue());

        UserInfoDto fail2 = new UserInfoDto();
        fail2.setUuid(null);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        UserInfoDto success = this.getLastCreateObjectClone();
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUser()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployee()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> byUUIDAfterDeleteGetTestCases() {
        UserInfoDto fail1 = this.getLastDeletedObjectClone();
        fail1.setUuid(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("UserInfoEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> listGetTestCases() {
        UserInfoDto successAll = new UserInfoDto();
        TestCase<UserInfoDto, Page<UserInfoDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test1.addTest(result -> assertThat(result.getResult().getTotalElements() > 0).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getContent().get(0).getUser()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().getContent().get(0).getCompany()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().getContent().get(0).getEmployee()).isNotNull());

        UserInfoDto successEmpty = new UserInfoDto();
        successEmpty.setUserId(notFoundUUID);
        TestCase<UserInfoDto, Page<UserInfoDto>> test2 = new TestCase<>(successEmpty, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(0));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> putGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.getCode().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("userId"))
        ).isTrue());

        UserInfoDto fail2 = this.getLastCreateObjectClone();
        fail2.setUserId(notFoundUUID);
        fail2.setCompanyId(notFoundUUID);
        fail2.setEmployeeId(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("userId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("companyId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employeeId"))
        ).isTrue());

        UserInfoDto success1 = this.getLastCreateObjectClone();
        success1.setCompanyId(null);
        success1.setEmployeeId(null);
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success1, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success1.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getCompanyId()).isNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany()).isNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployeeId()).isNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployee()).isNull());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> patchGetTestCases() {
        UserInfoDto createObject = this.getLastCreateObjectClone();

        UserInfoDto fail1 = new UserInfoDto();
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("uuid")).isTrue());

        UserInfoDto fail2 = new UserInfoDto();
        fail2.setUuid(createObject.getUuid());
        fail2.setUserId(notFoundUUID);
        fail2.setCompanyId(notFoundUUID);
        fail2.setEmployeeId(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("userId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("companyId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employeeId"))
        ).isTrue());

        UserInfoDto success = new UserInfoDto();
        success.setUuid(createObject.getUuid());
        success.setCompanyId(presentUUID);
        success.setEmployeeId(presentUUID);
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(createObject.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getUserId()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUser()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompanyId()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployeeId()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getEmployee()).isNotNull());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> deleteSoftGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        fail1.setUuid(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("UserInfoEntity")).isTrue());

        UserInfoDto fail2 = new UserInfoDto();
        fail2.setUuid(null);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        UserInfoDto success = this.getLastCreateObjectClone();
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<UserInfoDto, ?>> restoreGetTestCases() {
        UserInfoDto fail1 = new UserInfoDto();
        fail1.setUuid(notFoundUUID);
        TestCase<UserInfoDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("UserInfoEntity")).isTrue());

        UserInfoDto fail2 = new UserInfoDto();
        fail2.setUuid(null);
        TestCase<UserInfoDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        UserInfoDto success = this.getLastCreateObjectClone();
        TestCase<UserInfoDto, UserInfoDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1, test2, test3);
    }

}
