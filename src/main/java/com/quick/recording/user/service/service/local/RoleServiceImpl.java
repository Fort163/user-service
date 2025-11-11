package com.quick.recording.user.service.service.local;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.RemoteDateException;
import com.quick.recording.gateway.dto.auth.Role2UserDto;
import com.quick.recording.gateway.dto.auth.RoleDto;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.dto.notification.message.NotificationMessageDto;
import com.quick.recording.gateway.dto.user.UserInfoDto;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import com.quick.recording.gateway.enumerated.MessageType;
import com.quick.recording.gateway.enumerated.Project;
import com.quick.recording.gateway.enumerated.SendType;
import com.quick.recording.resource.service.anatation.WithServerAuth;
import com.quick.recording.user.service.repository.dto.UserInfoDtoRepository;
import com.quick.recording.user.service.service.remote.RemoteEmployeeService;
import com.quick.recording.user.service.service.remote.RemoteNotificationMessageService;
import com.quick.recording.user.service.service.remote.RemoteRoleService;
import com.quick.recording.user.service.service.remote.RemoteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.quick.recording.gateway.Constant.ROLE_SIMPLE_USER;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    @Value("${spring.application.name}")
    private String appName;

    private final RemoteUserService remoteUserService;
    private final RemoteEmployeeService remoteEmployeeService;
    private final RemoteRoleService roleService;
    private final RemoteNotificationMessageService messageService;
    private final UserInfoDtoRepository userInfoDtoRepository;
    private final MessageUtil messageUtil;

    @Override
    @WithServerAuth
    public void changeRoleIfNeeded(UserInfoDto oldDto, UserInfoDto newDto) throws RemoteDateException {
        if(Objects.isNull(oldDto.getEmployeeId()) && Objects.isNull(newDto.getEmployeeId())){
            return;
        }
        if(Objects.isNull(oldDto.getEmployeeId()) || Objects.isNull(newDto.getEmployeeId())){
            if(Objects.isNull(oldDto.getEmployeeId())){
                ResponseEntity<EmployeeDto> employee = remoteEmployeeService.byUuid(newDto.getEmployeeId());
                //add new role by permissions
                List<CompanyHierarchyEnum> permissions = employee.getBody().getPermissions();
                for(CompanyHierarchyEnum permission : permissions){
                    RoleDto role = getRoleByName(permission.name());
                    Role2UserDto add = new Role2UserDto();
                    add.setUser(newDto.getUserId());
                    add.setRole(role.getUuid());
                    remoteUserService.addRole(add);
                }
                //remove simple role
                RoleDto simple = getSimpleRole();
                Role2UserDto remove = new Role2UserDto();
                remove.setUser(newDto.getUserId());
                remove.setRole(simple.getUuid());
                remoteUserService.removeRole(remove);
            }
            if(Objects.isNull(newDto.getEmployeeId())){
                //add simple role
                RoleDto simple = getSimpleRole();
                Role2UserDto add = new Role2UserDto();
                add.setUser(newDto.getUserId());
                add.setRole(simple.getUuid());
                remoteUserService.addRole(add);
                //remove old role by permissions
                ResponseEntity<EmployeeDto> employee = remoteEmployeeService.byUuid(oldDto.getEmployeeId());
                List<CompanyHierarchyEnum> permissions = employee.getBody().getPermissions();
                for(CompanyHierarchyEnum permission : permissions){
                    RoleDto role = getRoleByName(permission.name());
                    Role2UserDto remove = new Role2UserDto();
                    remove.setUser(newDto.getUserId());
                    remove.setRole(role.getUuid());
                    remoteUserService.removeRole(remove);
                }
            }
            NotificationMessageDto messageDto = createMessage(newDto.getUserId());
            messageService.post(messageDto);
            userInfoDtoRepository.deleteById(newDto.getUuid());
        }
        else {
            if(!oldDto.getEmployeeId().equals(newDto.getEmployeeId())){
                ResponseEntity<EmployeeDto> employeeOld = remoteEmployeeService.byUuid(oldDto.getEmployeeId());
                ResponseEntity<EmployeeDto> employeeNew = remoteEmployeeService.byUuid(newDto.getEmployeeId());
                List<CompanyHierarchyEnum> permissionsNew = employeeNew.getBody().getPermissions();
                List<CompanyHierarchyEnum> permissionsOld = employeeOld.getBody().getPermissions();
                List<CompanyHierarchyEnum> permissionsForAdded = permissionsNew.stream()
                        .filter(item -> !permissionsOld.contains(item))
                        .toList();
                List<CompanyHierarchyEnum> permissionsForRemoved = permissionsOld.stream()
                        .filter(item -> !permissionsNew.contains(item))
                        .toList();
                //add new role by permissions
                for(CompanyHierarchyEnum permission : permissionsForAdded){
                    RoleDto role = getRoleByName(permission.name());
                    Role2UserDto add = new Role2UserDto();
                    add.setUser(newDto.getUserId());
                    add.setRole(role.getUuid());
                    remoteUserService.addRole(add);
                }
                //remove old role by permissions
                for(CompanyHierarchyEnum permission : permissionsForRemoved){
                    RoleDto role = getRoleByName(permission.name());
                    Role2UserDto remove = new Role2UserDto();
                    remove.setUser(newDto.getUserId());
                    remove.setRole(role.getUuid());
                    remoteUserService.removeRole(remove);
                }

                NotificationMessageDto messageDto = createMessage(newDto.getUserId());
                messageService.post(messageDto);
                userInfoDtoRepository.deleteById(newDto.getUuid());
            }
        }
    }

    private NotificationMessageDto createMessage(UUID userId) {
        String userName = remoteUserService.byUuid(userId).getBody().getUsername();
        NotificationMessageDto messageDto = new NotificationMessageDto();
        messageDto.setFromUser(appName);
        messageDto.setToUser(userName);
        messageDto.setSendType(SendType.TO_USER);
        messageDto.setMessageType(MessageType.LOGOUT);
        messageDto.setProject(Project.QR);
        messageDto.setMessageCode("notification.message.role-updated");
        return messageDto;
    }

    private RoleDto getSimpleRole() throws RemoteDateException {
        return getRoleByName(ROLE_SIMPLE_USER);
    }

    private RoleDto getRoleByName(String roleName) throws RemoteDateException {
        RoleDto search = new RoleDto();
        search.setName(roleName);
        Page<RoleDto> page = roleService.list(search, Pageable.ofSize(10));
        if(page.getTotalElements() == 1){
            return page.getContent().get(0);
        }
        else {
            if(page.getTotalPages() == 1){
                List<RoleDto> roles = page.getContent().
                        stream()
                        .filter(item -> item.getName().equals(roleName))
                        .toList();
                if(roles.size() == 1){
                    return roles.get(0);
                }
            }
        }
        throw new RemoteDateException(messageUtil, roleService.getClass());
    }

}
