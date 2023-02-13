package ru.otr.sf.user.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otr.sf.user.service.mapper.dto.keycloak.UserDto;
import ru.otr.sf.user.service.service.KeyCloakService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final KeyCloakService keyCloakService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> users(@RequestParam(value = "withRole", required = false, defaultValue = "false") boolean withRole,
                                               @RequestParam(value = "withGroup", required = false, defaultValue = "false") boolean withGroup) {
        return ResponseEntity.ok(keyCloakService.getUser(withRole,withGroup));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UserDto>> usersByGroup(@RequestParam(value = "withRole", required = false, defaultValue = "false") boolean withRole,
                                               @RequestParam(value = "withGroup", required = false, defaultValue = "false") boolean withGroup,
                                               @PathParam(value = "groupId") String groupId) {
        return ResponseEntity.ok(keyCloakService.getUserByGroup(groupId,withRole,withGroup));
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserDto>> usersByRole(@RequestParam(value = "withGroup", required = false, defaultValue = "false") boolean withGroup,
                                               @PathParam(value = "roleName") String roleName) {
        return ResponseEntity.ok(keyCloakService.getUserByRole(roleName,withGroup));
    }

}
