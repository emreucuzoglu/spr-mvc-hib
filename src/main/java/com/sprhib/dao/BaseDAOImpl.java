package com.sprhib.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sprhib.model.ABaseModel;
import com.sprhib.service.IMessageService;
import com.sprhib.utilities.Constants;

@Repository
@SuppressWarnings("unchecked")
public class BaseDAOImpl implements IBaseDAO {

  @Autowired
  private SessionFactory sessionFactory;
  @Autowired
  protected IMessageService messageService;

  private Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  @Override
  public <T extends ABaseModel> void create(T model) {
    getCurrentSession().save(model);
  }

  @Override
  public <T extends ABaseModel> T read(Class<T> clazz, int id) {
    T model = (T) getCurrentSession().get(clazz, id);

    if (model == null) {
      throw new RuntimeException(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, id));
    }

    return model;
  }

  @Override
  public <T extends ABaseModel> void update(T model) {
    getCurrentSession().update(model);

  }

  @Override
  public <T extends ABaseModel> T delete(Class<T> clazz, int id) {
    T model = read(clazz, id);
    if (model != null) {
      getCurrentSession().delete(model);
    }

    return model;
  }

  @Override
  public <T extends ABaseModel> List<T> getModels(Class<T> clazz) {
    return getCurrentSession().createQuery("from " + clazz.getSimpleName()).list();
  }

}
