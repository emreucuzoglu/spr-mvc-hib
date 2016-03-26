package com.sprhib.controller;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sprhib.model.ABaseModel;

public interface IBaseController<T extends ABaseModel> {

  public static final String PATH_ADD = "/add";
  public static final String PATH_LIST = "/list";
  public static final String PATH_EDIT = "/edit";
  public static final String PATH_DELETE = "/delete";

  public static final String REQUEST_PARAM_ID = "/{id}";

  /**
   * @return
   */
  @RequestMapping(value = PATH_ADD, method = RequestMethod.GET)
  public ModelAndView getAddModelPage();

  /**
   * @param model
   * @param result
   * @param modelMap
   * @return
   */
  @RequestMapping(value = PATH_ADD, method = RequestMethod.POST)
  public ModelAndView addModel(@ModelAttribute T model, BindingResult result, ModelMap modelMap);

  /**
   * @return
   */
  @RequestMapping(value = PATH_LIST, method = RequestMethod.GET)
  public ModelAndView listModel();

  /**
   * @param id
   * @return
   */
  @RequestMapping(value = PATH_EDIT + REQUEST_PARAM_ID, method = RequestMethod.GET)
  public ModelAndView getEditModelPage(@PathVariable Integer id);

  /**
   * @param model
   * @param id
   * @param result
   * @param modelMap
   * @return
   */
  @RequestMapping(value = PATH_EDIT + REQUEST_PARAM_ID, method = RequestMethod.POST)
  public ModelAndView editModel(@ModelAttribute T model, @PathVariable Integer id, BindingResult result, ModelMap modelMap);

  /**
   * @param id
   * @return
   */
  @RequestMapping(value = PATH_DELETE + REQUEST_PARAM_ID, method = RequestMethod.GET)
  public ModelAndView deleteModel(@PathVariable Integer id);

}
