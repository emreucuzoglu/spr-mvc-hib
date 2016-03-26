package com.sprhib.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sprhib.controller.OrganizationController;
import com.sprhib.model.Organization;
import com.sprhib.utilities.Constants;

public class OrganizationValidator implements Validator {

  private Pattern pattern;
  private Matcher matcher;

  @Override
  public boolean supports(Class<?> arg0) {
    return Organization.class.equals(arg0);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Organization organization = (Organization) target;

    ValidationUtils.rejectIfEmpty(errors, OrganizationController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_REQUIRED);

    if (organization.getName() != null && !organization.getName().isEmpty()) {
      pattern = Pattern.compile(Constants.PATTERN_NAME);
      matcher = pattern.matcher(organization.getName());
      if (!matcher.matches()) {
        errors.rejectValue(OrganizationController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_NOT_VALID);
      }
    } else {
      errors.rejectValue(OrganizationController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_NOT_VALID);
    }

  }
}