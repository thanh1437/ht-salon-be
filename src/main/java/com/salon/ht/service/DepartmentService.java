package com.salon.ht.service;

import com.salon.ht.exception.DepartmentException;
import com.salon.ht.exception.ResourceAlreadyInUseException;
import com.salon.ht.exception.ResourceNotFoundException;
import com.salon.ht.repository.DepartmentRepository;
import com.salon.ht.dto.DepartmentDto;
import com.salon.ht.dto.DeptUserDto;
import com.salon.ht.entity.Department;
import com.salon.ht.entity.UserDepartment;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.DepartmentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentService extends AbstractService<Department, Long> {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDepartmentService userDepartmentService;


    @Override
    protected JpaRepository<Department, Long> getRepository() {
        return departmentRepository;
    }

    public List<DepartmentDto> getList(String name, boolean status) {
        List<Department> departments = departmentRepository.getList(name, status);
        return departments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createDepartment(DepartmentRequest departmentRequest) {
        try {
            String deptName = departmentRequest.getName();
            if (departmentRepository.existsByName(deptName)) {
                LOGGER.error("Department name exists: " + deptName);
                throw new ResourceAlreadyInUseException("Phòng ban", "Tên phòng ban", deptName);
            }

            Department department = new Department();
            department.setName(departmentRequest.getName());
            department.setShortName(departmentRequest.getShortName());
            department.setStatus(true);
            departmentRepository.save(department);

            Department parent = departmentRepository.getById(departmentRequest.getParentId());

            String departmentPath = parent.getDepartmentPath() + "/" + deptName;
            String deptExtId = parent.getDepartmentExtId() + "," + department.getId();
            department.setDepartmentPath(departmentPath);
            department.setDepartmentExtId(deptExtId);
            department.setParentId(departmentRequest.getParentId());
            departmentRepository.save(department);

            Set<UserDepartment> userDepartments = new HashSet<>();

            departmentRequest.getUsers().forEach(deptUserDto -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setDepartment(department);
                UserEntity userEntity = userService.getUserById(deptUserDto.getUserId());
                userDepartment.setUser(userEntity);
                userDepartment.setJobTile(deptUserDto.getJobTitle());
                userDepartment.setPosition(deptUserDto.getPosition());
                userDepartment.setIsRoot(deptUserDto.isRoot());
                userDepartment.setIsAdmin(deptUserDto.isAdmin());
                userDepartments.add(userDepartment);
            });

            // save user department
            userDepartments.forEach(userDepartmentService::save);
        } catch (Exception e) {
            LOGGER.info("Xảy ra lỗi khi thêm mới phòng ban {}", e.getMessage());
            throw new DepartmentException("Xảy ra lỗi khi thêm mới phòng ban: " + e.getMessage());
        }

    }

    public void updateDepartment(DepartmentRequest departmentRequest) {
        try {
            Long deptId = departmentRequest.getId();
            Long parentId = departmentRequest.getParentId();
            Department department = departmentRepository.findById(deptId).orElse(null);

            if (department == null) {
                throw new ResourceNotFoundException("Phòng ban", "Id", deptId.toString());
            }

            department.setName(departmentRequest.getName());
            department.setShortName(departmentRequest.getShortName());
            department.setStatus(departmentRequest.getStatus());

            //check update parent id
            Long oldParentId = department.getParentId();
            if (!oldParentId.equals(parentId)) {
                Department parent = departmentRepository.getById(parentId);
                String parentPath = parent.getDepartmentPath();
                String extParent = parent.getDepartmentExtId();
                String path = parentPath + "/" + departmentRequest.getName();
                String extId = extParent + "," + departmentRequest.getId();
                department.setParentId(departmentRequest.getParentId());
                department.setDepartmentPath(path);
                department.setDepartmentExtId(extId);
            }
            // Save
            departmentRepository.save(department);
            // save user departement
            List<DeptUserDto> users = departmentRequest.getUsers();
            Set<UserDepartment> userDepartments = department.getDepartmentUsers();
            List<Long> idsOld = userDepartments.stream().map(UserDepartment::getId).collect(Collectors.toList());

            // filter delete department
            List<Long> idsNew = users.stream().map(DeptUserDto::getId).collect(Collectors.toList());
            List<Long> removeIds = idsOld.stream().filter(item -> !idsNew.contains(item)).collect(Collectors.toList());
            users.forEach(deptUserDto -> {
                Long id = deptUserDto.getId();
                Long userId = deptUserDto.getUserId();
                // Them moi
                if (id == 0) {
                    UserDepartment userDepartment = new UserDepartment();
                    userDepartment.setDepartment(department);
                    UserEntity userEntity = userService.getUserById(userId);
                    userDepartment.setUser(userEntity);
                    userDepartment.setIsAdmin(deptUserDto.isAdmin());
                    userDepartment.setIsRoot(deptUserDto.isRoot());
                    userDepartment.setPosition(deptUserDto.getPosition());
                    userDepartment.setJobTile(deptUserDto.getJobTitle());
                    userDepartmentService.save(userDepartment);
                } else {
                    UserDepartment userDepartment = userDepartmentService.get(id);
                    if (userDepartment != null) {
                        Long oldUserId = userDepartment.getUser().getId();
                        if (!oldUserId.equals(userId)) {
                            UserEntity userEntity = userService.getUserById(userId);
                            userDepartment.setUser(userEntity);
                        }

                        userDepartment.setPosition(deptUserDto.getPosition());
                        userDepartment.setJobTile(deptUserDto.getJobTitle());
                        userDepartment.setIsRoot(deptUserDto.isRoot());
                        userDepartment.setIsAdmin(deptUserDto.isAdmin());
                        userDepartmentService.save(userDepartment);
                    }
                }
            });
            removeIds.forEach(userDepartmentService::delete);
        } catch (Exception e) {
            LOGGER.error("Xảy ra lỗi khi cập nhật thông tin phòng ban {}", e.getMessage());
            throw new DepartmentException("Xảy ra lỗi khi cập nhật phòng ban: " + e.getMessage());
        }
    }

    public void deleteDepartment(Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            throw new ResourceNotFoundException("Phòng ban", "Phòng ban Id", departmentId.toString());
        }

        if (isParentDepartment(departmentId)) {
            throw new DepartmentException("Không thể xóa phòng ban này do có chứa các phòng ban con khác!");
        }

        departmentRepository.deleteById(departmentId);
    }

    public DepartmentDto convertToDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentPath(department.getDepartmentPath());
        departmentDto.setDepartmentExtId(department.getDepartmentExtId());
        departmentDto.setCreatedDate(department.getCreatedDate());
        departmentDto.setId(department.getId());
        departmentDto.setName(department.getName());
        departmentDto.setStatus(department.isStatus());
        departmentDto.setModifiedDate(department.getModifiedDate());
        departmentDto.setShortName(department.getShortName());
        departmentDto.setParentId(department.getParentId());
        Set<UserDepartment> userDepartments = department.getDepartmentUsers();
        Set<DeptUserDto> userDtos = new HashSet<>();
        if (userDepartments.size() > 0) {
            userDepartments.forEach(userDepartment -> {
                userDtos.add(this.convertToDeptUserDto(userDepartment));
            });
            departmentDto.setUsers(userDtos);
        }
        return departmentDto;
    }

    public DeptUserDto convertToDeptUserDto(UserDepartment userDepartment) {
        DeptUserDto deptUserDto = new DeptUserDto();
        deptUserDto.setId(userDepartment.getId());
        deptUserDto.setUserId(userDepartment.getUser().getId());
        deptUserDto.setName(userDepartment.getUser().getName());
        deptUserDto.setAdmin(userDepartment.getIsAdmin());
        deptUserDto.setRoot(userDepartment.getIsRoot());
        deptUserDto.setPosition(userDepartment.getPosition());
        deptUserDto.setJobTitle(userDepartment.getJobTile());
        return deptUserDto;
    }

    public Department findByPath(String departmentPath) {
        Optional<Department> departmentOptional = departmentRepository.findByDepartmentPath(departmentPath);
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        } else {
            throw new ResourceNotFoundException("Phòng ban", "Path", departmentPath);
        }
    }

    private Boolean isParentDepartment(Long id) {
        List<Department> childDepartments = departmentRepository.isParentDepartment(id);
        return childDepartments.size() > 0;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
}
