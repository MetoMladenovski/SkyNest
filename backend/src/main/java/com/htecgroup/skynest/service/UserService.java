package com.htecgroup.skynest.service;

import com.htecgroup.skynest.model.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto registerUser(UserDto userDto);

  UserDto findUserByEmail(String email);

  List<UserDto> listAllUsers();

  String confirmEmail(String token);

  UserDto enableUser(UserDto userDto);

  void sendVerificationEmail(String email);

  void sendPasswordResetEmail(String email);

  String resetPassword(String token, String password);

  void deleteUser(UUID uuid);
}
