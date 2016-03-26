package com.sprhib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService {
  @Autowired
  private MessageSource messageSource;

  public String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  public String getMessage(String code, String defaultMessage, Object... args) {
    return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
  }
}
