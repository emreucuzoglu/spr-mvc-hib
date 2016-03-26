package com.sprhib.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprhib.model.Team;

@Service
@Transactional
public class TeamServiceImpl extends ABaseService implements ITeamService {

  public Team addTeam(Team team) {
    baseDao.create(team);
    return team;
  }

  public Team updateTeam(Team team) {
    baseDao.update(team);
    return team;
  }

  public Team getTeam(int id) {
    return baseDao.read(Team.class, id);
  }

  public Team deleteTeam(int id) {
    return baseDao.delete(Team.class, id);
  }

  public List<Team> getTeams() {
    return baseDao.getModels(Team.class);
  }

}
