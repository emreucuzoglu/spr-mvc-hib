package com.sprhib.dao;

import java.util.List;

import com.sprhib.model.ABaseModel;

public interface IBaseDAO {

  /**
   * @param model
   */
  public <T extends ABaseModel> void create(T model);

  /**
   * @param clazz
   * @param id
   * @return
   */
  public <T extends ABaseModel> T read(Class<T> clazz, int id);

  /**
   * @param model
   */
  public <T extends ABaseModel> void update(T model);

  /**
   * @param clazz
   * @param id
   * @return
   */
  public <T extends ABaseModel> T delete(Class<T> clazz, int id);

  /**
   * @param clazz
   * @return
   */
  public <T extends ABaseModel> List<T> getModels(Class<T> clazz);

}
