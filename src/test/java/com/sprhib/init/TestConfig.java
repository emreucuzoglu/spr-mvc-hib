package com.sprhib.init;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import com.sprhib.service.IMemberService;
import com.sprhib.service.IOrganizationService;
import com.sprhib.service.ITeamService;

public class TestConfig extends BaseTestConfig {

  @Bean
  public IMemberService memberService() {
    return Mockito.mock(IMemberService.class);
  }

  @Bean
  public ITeamService teamService() {
    return Mockito.mock(ITeamService.class);
  }

  @Bean
  public IOrganizationService organizationService() {
    return Mockito.mock(IOrganizationService.class);
  }

}
