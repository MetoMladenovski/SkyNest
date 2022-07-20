package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.model.dto.LoggedUserDto;
import com.htecgroup.skynest.model.email.Email;
import com.htecgroup.skynest.service.CurrentUserService;
import com.htecgroup.skynest.service.EmailService;
import com.htecgroup.skynest.service.InviteService;
import com.htecgroup.skynest.util.EmailUtil;
import com.htecgroup.skynest.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class InviteServiceImpl implements InviteService {

  private final CurrentUserService currentUserService;
  private final EmailService emailService;

  @Override
  public void sendRegistrationInvite(String newUserEmail) {

    LoggedUserDto loggedUserDto = currentUserService.getLoggedUser();

    String token =
        JwtUtils.generateRegistrationInviteToken(
            newUserEmail, loggedUserDto.getCompany().getName());
    Email email = EmailUtil.createRegistrationInviteEmail(loggedUserDto, newUserEmail, token);

    emailService.send(email);
    log.info(
        "A registration invite was sent to {} by user {}",
        newUserEmail,
        loggedUserDto.getUsername());
  }
}