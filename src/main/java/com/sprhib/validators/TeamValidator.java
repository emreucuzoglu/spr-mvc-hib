package com.sprhib.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sprhib.controller.TeamController;
import com.sprhib.model.Team;
import com.sprhib.utilities.Constants;

public class TeamValidator implements Validator {

  private Pattern pattern;
  private Matcher matcher;

  @Override
  public boolean supports(Class<?> arg0) {
    return Team.class.equals(arg0);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Team team = (Team) target;

    ValidationUtils.rejectIfEmpty(errors, TeamController.MODEL_ATTRIBUTE_ORGANIZATION, Constants.MESSAGE_KEY_ORGANIZATION_REQUIRED);

    ValidationUtils.rejectIfEmpty(errors, TeamController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_REQUIRED);

    if (!(team.getName() != null && team.getName().isEmpty())) {
      pattern = Pattern.compile(Constants.PATTERN_NAME);
      matcher = pattern.matcher(team.getName());
      if (!matcher.matches()) {
        errors.rejectValue(TeamController.MODEL_ATTRIBUTE_NAME, Constants.MESSAGE_KEY_NAME_NOT_VALID);
      }
    }

    ValidationUtils.rejectIfEmpty(errors, TeamController.MODEL_ATTRIBUTE_RATING, Constants.MESSAGE_KEY_RATING_REQUIRED);

  }
}