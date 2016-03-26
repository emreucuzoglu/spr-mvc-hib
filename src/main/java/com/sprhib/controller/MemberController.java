package com.sprhib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sprhib.model.Member;
import com.sprhib.service.IMemberService;
import com.sprhib.service.ITeamService;
import com.sprhib.validators.MemberValidator;

@Controller
@RequestMapping(value = MemberController.PATH)
public class MemberController extends BaseController implements IBaseController<Member> {
  public static final String PATH = "/member";

  public static final String VIEW_ADD = "member/add-member-form";
  public static final String VIEW_LIST = "member/list-of-members";
  public static final String VIEW_EDIT = "member/edit-member-form";

  public static final String VIEW_MODEL_TEAMS = "teams";
  public static final String VIEW_MODEL_MEMBER = "member";
  public static final String VIEW_MODEL_MEMBERS = "members";

  public static final String MODEL_ATTRIBUTE_NAME = "name";
  public static final String MODEL_ATTRIBUTE_TEAMS = "teams";

  public static final String MESSAGE_KEY_ADD_SUCCESS = "003.001.000";
  public static final String MESSAGE_KEY_EDIT_SUCCESS = "003.002.000";
  public static final String MESSAGE_KEY_DELETE_SUCCESS = "003.003.000";

  @Autowired
  private ITeamService teamService;

  @Autowired
  private IMemberService memberService;

  public ModelAndView getAddModelPage() {
    ModelAndView modelAndView = null;
    try {
      modelAndView = new ModelAndView(VIEW_ADD);
      modelAndView.addObject(VIEW_MODEL_TEAMS, teamService.getTeams());
      modelAndView.addObject(VIEW_MODEL_MEMBER, new Member());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView addModel(@ModelAttribute Member member, BindingResult result, ModelMap model) {
    Validator formValidation = new MemberValidator();
    ModelAndView modelAndView = null;

    formValidation.validate(member, result);
    if (result.hasErrors()) {
      modelAndView = new ModelAndView(VIEW_ADD);
      modelAndView.addObject(VIEW_MODEL_TEAMS, teamService.getTeams());
      return modelAndView;
    }

    try {
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      member = memberService.addMember(member);
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_ADD_SUCCESS));
      modelAndView.addObject(VIEW_MODEL_MEMBER, member.getId());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView listModel() {
    ModelAndView modelAndView = null;

    try {
      modelAndView = new ModelAndView(VIEW_LIST);
      modelAndView.addObject(VIEW_MODEL_MEMBERS, memberService.getMembers());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView getEditModelPage(@PathVariable Integer id) {
    ModelAndView modelAndView = null;

    try {
      modelAndView = new ModelAndView(VIEW_EDIT);
      modelAndView.addObject(VIEW_MODEL_TEAMS, teamService.getTeams());
      modelAndView.addObject(VIEW_MODEL_MEMBER, memberService.getMember(id));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView editModel(@ModelAttribute Member member, @PathVariable Integer id, BindingResult result, ModelMap modelMap) {
    Validator formValidation = new MemberValidator();
    ModelAndView modelAndView = null;

    formValidation.validate(member, result);
    if (result.hasErrors()) {
      modelAndView = new ModelAndView(VIEW_EDIT);
      modelAndView.addObject(VIEW_MODEL_TEAMS, teamService.getTeams());
      return modelAndView;
    }

    try {
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      memberService.updateMember(member);
      modelAndView.addObject(VIEW_MODEL_MEMBER, member.getId());
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
      memberService.deleteMember(id);
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_DELETE_SUCCESS));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

}
