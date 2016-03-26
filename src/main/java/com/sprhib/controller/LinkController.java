package com.sprhib.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LinkController {
  public static final String PATH = "/";
  public static final String PATH_INDEX = "/index";
  public static final String VIEW_HOME = "home";

  public static final String VIEW_ATTRIBUTE_MESSAGE = "message";

  @RequestMapping(value = PATH)
  public ModelAndView mainPage() {
    return new ModelAndView(VIEW_HOME);
  }

  @RequestMapping(value = PATH_INDEX)
  public ModelAndView indexPage() {
    return new ModelAndView(VIEW_HOME);
  }
}
