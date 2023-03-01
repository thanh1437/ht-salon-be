package com.salon.ht.service;

import com.salon.ht.constant.RoleName;
import com.salon.ht.constant.UserStatus;
import com.salon.ht.entity.Department;
import com.salon.ht.entity.Role;
import com.salon.ht.entity.UserDepartment;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.RegistrationRequest;
import com.salon.ht.entity.payload.RegistrationUserRequest;
import com.salon.ht.entity.payload.UserDeptRegistrationRequest;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.exception.ImportUserException;
import com.salon.ht.exception.ResourceAlreadyInUseException;
import com.salon.ht.exception.ResourceNotFoundException;
import com.salon.ht.exception.UploadFileException;
import com.salon.ht.exception.UserRegistrationException;
import com.salon.ht.mapper.csv.UserCSV;
import com.salon.ht.repository.UserRepository;
import com.salon.ht.repository.basic.UserRepositoryBasicImpl;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.dto.PageDto;
import com.salon.ht.dto.RoleDto;
import com.salon.ht.dto.UserDeptDto;
import com.salon.ht.dto.UserDto;
import com.salon.ht.util.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepositoryBasicImpl userRepositoryBasicImpl;
    @Autowired
    private ExcelService excelService;

    private String[] columns = {"Tên tài khoản", "Họ và tên", "Email", "Điện thoại", "Ảnh", "Phòng ban", "Mật khẩu", "Chức danh", "Chức vụ"};

    private final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@vnpt.vn");
    private final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]+$");
    private final Pattern PHONE_PATTERN = Pattern.compile("(84|0[1|2|3|5|7|8|9])+([0-9]{8})\\b");

    @Value("${app.resource.location.local}")
    private String rootPath;
    @Value("${app.photo.location}")
    private String relativePath;
    @Value("${app.photo.default.name}")
    private String defaultPhotoName;
    @Value("${app.password.default}")
    private String defaultPassword;
    @Value("${app.email.admin}")
    private String adminEmail;
    @Value("${app.username.admin}")
    private String adminUsername;

    /*@PostConstruct
    public void init() {
        Optional<Role> adminRole = roleService.findByName(RoleName.ROLE_ADMIN.name());
        Role roleAdmin = new Role();
        if (adminRole.isPresent()) {
            roleAdmin = adminRole.get();
        } else {
            roleAdmin.setName(RoleName.ROLE_ADMIN.toString());
            roleAdmin.setDetail("Quản trị");
            roleService.save(roleAdmin);
        }

        Optional<Role> userRole = roleService.findByName(RoleName.ROLE_USER.name());
        Role roleUser = new Role();
        if (userRole.isPresent()) {
            roleUser = userRole.get();
        } else {
            roleUser.setName(RoleName.ROLE_USER.toString());
            roleUser.setDetail("Người dùng");
            roleService.save(roleUser);
        }

        UserEntity user = new UserEntity();
        user.setName("Quản trị viên");
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setEmail(adminEmail);
        user.setCreatedDate(new Date());
        user.setUsername(adminUsername);
        user.setPhoto(relativePath + defaultPhotoName);
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));

        userRepository.save(user);
    }*/

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserEntity buildUserEntity(RegistrationRequest request) {
        UserEntity newUser = new UserEntity();
        boolean isNewUserAdmin = request.getRegisterAsAdmin();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setMobile(request.getMobile());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(getRolesForNewUser(isNewUserAdmin));
        newUser.setStatus(UserStatus.ACTIVE);
        return newUser;
    }

    public UserDto getUser(Long userId) {
        return userRepository.findById(userId).map(this::setUserDto).orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
    }

    public void createUser(RegistrationUserRequest userRequest, MultipartFile photo) {
        try {
            // Create User entity
            if (this.existsByEmail(userRequest.getEmail())) {
                throw new ResourceAlreadyInUseException("Email", "địa chỉ", userRequest.getEmail());
            }

            if (this.existsByUsername(userRequest.getUsername())) {
                throw new ResourceAlreadyInUseException("Tài khoản người dùng", "tên tài khoản", userRequest.getEmail());
            }

            UserEntity user = new UserEntity();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setName(userRequest.getName());
            user.setMobile(userRequest.getMobile());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setStatus(UserStatus.ACTIVE);
            Long roleId = userRequest.getRoleId();
            Role role = roleService.get(roleId);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            List<UserDeptRegistrationRequest> userDepts = userRequest.getUserDepts();
            // save user
            this.save(user);
            Set<UserDepartment> userDepartments = new HashSet<>();
            userDepts.forEach(userDeptRegistrationRequest -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setUser(user);
                Department department = departmentService.get(userDeptRegistrationRequest.getId());
                userDepartment.setDepartment(department);
                userDepartment.setIsAdmin(userDeptRegistrationRequest.getIsAdmin());
                userDepartment.setIsRoot(userDeptRegistrationRequest.getIsRoot());
                userDepartment.setJobTile(userDeptRegistrationRequest.getPosition());
                userDepartment.setPosition(userDeptRegistrationRequest.getPosition());
                userDepartments.add(userDepartment);
            });

            // save user department
            userDepartments.forEach(userDepartmentService::save);

            // save photo to server and save path to db
            uploadPhoto(photo, user);
        } catch (Exception e) {
            throw new UserRegistrationException(userRequest.getUsername(), e.getMessage());
        }
    }

    public void deleteUser(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Nhân viên", "ID", String.valueOf(userId));
        }
        userRepository.deleteById(userId);
    }

    public void updateStatus(Long userId, Map<String, Object> request) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("Nhân viên", "UserId", userId.toString());
        }
        LOGGER.info("new status {}", request.get("status"));
        double doubleStatus = Double.parseDouble(request.get("status").toString());
        Integer newStatus = (int) doubleStatus;
        UserStatus userStatus = Arrays.stream(UserStatus.values()).filter(status -> status.getValue().equals(newStatus)).findFirst().orElse(null);
        if (userStatus == null) {
            throw new BadRequestException("Trạng thái truyền lên không hợp lệ");
        }
        userRepository.updateStatus(userId, newStatus);
    }

    public void updateUser(RegistrationUserRequest request, MultipartFile file) {
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findById(request.getId());
            if (userEntityOptional.isPresent()) {
                UserEntity user = userEntityOptional.get();
                user.setUsername(request.getUsername());
                user.setName(request.getName());
                user.setMobile(request.getMobile());
                user.setEmail(request.getEmail());
                user.setModifiedDate(new Date());
                Long roleId = request.getRoleId();
                UserDeptRegistrationRequest deptRegistrationRequest = request.getUserDepts().get(0);
                // check neu role id o request khac role id hien tai cua user thi update con ko thi them moi
                if (user.getRoles().size() > 0) {
                    Role oldRole = user.getRoles().iterator().next();
                    Long oldRoleId = oldRole.getId();
                    if (!oldRoleId.equals(roleId)) {
                        Role role = roleService.get(roleId);
                        Set<Role> roles = new HashSet<>();
                        roles.add(role);
                        user.setRoles(roles);
                    }
                } else {
                    Role role = roleService.get(roleId);
                    Set<Role> roles = new HashSet<>();
                    roles.add(role);
                    user.setRoles(roles);
                }
                userRepository.save(user);
                uploadPhoto(file, user);
            } else {
                throw new ResourceNotFoundException("Nhân viên", "ID", request.getId());
            }
        } catch (Exception e) {
            throw new UserRegistrationException(request.getName(), e.getMessage());
        }
    }

    public UserEntity getUserById(Long id) {
        return userRepository.getById(id);
    }

    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        LOGGER.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    public PageDto<UserDto> getList(String name, Integer status, String mobile, String email, Long departmentId, String fromDate, String toDate, Integer page, Integer limit) {
        PageRequest pageRequest;
        if (page == null || limit == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "modified_date");
        } else {
            pageRequest = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "modified_date");
        }
        Page<UserEntity> userEntities = userRepositoryBasicImpl.getUsers(name, status, mobile, email, departmentId, fromDate, toDate, pageRequest);
        return setPageDto(userEntities);
    }

    private PageDto<UserDto> setPageDto(Page<UserEntity> userPages) {
        List<UserEntity> userEntities = userPages.getContent();
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity user : userEntities) {
            UserDto userDto = setUserDto(user);
            userDtos.add(userDto);
        }
        return new PageDto<>(userPages, userDtos);
    }

    public List<UserEntity> getByIds(List<Long> userIds) {
        return userRepository.getByIds(userIds);
    }

    public UserDto setUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());
        userDto.setMobile(userEntity.getMobile());
        userDto.setStatus(userEntity.getStatus());
        userDto.setCreatedDate(userEntity.getCreatedDate());
        userDto.setModifiedDate(userEntity.getModifiedDate());
        userDto.setName(userEntity.getName());
        if (userEntity.getPhoto() == null) {
            userDto.setPhoto(relativePath + defaultPhotoName);
        } else {
            userDto.setPhoto(userEntity.getPhoto());
        }
        Set<Role> userRoles = userEntity.getRoles();
        List<RoleDto> roleDtos = new ArrayList<>();
        userRoles.forEach(role -> {
            RoleDto roleDto = roleService.convertToDto(role);
            roleDtos.add(roleDto);
        });
        userDto.setRoles(roleDtos);
        return userDto;
    }

    public UserDeptDto convertUserDeptDto(UserDepartment userDepartment) {
        UserDeptDto userDeptDto = new UserDeptDto();
        userDeptDto.setId(userDepartment.getId());
        userDeptDto.setDepartmentId(userDepartment.getDepartment().getId());
        userDeptDto.setDepartmentName(userDepartment.getDepartment().getName());
        userDeptDto.setJobTitle(userDepartment.getJobTile());
        userDeptDto.setPosition(userDepartment.getPosition());
        userDeptDto.setAdmin(userDepartment.getIsAdmin());
        userDeptDto.setRoot(userDepartment.getIsRoot());
        return userDeptDto;
    }

    public void changePassword(UserDetailsImpl currentUser, String oldPassword, String newPassword, String confirmNewPassword) {
        if (!confirmNewPassword.equals(newPassword)) {
            throw new BadRequestException("Xác nhận mật khẩu mới không khớp. Vui lòng thử lại!");
        }
        Optional<Authentication> authentication = Optional.ofNullable(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(currentUser.getUsername(), oldPassword)));

        if (authentication.isEmpty()) {
            throw new BadRequestException("Mật khẩu cũ không đúng. Vui lòng nhập lại!");
        } else {
            Optional<UserEntity> userOptional = userRepository.findByUsername(currentUser.getUsername());
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            } else {
                throw new ResourceNotFoundException("Nhân viên", "Tên tài khoản", String.valueOf(currentUser.getUsername()));
            }
        }
    }

    public void resetPassword(Long userId, String newPassword, String confirmNewPassword) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("Nhân viên", "ID", userId.toString());
        }

        if (!confirmNewPassword.equals(newPassword)) {
            throw new BadRequestException("Xác nhận mật khẩu không khớp. Vui lòng nhập lại!");
        }

        UserEntity user = userEntity.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeUserPhoto(UserDetailsImpl currentUser, MultipartFile photo) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(currentUser.getId());
        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            uploadPhoto(photo, user);
        } else {
            throw new ResourceNotFoundException("Nhân viên", "ID", currentUser.getId());
        }
    }

    public void uploadPhoto(MultipartFile file, UserEntity user) {
        String photoPathDB = "";
        String imgName = "";
        boolean isDelete = true;
        try {
            if (file != null) {
                String requestFileName = file.getOriginalFilename();
                String absolutePath = rootPath;
                File imgFolder = new File(absolutePath);
                if (requestFileName != null) {
                    imgName += user.getUsername() + requestFileName.substring(requestFileName.lastIndexOf("."));
                } else {
                    throw new BadRequestException("Ảnh nhân viên không đúng định dạng");
                }
                photoPathDB += relativePath + "/" + imgName;

                if (user.getPhoto() != null && !user.getPhoto().equals(photoPathDB) && !user.getPhoto().equals(relativePath + defaultPhotoName)) {
                    String oldImgName = user.getPhoto().substring(user.getPhoto().lastIndexOf("/"));
                    File oldPhoto = new File(rootPath + oldImgName);
                    isDelete = oldPhoto.delete();
                }

                // check when delete photo successfully then save new photo
                if (isDelete) {
                    if (!imgFolder.exists()) {
                        imgFolder.mkdirs();
                    }
                    file.transferTo(new File(imgFolder, imgName));
                    user.setPhoto(photoPathDB);
                    user.setModifiedDate(new Date());
                    userRepository.save(user);
                } else {
                    throw new UploadFileException("Xoá ảnh cũ thất bại!");
                }
            } else {
                // check trong trường hợp thêm mới user và ko truyền image lên
                if (user.getPhoto() == null) {
                    String photo = this.relativePath + this.defaultPhotoName;
                    user.setPhoto(photo);
                    user.setModifiedDate(new Date());
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            // check trong trường hợp thêm mới user và ko truyền image lên
            String photo = this.relativePath + this.defaultPhotoName;
            user.setPhoto(photo);
            user.setModifiedDate(new Date());
            userRepository.save(user);
            LOGGER.error("Xảy ra lỗi khi lưu ảnh nhân viên {}", e.getMessage());
            throw new UploadFileException("Xảy ra lỗi khi lưu ảnh của nhân viên: " + e.getMessage());
        }

    }

    public String importUserByCSV(MultipartFile file) {
        if (!excelService.hasExcelFormat(file)) {
            throw new ImportUserException("Tệp tải lên phải có định dạng Excel!");
        }

        try {
            List<UserCSV> usersCSVs = excelService.readData(file.getInputStream(), Arrays.asList(columns), "DATA");
            return this.saveDataImport(usersCSVs);
        } catch (Exception e) {
            LOGGER.error("Lỗi nhập dữ liệu từ tệp CSV do " + e.getMessage());
            throw new UploadFileException("Lỗi đọc dữ liệu từ tệp CSV do " + e.getMessage());
        }
    }

    private String saveDataImport(List<UserCSV> userCSVs) {
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);
        AtomicInteger duplicate = new AtomicInteger(0);
        StringBuilder message = new StringBuilder();

        message.append("Import thành công %d, bị trùng %d, bị lỗi %d.\n");

        userCSVs.forEach(userCSV -> {
            StringBuilder validateError = validateUserCSV(userCSV, fail);
            if (!validateError.toString().equals("")) {
                message.append(validateError);
                return;
            }

            if (this.existsByEmail(userCSV.getEmail())) {
                LOGGER.error("Địa chỉ email " + userCSV.getEmail() + " đã tồn tại trong hệ thống.");
                duplicate.getAndIncrement();
                message.append("Địa chỉ email ").append(userCSV.getEmail()).append(" đã tồn tại trong hệ thống.\n");
                return;
            }

            if (this.existsByUsername(userCSV.getUsername())) {
                LOGGER.error("Tên tài khoản " + userCSV.getUsername() + " đã tồn tại trong hệ thống.");
                message.append("Tên tài khoản ").append(userCSV.getUsername()).append(" đã tồn tại trong hệ thống.\n");
                duplicate.getAndIncrement();
                return;
            }

            UserEntity user = new UserEntity();
            user.setUsername(userCSV.getUsername());
            user.setName(userCSV.getName());
            user.setMobile(userCSV.getMobile());
            user.setEmail(userCSV.getEmail());
            Role role = roleService.findAll().stream().filter(r -> Objects.equals(r.getName(), RoleName.ROLE_USER.name())).collect(Collectors.toList()).get(0);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            user.setPhoto(userCSV.getPhoto());
            user.setPassword(passwordEncoder.encode(userCSV.getPassword()));
            user.setStatus(UserStatus.ACTIVE);
            this.save(user);
            Set<UserDepartment> userDepartments = new HashSet<>();
            UserDepartment userDepartment = new UserDepartment();
            userDepartment.setUser(user);
            Department department = departmentService.findByPath(userCSV.getDepartmentPath());
            userDepartment.setDepartment(department);
            userDepartment.setJobTile(userCSV.getJobTile());
            userDepartment.setPosition(userCSV.getPosition());
            userDepartment.setIsRoot(true);
            userDepartment.setIsAdmin(true);
            userDepartments.add(userDepartment);

            // save user department
            userDepartments.forEach(userDepartmentService::save);
            success.getAndIncrement();
        });
        return String.format(message.toString(), success.get(), duplicate.get(), fail.get());
    }

    private StringBuilder validateUserCSV(UserCSV userCSV, AtomicInteger fail) {
        StringBuilder errorMsg = new StringBuilder("");

        Matcher emailMatcher = EMAIL_PATTERN.matcher(userCSV.getEmail());
        Matcher nameMatcher = NAME_PATTERN.matcher(userCSV.getName());
        Matcher phoneNumMatcher = PHONE_PATTERN.matcher(userCSV.getMobile());

        if (!emailMatcher.find()) {
            LOGGER.error("Địa chỉ email " + userCSV.getEmail() + " không đúng định dạng!");
            fail.getAndIncrement();
            errorMsg.append("Địa chỉ email ").append(userCSV.getEmail()).append(" không đúng định dạng.\n");
        } else if (!nameMatcher.find()) {
            LOGGER.error("Họ và tên " + userCSV.getName() + " không đúng định dạng!");
            fail.getAndIncrement();
            errorMsg.append("Họ và tên ").append(userCSV.getName()).append(" không đúng định dạng.\n");
        } else if (!phoneNumMatcher.find()) {
            LOGGER.error("Số điện thoại " + userCSV.getMobile() + " không đúng định dạng!");
            fail.getAndIncrement();
            errorMsg.append("Số điện thoại ").append(userCSV.getMobile()).append(" không đúng định dạng.\n");
        }
        return errorMsg;
    }

    @Override
    protected JpaRepository<UserEntity, Long> getRepository() {
        return userRepository;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
}
