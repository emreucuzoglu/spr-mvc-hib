package com.sprhib.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.sprhib.utilities.Constants;

@Entity
@Table(name = Constants.DB_TABLE_MEMBER)
public class Member extends ABaseModel {

  private String name;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
  @JoinTable(name = "member_team", joinColumns = { @JoinColumn(name = "member_id", nullable = false, updatable = false) }, inverseJoinColumns = {
      @JoinColumn(name = "team_id", nullable = false, updatable = false) })
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
