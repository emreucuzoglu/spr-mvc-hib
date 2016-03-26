package com.sprhib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.sprhib.converter.OrganizationEditor;
import com.sprhib.converter.TeamEditor;
import com.sprhib.model.Organization;
import com.sprhib.model.Team;
import com.sprhib.service.IMessageService;

@Controller
public class BaseController {

  public static final String MODEL_ATTRIBUTE_ID = "id";

  @Autowired
  private OrganizationEditor organizationEditor;

  @Autowired
  private TeamEditor teamEditor;

  @Autowired
  protected IMessageService messageService;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Organization.class, organizationEditor);
    binder.registerCustomEditor(Team.class, teamEditor);
  }

  protected ModelAndView handleException(Throwable t) {
    ModelAndView modelAndView = new ModelAndView(LinkController.VIEW_HOME);
    modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, t.getLocalizedMessage());

    return modelAndView;
  }

}
