package com.sprhib.service;

import java.util.List;

import com.sprhib.model.Team;

public interface ITeamService {

  /**
   * @param team
   * @return
   */
  public Team addTeam(Team team);

  /**
   * @param team
   * @return
   */
  public Team updateTeam(Team team);

  /**
   * @param id
   * @return
   */
  public Team getTeam(int id);

  /**
   * @param id
   * @return
   */
  public Team deleteTeam(int id);

  /**
   * @return
   */
  public List<Team> getTeams();

}
