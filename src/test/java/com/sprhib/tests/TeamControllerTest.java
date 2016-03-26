package com.sprhib.tests;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.text.IsEmptyString;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sprhib.controller.LinkController;
import com.sprhib.controller.OrganizationController;
import com.sprhib.controller.TeamController;
import com.sprhib.init.TestConfig;
import com.sprhib.init.WebAppConfig;
import com.sprhib.model.Organization;
import com.sprhib.model.Team;
import com.sprhib.service.IMessageService;
import com.sprhib.service.IOrganizationService;
import com.sprhib.service.ITeamService;
import com.sprhib.utilities.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
@TestExecutionListeners({ 
   DependencyInjectionTestExecutionListener.class,
   DirtiesContextTestExecutionListener.class})
public class TeamControllerTest {
   private static final Integer ID_VALUE = 1;
   private static final Integer RATING_VALUE = 1;
   private static final Integer RATING_VALUE_EDIT = 2;
   private static final Integer ORGANIZATION_ID_VALUE = 1;
   private static final Integer ORGANIZATION_ID_VALUE_EDIT = 2;
   private static final String ORGANIZATION_NAME_VALUE = "name";
   private static final String ORGANIZATION_NAME_VALUE_EDIT = "nam2";
   private static final String NAME_VALUE = "name";
   private static final String NAME_VALUE_INVALID = "invalid_name";
   private static final String NAME_VALUE_EDIT = "name2";
   
   private static Organization organizationValue;
   private static Organization organizationValueEdit;
   private static List<Organization> organizations;
   private static Team teamView;
   private static Team teamDB;
   private static Team teamEdit;
   private static List<Team> teams;

   private MockMvc mockMvc;

   @Autowired
   private WebApplicationContext wac;
   @Autowired
   private IOrganizationService organizationService;
   @Autowired
   private ITeamService teamService;
   @Autowired
   private IMessageService messageService;

   @BeforeClass
   public static void initObjects() {
      teamView = new Team();
      organizationValue = new Organization();
      organizationValue.setId(ORGANIZATION_ID_VALUE);
      organizationValue.setName(ORGANIZATION_NAME_VALUE);

      teamDB = new Team();
      teamDB.setId(ID_VALUE);
      teamDB.setName(NAME_VALUE);
      teamDB.setOrganization(organizationValue);
      teamDB.setRating(RATING_VALUE);

      organizationValueEdit = new Organization();
      organizationValueEdit.setId(ORGANIZATION_ID_VALUE_EDIT);
      organizationValueEdit.setName(ORGANIZATION_NAME_VALUE_EDIT);

      teamEdit = new Team();
      teamEdit.setId(ID_VALUE);
      teamEdit.setName(NAME_VALUE_EDIT);
      teamEdit.setOrganization(organizationValue);
      teamEdit.setRating(RATING_VALUE_EDIT);

      teams = new ArrayList<>();
      teams.add(teamDB);
      
      organizations = new ArrayList<>();
      organizations.add(organizationValue);
   }

   @Before
   public void init() {
      Mockito.reset(teamService);
      Mockito.reset(organizationService);
      when(organizationService.getOrganization(ORGANIZATION_ID_VALUE)).thenReturn(organizationValue);
      when(organizationService.getOrganization(ORGANIZATION_ID_VALUE_EDIT)).thenReturn(organizationValueEdit);
      when(organizationService.getOrganizations()).thenReturn(organizations);
      
      mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
   }
   
