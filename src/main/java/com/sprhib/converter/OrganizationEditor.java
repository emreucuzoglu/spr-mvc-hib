package com.sprhib.converter;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sprhib.service.IOrganizationService;

@Component
public class OrganizationEditor extends PropertyEditorSupport {

  @Autowired
  private IOrganizationService organizationService;

  @Override
  public void setAsText(String text) {
    this.setValue(organizationService.getOrganization(Integer.valueOf(text)));
  }
}