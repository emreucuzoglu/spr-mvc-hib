package com.sprhib.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprhib.model.Organization;

@Service
@Transactional
public class OrganizationServiceImpl extends ABaseService implements IOrganizationService {

  @Override
  public Organization addOrganization(Organization organization) {
    baseDao.create(organization);
    return organization;
  }

  @Override
  public Organization updateOrganization(Organization organization) {
    baseDao.update(organization);
    return organization;
  }

  @Override
  public Organization getOrganization(int id) {
    return baseDao.read(Organization.class, id);
  }

  @Override
  public Organization deleteOrganization(int id) {
    return baseDao.delete(Organization.class, id);
  }

  @Override
  public List<Organization> getOrganizations() {
    return baseDao.getModels(Organization.class);
  }

}
