package ru.otr.sf.user.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otr.sf.user.service.mapper.dto.keycloak.RoleDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.UserDto;
import ru.otr.sf.user.service.service.KeyCloakService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("api/role")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private final KeyCloakService keyCloakService;

    @GetMapping()
    public ResponseEntity<List<RoleDto>> roles() {
        return ResponseEntity.ok(keyCloakService.getRole());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoleDto>> rolesByUser(@PathParam(value = "userId") String userId) {
        return ResponseEntity.ok(keyCloakService.getRoleByUser(userId));
    }

}
