package com.quick.recording.user.service;


import com.quick.recording.user.service.integration.FinishIntegration;
import com.quick.recording.user.service.integration.UserTestController;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.test.context.ActiveProfiles;

@Suite
@SuiteDisplayName("Test Suite for User Service")
@SelectClasses({
        UserTestController.class,
        FinishIntegration.class
})
@ActiveProfiles("test")
public class UserServiceAppSuiteTest {
}
