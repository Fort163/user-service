package com.quick.recording.employee.service.controller;

import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.user.UserDto;
import com.quick.recording.gateway.service.auth.AuthServiceUserApi;
import com.quick.recording.gateway.service.user.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    private final AuthServiceUserApi authServiceUserApi;

    @Override
    public ResponseEntity<UserDto> getTest(String name) {
        return null;
    }

    @Override
    @GetMapping({"/currentUser"})
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Витя Дробыш");
        userDto.setUuid(UUID.randomUUID());
        userDto.setWork("Рабочник");
        return ResponseEntity.ok(userDto);
    }

    @Override
    @GetMapping({"/userByCompany/{uuid}"})
    public ResponseEntity<List<UserDto>> usersByCompany(String uuid) {
        if(uuid!= null && !uuid.isEmpty()) {
            UserDto userDto = new UserDto();
            userDto.setName("Витя Дробыш");
            userDto.setAge(60);
            userDto.setUuid(UUID.randomUUID());
            UserDto userDto1 = new UserDto();
            userDto1.setName("Генадий Хазанов");
            userDto1.setAge(55);
            AuthUserDto userDto2 = new AuthUserDto();
            userDto2.setUuid(UUID.randomUUID());
            ResponseEntity<AuthUserDto> put = authServiceUserApi.patch(userDto2);
            userDto1.setUuid(UUID.randomUUID());
            return ResponseEntity.ok(List.of(userDto,userDto1));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping({"/userByName/{name}"})
    public ResponseEntity<UserDto> getUserByName(@PathVariable String name){
        if(name !=null && !name.isEmpty()) {
            UserDto userDto = new UserDto();
            userDto.setName("Петя Гитарист");
            userDto.setUuid(UUID.randomUUID());
            userDto.setAge(54);
            userDto.setWork("Гитарист");
            return ResponseEntity.ok(userDto);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

}
