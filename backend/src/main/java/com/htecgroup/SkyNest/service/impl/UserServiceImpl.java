package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.exception.UserException;
import com.htecgroup.skynest.exception.UserExceptionType;
import com.htecgroup.skynest.model.dto.RoleDto;
import com.htecgroup.skynest.model.dto.UserDto;
import com.htecgroup.skynest.model.enitity.RoleEntity;
import com.htecgroup.skynest.model.enitity.UserEntity;
import com.htecgroup.skynest.repository.RoleRepository;
import com.htecgroup.skynest.repository.UserRepository;
import com.htecgroup.skynest.service.EmailService;
import com.htecgroup.skynest.service.UserService;
import com.htecgroup.skynest.util.JwtEmailVerificationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private JwtEmailVerificationUtils jwtEmailVerificationUtils;
  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired private ModelMapper modelMapper;
  @Autowired private EmailService emailService;

  @Autowired private RoleRepository roleRepository;

  @Override
  public UserDto registerUser(UserDto userDto) {

    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new UserException(UserExceptionType.EMAIL_ALREADY_IN_USE);
    }

    RoleEntity roleEntity =
            roleRepository
                    .findByName(RoleEntity.ROLE_WORKER)
                    .orElseThrow(
                            () ->
                                    new UserException(
                                            "Role " + RoleEntity.ROLE_WORKER + " not found.",
                                            HttpStatus.INTERNAL_SERVER_ERROR));
    userDto.setRole(modelMapper.map(roleEntity, RoleDto.class));

    userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
    userDto.setVerified(false);
    userDto.setEnabled(false);

    UserEntity userEntity = userRepository.save(modelMapper.map(userDto, UserEntity.class));

    String token = jwtEmailVerificationUtils.generateJwtEmailVerificationToken(userDto);

    String confirmationLink = jwtEmailVerificationUtils.getConfirmationLink() + token;
    emailService.send(
        userEntity.getEmail(),
        jwtEmailVerificationUtils.buildEmail(userEntity.getName(), confirmationLink));

    return modelMapper.map(userEntity, UserDto.class);
  }

  @Override
  public UserDto findUserByEmail(String email) {
    UserEntity userEntity =
            userRepository
                    .findUserByEmail(email)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("could not find user with email: " + email));

    return modelMapper.map(userEntity, UserDto.class);
  }

  @Override
  public Boolean confirmEmail(String token) {
    boolean validateToken = jwtEmailVerificationUtils.validateJwtToken(token);
    if (validateToken) {
      String email = jwtEmailVerificationUtils.getEmailFromJwtEmailVerificationToken(token);
      return this.enableUser(email);
    } else throw new UserException(UserExceptionType.EMAIL_VERIFICATION_TOKEN_FAILED);
  }

  public Boolean enableUser(String email) {
    // TODO: possibly in different service
    return true;
  }
}
