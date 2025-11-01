package com.quick.recording.user.service.integration;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.user.service.service.local.UserInfoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Finish integration test company service")
public class FinishIntegration {

    @LocalServerPort
    private int port;

    @Autowired
    private UserInfoService userInfoService;

    @Test
    @Order(1)
    void loadContext() {
        assertThat(userInfoService).isNotNull();
    }

    @Test
    @Order(2)
    void clearDataBase() {
        clearService(userInfoService);
        assertThat(userInfoService.findAll().isEmpty()).isTrue();
    }

    private void clearService(MainService<? extends SmartEntity, ? extends SmartDto> service){
        List<? extends SmartEntity> all = service.findAll();
        if (Objects.nonNull(all) && !all.isEmpty()) {
            service.deleteAll(all.stream().map(SmartEntity::getUuid).collect(Collectors.toList()));
        }
    }

}
