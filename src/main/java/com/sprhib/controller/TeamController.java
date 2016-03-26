package com.sprhib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sprhib.model.Team;
import com.sprhib.service.IOrganizationService;
import com.sprhib.service.ITeamService;
import com.sprhib.validators.TeamValidator;

@Controller
@RequestMapping(value = TeamController.PATH)
public class TeamController extends BaseController implements IBaseController<Team> {
  public static final String PATH = "/team";

  public static final String VIEW_ADD = "team/add-team-form";
  public static final String VIEW_LIST = "team/list-of-teams";
  public static final String VIEW_EDIT = "team/edit-team-form";

  public static final String VIEW_MODEL_ORGANIZATIONS = "organizations";
  public static final String VIEW_MODEL_TEAM = "team";
  public static final String VIEW_MODEL_TEAMS = "teams";

  public static final String MODEL_ATTRIBUTE_ORGANIZATION = "organization";
  public static final String MODEL_ATTRIBUTE_NAME = "name";
  public static final String MODEL_ATTRIBUTE_RATING = "rating";
  public static final String MODEL_ATTRIBUTE_MEMBERS = "members";

  public static final String MESSAGE_KEY_ADD_SUCCESS = "002.001.000";
  public static final String MESSAGE_KEY_EDIT_SUCCESS = "002.002.000";
  public static final String MESSAGE_KEY_DELETE_SUCCESS = "002.003.000";

  @Autowired
  private ITeamService teamService;

  @Autowired
  private IOrganizationService organizationService;

  public ModelAndView getAddModelPage() {
    ModelAndView modelAndView = null;
    try {
      modelAndView = new ModelAndView(VIEW_ADD);
      modelAndView.addObject(VIEW_MODEL_TEAM, new Team());
      modelAndView.addObject(VIEW_MODEL_ORGANIZATIONS, organizationService.getOrganizations());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public @ResponseBody ModelAndView addModel(@ModelAttribute Team team, BindingResult result, ModelMap model) {
    Validator formValidation = new TeamValidator();
    ModelAndView modelAndView = null;

    formValidation.validate(team, result);
    if (result.hasErrors()) {
      modelAndView = new ModelAndView(VIEW_ADD);
      modelAndView.addObject(VIEW_MODEL_ORGANIZATIONS, organizationService.getOrganizations());
      return modelAndView; 
    }

    try {
      team = teamService.addTeam(team);
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      modelAndView.addObject(VIEW_MODEL_TEAM, team.getId());
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_ADD_SUCCESS));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView listModel() {
    ModelAndView modelAndView = null;
    try {
      modelAndView = new ModelAndView(VIEW_LIST);
      modelAndView.addObject(VIEW_MODEL_TEAMS, teamService.getTeams());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView getEditModelPage(@PathVariable Integer id) {
    ModelAndView modelAndView = null;
    try {
      modelAndView = new ModelAndView(VIEW_EDIT);
      modelAndView.addObject(VIEW_MODEL_TEAM, teamService.getTeam(id));
      modelAndView.addObject(VIEW_MODEL_ORGANIZATIONS, organizationService.getOrganizations());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView editModel(@ModelAttribute Team team, @PathVariable Integer id, BindingResult result, ModelMap modelMap) {
    Validator formValidation = new TeamValidator();
    ModelAndView modelAndView = null;

    formValidation.validate(team, result);
    if (result.hasErrors()) {
      modelAndView = new ModelAndView(VIEW_EDIT);
      modelAndView.addObject(VIEW_MODEL_ORGANIZATIONS, organizationService.getOrganizations());
      return modelAndView;
    }

    try {
      teamService.updateTeam(team);
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      modelAndView.addObject(VIEW_MODEL_TEAM, team.getId());
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_EDIT_SUCCESS));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView deleteModel(@PathVariable Integer id) {
    ModelAndView modelAndView = null;
    try {
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      teamService.deleteTeam(id);
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_DELETE_SUCCESS));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

}
