package com.sprhib.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sprhib.utilities.Constants;

@Entity
@Table(name = Constants.DB_TABLE_ORGANIZATION)
public class Organization extends ABaseModel {

  private String name;

  @OneToMany(mappedBy = "organization", cascade = { CascadeType.DETACH }, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Team> teams;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Team> getTeams() {
    return teams;
  }

  public void setTeams(Set<Team> teams) {
    this.teams = teams;
  }

}
