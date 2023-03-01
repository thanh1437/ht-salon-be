package com.salon.ht.service;

import com.salon.ht.exception.ResourceAlreadyInUseException;
import com.salon.ht.exception.ResourceNotFoundException;
import com.salon.ht.exception.RoleException;
import com.salon.ht.repository.RoleRepository;
import com.salon.ht.dto.RoleDto;
import com.salon.ht.dto.UserRoleDto;
import com.salon.ht.entity.Role;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.RoleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService extends AbstractService<Role, Long> {
    private final RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<RoleDto> getAllRoles() {
        List<Role> roles = this.findAll();
        return roles.stream().map(role -> new RoleDto(role.getId(), role.getName(), role.getDetail(), role.getDescription(), role.isAdminRole(), convertToUserRole(role.getUsers())))
                .collect(Collectors.toList());
    }

    public RoleDto getRoleDetail(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            return convertToDto(role);
        } else {
            throw new ResourceNotFoundException("Quyền", "ID", id);
        }
    }

    public void deleteRole(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            role.getUsers().forEach(u -> u.getRoles().remove(role));
            roleRepository.saveAndFlush(role);
            roleRepository.delete(role);
        } else {
            throw new ResourceNotFoundException("Quyền", "ID", id);
        }
    }

    public void createRole(RoleRequest roleRequest) {
        try {
            Optional<Role> roleOptional = roleRepository.findByName(roleRequest.getName());
            if (roleOptional.isPresent()) {
                throw new ResourceAlreadyInUseException("Quyền", "Tên", roleRequest.getName());
            }

            Role role = new Role();
            role.setName(roleRequest.getName());
            role.setDetail(roleRequest.getDetail());

            Set<UserEntity> users = new HashSet<>();
            List<Long> userIds = roleRequest.getUserIds();
            userIds.forEach(userId -> {
                UserEntity user = userService.getUserById(userId);
                user.setRoles(new HashSet<>(Arrays.asList(role)));
                users.add(user);
            });
            role.setUsers(users);
            this.save(role);
        } catch (Exception e) {
            LOGGER.info("Xảy ra lỗi khi thêm mới quyền {}", e.getMessage());
            throw new RoleException("Xảy ra lỗi khi thêm mới quyền: " + e.getMessage());
        }
    }

    public void updateRole (RoleRequest roleRequest) {
        try {
            Optional<Role> roleOptional = roleRepository.findById(roleRequest.getId());
            if (!roleOptional.isPresent()) {
                throw new ResourceNotFoundException("Quyền", "Id", roleRequest.getId());
            }

            Role role = roleOptional.get();
            role.setName(roleRequest.getName());
            role.setDetail(roleRequest.getDetail());
            role.setDescription(roleRequest.getDescription());
            List<Long> userIdsNew = roleRequest.getUserIds();
            List<UserEntity> usersRemove = role.getUsers().stream().filter(r -> !userIdsNew.contains(r.getId())).collect(Collectors.toList());
            usersRemove.forEach(u -> u.getRoles().remove(role));

            userIdsNew.forEach(userId -> {
                Optional<UserEntity> userEntityOptional = userService.getRepository().findById(userId);
                if (userEntityOptional.isPresent()) {
                    UserEntity user = userEntityOptional.get();
                    user.setRoles(new HashSet<>(Arrays.asList(role)));
                    userService.save(user);
                }
            });

            roleRepository.saveAndFlush(role);

        } catch (Exception e) {
            LOGGER.error("Xảy ra lỗi khi cập nhật quyền do " + e.getMessage());
            throw new RoleException("Xảy ra lỗi khi cập nhật quyền!");
        }

    }

    public RoleDto convertToDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setAdminRole(role.isAdminRole());
        dto.setDetail(role.getDetail());
        dto.setDescription(role.getDescription());
        dto.setName(role.getName());
        dto.setId(role.getId());
        dto.setUsers(convertToUserRole(role.getUsers()));
        return dto;
    }

    private List<UserRoleDto> convertToUserRole(Set<UserEntity> users) {
        List<UserRoleDto> userRoleDtos = new ArrayList<>();
        users.forEach(user -> {
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setId(user.getId());
            userRoleDto.setUsername(user.getUsername());
            userRoleDto.setName(user.getName());
            userRoleDto.setEmail(user.getEmail());
            userRoleDto.setMobile(user.getMobile());
            userRoleDto.setPhoto(user.getPhoto());
            userRoleDtos.add(userRoleDto);
        });

        return userRoleDtos;
    }

    @Override
    protected JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);


}