   @Test
   public void testAddPageGet() throws Exception {
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_ADD))
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_ADD))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_ORGANIZATIONS, IsIterableContainingInOrder.contains(organizations.toArray())))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, allOf(
         hasProperty(TeamController.MODEL_ATTRIBUTE_ID, nullValue()),
         hasProperty(TeamController.MODEL_ATTRIBUTE_NAME, IsEmptyString.isEmptyOrNullString()),
         hasProperty(TeamController.MODEL_ATTRIBUTE_RATING, nullValue()),
         hasProperty(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, nullValue()),
         hasProperty(TeamController.MODEL_ATTRIBUTE_MEMBERS, nullValue())
     )))
     ;
     
     verifyZeroInteractions(teamService);
   }
   
   @Test
   public void testAddPagePost_Exception() throws Exception {
     when(teamService.addTeam(isA(Team.class))).thenThrow(new RuntimeException("e"));
     
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
     ;
     
     ArgumentCaptor<Team> formObjectArgument = ArgumentCaptor.forClass(Team.class);
     verify(teamService, times(1)).addTeam(formObjectArgument.capture());
     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testAddPagePost_NoOrganization() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_ADD))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_ORGANIZATION));

     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testAddPagePost_InvalidName() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_ADD))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_NAME));

     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testAddPagePost_NoRating() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_ADD))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_RATING));

     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testAddPagePost_Success() throws Exception {
     when(teamService.addTeam(isA(Team.class))).thenReturn(teamDB);
     
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, is(ID_VALUE)))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_ADD_SUCCESS))));

     ArgumentCaptor<Team> formObjectArgument = ArgumentCaptor.forClass(Team.class);
     verify(teamService, times(1)).addTeam(formObjectArgument.capture());
     verifyNoMoreInteractions(teamService);

     Team formObject = formObjectArgument.getValue();
     assertThat(formObject.getOrganization().getId(), is(ORGANIZATION_ID_VALUE));
     assertThat(formObject.getName(), is(NAME_VALUE));
     assertThat(formObject.getRating(), is(RATING_VALUE));
   }

   @Test
   public void testListPage_Exception() throws Exception {
     when(teamService.getTeams()).thenThrow(new RuntimeException("e"));
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_LIST))
       .andExpect(status().isOk())
       .andExpect(view().name(LinkController.VIEW_HOME))
       .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
       .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
       ;
     
     verify(teamService, times(1)).getTeams();
     verifyNoMoreInteractions(teamService);
   }

   @Test
   public void testListPage_Success() throws Exception {
     when(teamService.getTeams()).thenReturn(teams);
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_LIST))
       .andExpect(status().isOk())
       .andExpect(view().name(TeamController.VIEW_LIST))
       .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
       .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAMS, hasSize(1)))
       .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAMS, IsIterableContainingInOrder.contains(teams.toArray())))
       ;
     
     verify(teamService, times(1)).getTeams();
     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testEditPageGet_Exception() throws Exception {
     when(teamService.getTeam(ID_VALUE)).thenThrow(new RuntimeException("e"));
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE))
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
     ;
     verify(teamService, times(1)).getTeam(ID_VALUE);
     verifyZeroInteractions(teamService);
   }
   
   @Test
   public void testEditPageGet_InvalidId() throws Exception {
     when(teamService.getTeam(ID_VALUE)).thenThrow(new RuntimeException(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)));
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE))
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)))
     ;
     verify(teamService, times(1)).getTeam(ID_VALUE);
     verifyZeroInteractions(teamService);
   }
   
   @Test
   public void testEditPageGet_Success() throws Exception {
     when(teamService.getTeam(ID_VALUE)).thenReturn(teamDB);
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE))
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_EDIT))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_ORGANIZATIONS, IsIterableContainingInOrder.contains(organizations.toArray())))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, allOf(
         hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ID_VALUE)),
         hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE)),
         hasProperty(TeamController.MODEL_ATTRIBUTE_RATING, is(RATING_VALUE)),
         hasProperty(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, is(organizationValue)),
         hasProperty(TeamController.MODEL_ATTRIBUTE_MEMBERS, nullValue())
     )))
     ;
     verify(teamService, times(1)).getTeam(ID_VALUE);
     verifyZeroInteractions(teamService);
   }
   
   @Test
   public void testEditPagePost_NoOrganization() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
         )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_EDIT))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_ORGANIZATION));
     
     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testEditPagePost_InvalidName() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
         )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_EDIT))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_NAME));
     
     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testEditPagePost_NoRating() throws Exception {
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
         )
     .andExpect(status().isOk())
     .andExpect(view().name(TeamController.VIEW_EDIT))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_RATING));
     
     verifyNoMoreInteractions(teamService);
   }
   
   @Test
   public void testEditPagePost_Success() throws Exception {
     when(teamService.updateTeam(isA(Team.class))).thenReturn(teamEdit);
     
     mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE)
         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
         .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE_EDIT.toString())
         .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
         .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE_EDIT.toString())
         .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
         )
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, is(ID_VALUE)))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_EDIT_SUCCESS))));

     ArgumentCaptor<Team> formObjectArgument = ArgumentCaptor.forClass(Team.class);
     verify(teamService, times(1)).updateTeam(formObjectArgument.capture());
     verifyNoMoreInteractions(teamService);

     Team formObject = formObjectArgument.getValue();
     assertThat(formObject.getOrganization().getId(), is(ORGANIZATION_ID_VALUE_EDIT));
     assertThat(formObject.getId(), is(ID_VALUE));
     assertThat(formObject.getName(), is(NAME_VALUE_EDIT));
     assertThat(formObject.getRating(), is(RATING_VALUE_EDIT));
   }
   
   @Test
   public void deleteEditPageGet_InvalidId() throws Exception {
     when(teamService.deleteTeam(ID_VALUE)).thenThrow(new RuntimeException("e"));
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_DELETE + "/" + ID_VALUE))
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
     ;
     verify(teamService, times(1)).deleteTeam(ID_VALUE);
     verifyZeroInteractions(teamService);
   }
   
   @Test
   public void deleteEditPageGet_Success() throws Exception {
     when(teamService.deleteTeam(ID_VALUE)).thenReturn(teamDB);
     
     mockMvc.perform(get(TeamController.PATH + TeamController.PATH_DELETE + "/" + ID_VALUE))
     .andExpect(status().isOk())
     .andExpect(view().name(LinkController.VIEW_HOME))
     .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
     .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_DELETE_SUCCESS))))
     ;
     verify(teamService, times(1)).deleteTeam(ID_VALUE);
     verifyZeroInteractions(teamService);
   }

}
