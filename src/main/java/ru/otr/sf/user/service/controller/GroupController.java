package ru.otr.sf.user.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otr.sf.user.service.mapper.dto.keycloak.GroupDto;
import ru.otr.sf.user.service.mapper.dto.keycloak.RoleDto;
import ru.otr.sf.user.service.service.KeyCloakService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("api/group")
@RequiredArgsConstructor
public class GroupController {

    @Autowired
    private final KeyCloakService keyCloakService;

    @GetMapping()
    public ResponseEntity<List<GroupDto>> groups() {
        return ResponseEntity.ok(keyCloakService.getGroup());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupDto>> groupsByUser(@PathParam(value = "userId") String userId) {
        return ResponseEntity.ok(keyCloakService.getGroupByUser(userId));
    }

}
