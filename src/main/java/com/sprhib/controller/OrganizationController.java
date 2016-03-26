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

import com.sprhib.model.Organization;
import com.sprhib.service.IOrganizationService;
import com.sprhib.validators.OrganizationValidator;

@Controller
@RequestMapping(value = OrganizationController.PATH)
public class OrganizationController extends BaseController implements IBaseController<Organization> {
  public static final String PATH = "/org";

  public static final String VIEW_ADD = "org/add-org-form";
  public static final String VIEW_LIST = "org/list-of-orgs";
  public static final String VIEW_EDIT = "org/edit-org-form";

  public static final String VIEW_MODEL_ORGANIZATION = "organization";
  public static final String VIEW_MODEL_ORGANIZATIONS = "organizations";

  public static final String MODEL_ATTRIBUTE_ID = "id";
  public static final String MODEL_ATTRIBUTE_NAME = "name";

  public static final String MESSAGE_KEY_ADD_SUCCESS = "001.001.000";
  public static final String MESSAGE_KEY_EDIT_SUCCESS = "001.002.000";
  public static final String MESSAGE_KEY_DELETE_SUCCESS = "001.003.000";

  @Autowired
  private IOrganizationService organizationService;

  public ModelAndView getAddModelPage() {
    ModelAndView modelAndView = new ModelAndView(VIEW_ADD);
    modelAndView.addObject(VIEW_MODEL_ORGANIZATION, new Organization());
    return modelAndView;
  }

  public ModelAndView addModel(@ModelAttribute Organization organization, BindingResult result, ModelMap model) {
    Validator formValidation = new OrganizationValidator();

    formValidation.validate(organization, result);
    if (result.hasErrors()) {
      return new ModelAndView(VIEW_ADD);
    }

    ModelAndView modelAndView = null;

    try {
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      organization = organizationService.addOrganization(organization);
      modelAndView.addObject(VIEW_MODEL_ORGANIZATION, organization.getId());
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
      modelAndView.addObject(VIEW_MODEL_ORGANIZATIONS, organizationService.getOrganizations());
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public @ResponseBody ModelAndView getEditModelPage(@PathVariable Integer id) {
    ModelAndView modelAndView = null;

    try {
      modelAndView = new ModelAndView(VIEW_EDIT);
      modelAndView.addObject(VIEW_MODEL_ORGANIZATION, organizationService.getOrganization(id));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

  public ModelAndView editModel(@ModelAttribute Organization organization, @PathVariable Integer id, BindingResult result, ModelMap modelMap) {
    OrganizationValidator formValidation = new OrganizationValidator();

    formValidation.validate(organization, result);
    if (result.hasErrors()) {
      return new ModelAndView(VIEW_EDIT);
    }

    ModelAndView modelAndView = null;

    try {
      modelAndView = new ModelAndView(LinkController.VIEW_HOME);
      organizationService.updateOrganization(organization);
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
      organizationService.deleteOrganization(id);
      modelAndView.addObject(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(MESSAGE_KEY_DELETE_SUCCESS));
    } catch (Throwable e) {
      modelAndView = handleException(e);
    }

    return modelAndView;
  }

}
