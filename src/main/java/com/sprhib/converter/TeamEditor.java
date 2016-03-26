package com.sprhib.converter;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sprhib.service.ITeamService;

@Component
public class TeamEditor extends PropertyEditorSupport {

  @Autowired
  private ITeamService teamService;

  @Override
  public void setAsText(String text) {
    this.setValue(teamService.getTeam(Integer.valueOf(text)));
  }
}