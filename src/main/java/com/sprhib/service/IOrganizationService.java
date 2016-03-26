package com.sprhib.service;

import java.util.List;

import com.sprhib.model.Organization;

public interface IOrganizationService {

  /**
   * @param organization
   * @return
   */
  public Organization addOrganization(Organization organization);

  /**
   * @param organization
   * @return
   */
  public Organization updateOrganization(Organization organization);

  /**
   * @param id
   * @return
   */
  public Organization getOrganization(int id);

  /**
   * @param id
   * @return
   */
  public Organization deleteOrganization(int id);

  /**
   * @return
   */
  public List<Organization> getOrganizations();

}
