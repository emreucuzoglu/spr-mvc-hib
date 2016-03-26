package com.sprhib.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sprhib.controller.MemberController;
import com.sprhib.model.Member;
import com.sprhib.utilities.Constants;

public class MemberValidator implements Validator {

  private Pattern pattern;
  private Matcher matcher;

  @Override
  public boolean supports(Class<?> arg0) {
    return Member.class.equals(arg0);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Member member = (Member) target;

    ValidationUtils.rejectIfEmpty(errors, MemberController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_REQUIRED);

    if (!(member.getName() != null && member.getName().isEmpty())) {
      pattern = Pattern.compile(Constants.PATTERN_NAME);
      matcher = pattern.matcher(member.getName());
      if (!matcher.matches()) {
        errors.rejectValue(MemberController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_NOT_VALID);
      }
    }

  }
}