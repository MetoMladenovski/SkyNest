package com.htecgroup.skynest.annotation;

import com.htecgroup.skynest.annotation.validator.CurrentUserInCompanyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Constraint(validatedBy = CurrentUserInCompanyValidator.class)
public @interface CurrentUserIsInCompany {
  String message() default "Admin and user company don't match";

  Class<?> groups()[] default {};

  Class<? extends Payload>[] payload() default {};
}
