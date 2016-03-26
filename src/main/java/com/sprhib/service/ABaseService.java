package com.sprhib.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.sprhib.dao.IBaseDAO;

public abstract class ABaseService {

  @Autowired
  protected IBaseDAO baseDao;

}
