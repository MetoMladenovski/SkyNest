package com.htecgroup.skynest.exception.lambda;

import org.springframework.http.HttpStatus;

public class DropboxInvalidAccessTokenException extends LambdaException {

  private static final String MESSAGE = "Invalid dropbox access token, connect to dropbox again.";

  public DropboxInvalidAccessTokenException() {
    super(MESSAGE, HttpStatus.UNAUTHORIZED);
  }
}
