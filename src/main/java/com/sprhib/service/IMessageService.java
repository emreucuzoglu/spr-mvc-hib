package com.sprhib.service;

public interface IMessageService {

  /**
   * @param code
   * @param args
   * @return
   */
  public String getMessage(String code, Object... args);

  /**
   * @param code
   * @param defaultMessage
   * @param args
   * @return
   */
  public String getMessage(String code, String defaultMessage, Object... args);

}
