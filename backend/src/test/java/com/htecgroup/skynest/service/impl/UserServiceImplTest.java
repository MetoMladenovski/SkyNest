package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.exception.UserException;
import com.htecgroup.skynest.model.dto.RoleDto;
import com.htecgroup.skynest.model.dto.UserDto;
import com.htecgroup.skynest.model.entity.RoleEntity;
import com.htecgroup.skynest.model.entity.UserEntity;
import com.htecgroup.skynest.model.request.UserRegisterRequest;
import com.htecgroup.skynest.repository.RoleRepository;
import com.htecgroup.skynest.repository.UserRepository;
import com.htecgroup.skynest.service.EmailService;
import com.htecgroup.skynest.service.RoleService;
import com.htecgroup.skynest.util.EmailUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private RoleService roleService;
  @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Mock private EmailUtils emailUtils;
  @Spy private ModelMapper modelMapper;

  @Spy @InjectMocks private UserServiceImpl userService;

  private UserEntity enabledWorkerEntity;
  private RoleEntity roleWorkerEntity;
  private UserRegisterRequest newUserRequest;

  @BeforeEach
  void setUp() {

    modelMapper = new ModelMapper();

    LocalDateTime currentDateTime = LocalDateTime.now();

    roleWorkerEntity = new RoleEntity(UUID.randomUUID(), RoleEntity.ROLE_WORKER);
    enabledWorkerEntity =
        new UserEntity(
            UUID.randomUUID(),
            currentDateTime,
            currentDateTime,
            null,
            "test@test.com",
            "$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW",
            "Name",
            "Surname",
            "Address",
            "381123456789",
            true,
            true,
            roleWorkerEntity,
            null);

    newUserRequest = new UserRegisterRequest();
    newUserRequest.setEmail("test@test.com");
    newUserRequest.setPassword("123456");
    newUserRequest.setName("Name");
    newUserRequest.setSurname("Surname");
    newUserRequest.setPhoneNumber("38112345689");
    newUserRequest.setAddress("Address");
  }

  @Test
  void registerUser() {

    UserEntity expectedUserEntity = enabledWorkerEntity;
    expectedUserEntity.setEnabled(false);
    expectedUserEntity.setVerified(false);
    UserDto expectedUserDto = new ModelMapper().map(expectedUserEntity, UserDto.class);

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleService.findByName(anyString())).thenReturn(modelMapper.map(roleWorkerEntity, RoleDto.class));
    when(userRepository.save(any())).thenReturn(expectedUserEntity);
    when(bCryptPasswordEncoder.encode(anyString()))
        .thenReturn(expectedUserDto.getEncryptedPassword());

    doNothing().when(userService).sendVerificationEmail(anyString());

    UserDto newUserDto = new ModelMapper().map(newUserRequest, UserDto.class);
    UserDto actualUserDto = userService.registerUser(newUserDto);

    Assertions.assertEquals(expectedUserDto, actualUserDto);
  }

  @Test
  void registerUser_AlreadyExists() {

    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    UserDto newUserDto = new ModelMapper().map(newUserRequest, UserDto.class);

    Assertions.assertThrows(UserException.class, () -> userService.registerUser(newUserDto));
  }

  @Test
  void findUserByEmail() {

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(enabledWorkerEntity));

    UserDto expectedUserDto = userService.findUserByEmail(enabledWorkerEntity.getEmail());

    Assertions.assertEquals(
        new ModelMapper().map(enabledWorkerEntity, UserDto.class), expectedUserDto);
  }

  @Test
  void findUserByEmail_NoSuchUser() {

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UsernameNotFoundException.class,
        () -> userService.findUserByEmail(enabledWorkerEntity.getEmail()));
  }

  @Test
  void confirmEmail() {
    String expectedResponse = "User verified successfully";

    UserEntity disabledWorkerEntity = enabledWorkerEntity;
    disabledWorkerEntity.setEnabled(false);
    disabledWorkerEntity.setVerified(false);
    when(emailUtils.getEmailFromJwtEmailToken(anyString()))
        .thenReturn(disabledWorkerEntity.getEmail());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(disabledWorkerEntity));
    when(userRepository.save(any())).thenReturn(enabledWorkerEntity);

    Assertions.assertEquals(expectedResponse, userService.confirmEmail(anyString()));
  }

  @Test
  void confirmEmail_UserWithEmailNotFound() {
    UserEntity disabledWorkerEntity = enabledWorkerEntity;
    disabledWorkerEntity.setEnabled(false);
    disabledWorkerEntity.setVerified(false);
    when(emailUtils.getEmailFromJwtEmailToken(anyString()))
        .thenReturn(disabledWorkerEntity.getEmail());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UsernameNotFoundException.class, () -> userService.confirmEmail(anyString()));
  }

  @Test
  void listAllUsers() {
    List<UserEntity> userEntityList = new ArrayList<>();
    userEntityList.add(enabledWorkerEntity);
    when(userRepository.findAll()).thenReturn(userEntityList);

    List<UserDto> expectedDTOs =
        userEntityList.stream()
            .map(e -> modelMapper.map(e, UserDto.class))
            .collect(Collectors.toList());

    List<UserDto> returnedDTOs = userService.listAllUsers();

    Assertions.assertEquals(expectedDTOs.size(), returnedDTOs.size());
    Assertions.assertEquals(expectedDTOs.get(0), returnedDTOs.get(0));
    Assertions.assertEquals(expectedDTOs.get(0).getEmail(), returnedDTOs.get(0).getEmail());
  }

  @Test
  void resetPassword() {
    String expectedResponse = "Password was successfully reset";

    when(emailUtils.getEmailFromJwtEmailToken(anyString()))
        .thenReturn(enabledWorkerEntity.getEmail());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(enabledWorkerEntity));
    when(userRepository.save(any())).thenReturn(enabledWorkerEntity);

    Assertions.assertEquals(expectedResponse, userService.resetPassword(anyString(), anyString()));
  }

  @Test
  void deleteUser_UserWithIdNotFound(){
    when(userRepository.existsById(any())).thenReturn(false);

    Assertions.assertThrows(UserException.class, () -> userService.deleteUser(null));
  }

  @Test
  void enableUser(){
    UserDto notEnabledUserDto = modelMapper.map(enabledWorkerEntity, UserDto.class);
    notEnabledUserDto.setEnabled(false);
    notEnabledUserDto.setVerified(false);

    UserDto verifiedUser = userService.enableUser(notEnabledUserDto);

    Assertions.assertTrue(verifiedUser.getVerified());
    Assertions.assertTrue(verifiedUser.getEnabled());
  }

  @Test
  void enableUser_UserAlreadyEnabled(){
    UserDto enabledUserDto = modelMapper.map(enabledWorkerEntity, UserDto.class);
    Assertions.assertThrows(UserException.class, () -> userService.enableUser(enabledUserDto));
  }
}
