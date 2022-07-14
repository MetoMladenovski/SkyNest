package com.htecgroup.skynest.lambda;

import lombok.Getter;

@Getter
public enum LambdaType {
  // The code is used to represent the lambdas in the database and as such should not be changed.
  UPLOAD_FILE_TO_EXTERNAL_SERVICE_LAMBDA("U_F_T_E_S_L");

  private String databaseCode;

  LambdaType(String code) {
    this.databaseCode = code;
  }
}