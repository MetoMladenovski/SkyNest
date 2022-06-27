package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.exception.UserNotFoundException;
import com.htecgroup.skynest.exception.auth.AuthException;
import com.htecgroup.skynest.exception.register.EmailAlreadyInUseException;
import com.htecgroup.skynest.exception.register.PhoneNumberAlreadyInUseException;
import com.htecgroup.skynest.exception.role.UserNotWorkerException;
import com.htecgroup.skynest.model.dto.LoggedUserDto;
import com.htecgroup.skynest.model.dto.RoleDto;
import com.htecgroup.skynest.model.dto.UserDto;
import com.htecgroup.skynest.model.entity.RoleEntity;
import com.htecgroup.skynest.model.entity.UserEntity;
import com.htecgroup.skynest.model.request.UserEditRequest;
import com.htecgroup.skynest.model.request.UserRegisterRequest;
import com.htecgroup.skynest.model.response.UserResponse;
import com.htecgroup.skynest.repository.UserRepository;
import com.htecgroup.skynest.service.CurrentUserService;
import com.htecgroup.skynest.service.RoleService;
import com.htecgroup.skynest.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private RoleService roleService;
  @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Spy private ModelMapper modelMapper;
  @Mock private CurrentUserService currentUserService;
  @Spy @InjectMocks private UserServiceImpl userService;

  @Test
  void registerUser() {

    UserEntity expectedUserEntity = UserEntityUtil.getNotVerified();

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleService.findByName(anyString())).thenReturn(mock(RoleDto.class));
    when(userRepository.save(any())).thenReturn(expectedUserEntity);
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encryptedPassword");

    UserRegisterRequest userRegisterRequest = UserRegisterRequestUtil.get();
    UserResponse actualUserResponse = userService.registerUser(userRegisterRequest);

    this.assertUserEntityAndUserResponse(expectedUserEntity, actualUserResponse);
  }

  @Test
  void registerUser_AlreadyExistsByEmail() {

    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    String expectedErrorMessage = new EmailAlreadyInUseException().getMessage();

    UserRegisterRequest userRegisterRequest = UserRegisterRequestUtil.get();

    Exception thrownException =
        Assertions.assertThrows(
            EmailAlreadyInUseException.class, () -> userService.registerUser(userRegisterRequest));
    Assertions.assertEquals(expectedErrorMessage, thrownException.getMessage());
  }

  @Test
  void registerUser_AlreadyExistsByPhone() {

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);
    String expectedErrorMessage = new PhoneNumberAlreadyInUseException().getMessage();

    UserRegisterRequest userRegisterRequest = UserRegisterRequestUtil.get();

    Exception thrownException =
        Assertions.assertThrows(
            PhoneNumberAlreadyInUseException.class,
            () -> userService.registerUser(userRegisterRequest));
    Assertions.assertEquals(expectedErrorMessage, thrownException.getMessage());
  }

  @Test
  void findUserByEmail() {
    UserEntity userEntity = UserEntityUtil.getNotVerified();
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userEntity));

    UserDto actualUserDto = userService.findUserByEmail(userEntity.getEmail());
    this.assertUserDtoAndUserEntity(userEntity, actualUserDto);
  }

  @Test
  void getUser_IdNotFound() {
    when(userRepository.findById(any())).thenReturn(Optional.empty());
    UUID uuid = UUID.randomUUID();

    UserNotFoundException ex =
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser(uuid));

    Assertions.assertEquals(new UserNotFoundException().getMessage(), ex.getMessage());
    verify(userRepository, times(1)).findById(any());
  }

  @Test
  void authorizeUser_WorkerAccessDenied() {
    when(currentUserService.getLoggedUser()).thenReturn(LoggedUserDtoUtil.getLoggedWorkerUser());
    UUID uuid = UUID.randomUUID();

    AuthException ex =
        Assertions.assertThrows(
            AuthException.class, () -> userService.authorizeAccessToUserDetailsWith(uuid));

    Assertions.assertEquals("A worked can only view his/hers account details", ex.getMessage());
    verify(currentUserService, times(1)).getLoggedUser();
  }

  @Test
  void authorizeUser_Admin() {
    when(currentUserService.getLoggedUser()).thenReturn(LoggedUserDtoUtil.getLoggedAdminUser());
    UUID uuid = UUID.randomUUID();

    Assertions.assertDoesNotThrow(() -> userService.authorizeAccessToUserDetailsWith(uuid));
    verify(currentUserService, times(1)).getLoggedUser();
  }

  @Test
  void authorizeUser_Worker() {
    LoggedUserDto currentUser = LoggedUserDtoUtil.getLoggedWorkerUser();
    when(currentUserService.getLoggedUser()).thenReturn(currentUser);
    UUID uuid = currentUser.getUuid();

    Assertions.assertDoesNotThrow(() -> userService.authorizeAccessToUserDetailsWith(uuid));
    verify(currentUserService, times(1)).getLoggedUser();
  }

  @Test
  void getUser() {
    UserEntity userEntity = UserEntityUtil.getNotVerified();
    when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

    UserResponse actualUserResponse = userService.getUser(userEntity.getId());

    this.assertUserEntityAndUserResponse(userEntity, actualUserResponse);
    verify(userRepository, times(1)).findById(any());
  }

  @Test
  void findUserByEmail_NoSuchUser() {

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    String expectedErrorMessage = new UserNotFoundException().getMessage();
    Exception thrownException =
        Assertions.assertThrows(
            UserNotFoundException.class, () -> userService.findUserByEmail("email@email.com"));
    Assertions.assertEquals(expectedErrorMessage, thrownException.getMessage());
  }

  @Test
  void listAllUsers() {
    List<UserEntity> userEntityList = new ArrayList<>();
    userEntityList.add(UserEntityUtil.getVerified());
    when(userRepository.findAll()).thenReturn(userEntityList);

    List<UserEntity> expectedResponse = new ArrayList<>(userEntityList);

    List<UserResponse> actualResponse = userService.listAllUsers();

    Assertions.assertEquals(expectedResponse.size(), actualResponse.size());
    this.assertUserEntityAndUserResponse(expectedResponse.get(0), actualResponse.get(0));
  }

  @Test
  void deleteUser_UserDoesNotExist() {
    when(userRepository.existsById(any())).thenReturn(false);
    UUID uuid = UUID.randomUUID();
    String expectedErrorMessage = "User not found";
    Exception thrownException =
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));
    Assertions.assertEquals(expectedErrorMessage, thrownException.getMessage());
  }

  @Test
  void deleteUser_ShouldCallDeleteUser() {
    when(userRepository.existsById(any())).thenReturn(true);
    UUID uuid = UUID.randomUUID();
    userService.deleteUser(uuid);
    verify(userRepository).deleteById(uuid);
  }

  @Test
  void editUser() {
    UserEntity userEntityThatShouldBeEdited = UserEntityUtil.getVerified();
    UserEditRequest editedUser = UserEditRequestUtil.get();

    when(userRepository.findById(any())).thenReturn(Optional.of(userEntityThatShouldBeEdited));
    when(userRepository.save(any())).thenReturn(userEntityThatShouldBeEdited);
    UserResponse userResponse = userService.editUser(editedUser, UUID.randomUUID());

    this.assertUserEntityAndUserResponse(userEntityThatShouldBeEdited, userResponse);
  }

  @Test
  void when_NotWorkerUser_promoteUser_ShouldThrowUserNotWorker() {
    UserDto userDto = UserDtoUtil.getNotWorker();
    doReturn(userDto).when(userService).findUserById(any());
    String expectedErrorMessage = UserNotWorkerException.MESSAGE;
    Exception thrownException =
        Assertions.assertThrows(UserNotWorkerException.class, () -> userService.promoteUser(any()));
    Assertions.assertEquals(expectedErrorMessage, thrownException.getMessage());
  }

  @Test
  void when_WorkerUser_promoteUser_ShouldPromoteUser() {
    UserDto userDto = UserDtoUtil.getVerified();
    doReturn(userDto).when(userService).findUserById(any());
    UUID roleId = UUID.randomUUID();
    when(roleService.findByName(anyString()))
        .thenReturn(new RoleDto(roleId, RoleEntity.ROLE_MANAGER));
    userService.promoteUser(any());
    Mockito.verify(userRepository).save(any());
  }

  private void assertUserEntityAndUserResponse(
      UserEntity expectedUserEntity, UserResponse actualUserResponse) {
    Assertions.assertEquals(expectedUserEntity.getId().toString(), actualUserResponse.getId());
    Assertions.assertEquals(expectedUserEntity.getEmail(), actualUserResponse.getEmail());
    Assertions.assertEquals(expectedUserEntity.getName(), actualUserResponse.getName());
    Assertions.assertEquals(expectedUserEntity.getSurname(), actualUserResponse.getSurname());
    Assertions.assertEquals(expectedUserEntity.getAddress(), actualUserResponse.getAddress());
    Assertions.assertEquals(
        expectedUserEntity.getPhoneNumber(), actualUserResponse.getPhoneNumber());
  }

  private void assertUserDtoAndUserEntity(UserEntity expectedUserEntity, UserDto actualUserDto) {
    if (expectedUserEntity.getCompany() != null && actualUserDto.getCompany() != null) {
      Assertions.assertEquals(
          expectedUserEntity.getCompany().getId(), actualUserDto.getCompany().getId());
    }
    Assertions.assertEquals(expectedUserEntity.getId(), actualUserDto.getId());
    Assertions.assertEquals(expectedUserEntity.getCreatedOn(), actualUserDto.getCreatedOn());
    Assertions.assertEquals(expectedUserEntity.getModifiedOn(), actualUserDto.getModifiedOn());
    Assertions.assertEquals(expectedUserEntity.getDeletedOn(), actualUserDto.getDeletedOn());
    Assertions.assertEquals(expectedUserEntity.getEmail(), actualUserDto.getEmail());
    Assertions.assertEquals(
        expectedUserEntity.getEncryptedPassword(), actualUserDto.getEncryptedPassword());
    Assertions.assertEquals(expectedUserEntity.getName(), actualUserDto.getName());
    Assertions.assertEquals(expectedUserEntity.getSurname(), actualUserDto.getSurname());
    Assertions.assertEquals(expectedUserEntity.getAddress(), actualUserDto.getAddress());
    Assertions.assertEquals(expectedUserEntity.getPhoneNumber(), actualUserDto.getPhoneNumber());
    Assertions.assertEquals(expectedUserEntity.getVerified(), actualUserDto.getVerified());
    Assertions.assertEquals(expectedUserEntity.getEnabled(), actualUserDto.getEnabled());
    Assertions.assertEquals(expectedUserEntity.getRole().getId(), actualUserDto.getRole().getId());
  }
}
