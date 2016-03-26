package com.sprhib.tests;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.sprhib.controller.MemberController;
import com.sprhib.init.TestConfig;
import com.sprhib.init.WebAppConfig;
import com.sprhib.model.Member;
import com.sprhib.model.Organization;
import com.sprhib.model.Team;
import com.sprhib.service.IMemberService;
import com.sprhib.service.IMessageService;
import com.sprhib.service.ITeamService;
import com.sprhib.utilities.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class MemberControllerTest {

  private static final Integer ID_VALUE = 1;
  private static final Integer RATING_VALUE = 1;
  private static final Integer RATING_VALUE_EDIT = 2;
  private static final Integer TEAM_ID_VALUE = 1;
  private static final Integer TEAM_ID_VALUE_EDIT = 2;
  private static final String TEAM_NAME_VALUE = "name";
  private static final String TEAM_NAME_VALUE_EDIT = "nam2";
  private static final String NAME_VALUE = "name";
  private static final String NAME_VALUE_INVALID = "invalid_name";
  private static final String NAME_VALUE_EDIT = "name2";

  private static Team teamValue;
  private static Team teamValueEdit;
  private static List<Team> teams;
  private static Member memberView;
  private static Member memberDB;
  private static Member memberDBNoTeam;
  private static Member memberEdit;
  private static Member memberEditNoTeam;
  private static List<Member> members;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private ITeamService teamService;
  @Autowired
  private IMemberService memberService;
  @Autowired
  private IMessageService messageService;

  @BeforeClass
  public static void initObjects() {
    Organization organization = new Organization();
    organization.setId(ID_VALUE);
    organization.setName(NAME_VALUE);

    Set<Member> teamValueMembers = new HashSet<>();
    Set<Member> teamValueEditMembers = new HashSet<>();
    Set<Team> memberDBTeams = new HashSet<>();
    Set<Team> memberEditTeams = new HashSet<>();

    memberView = new Member();

    teamValue = new Team();
    teamValue.setId(TEAM_ID_VALUE);
    teamValue.setName(TEAM_NAME_VALUE);
    teamValue.setRating(RATING_VALUE);
    teamValue.setOrganization(organization);
    teamValue.setMembers(teamValueMembers);

    teamValueEdit = new Team();
    teamValueEdit.setId(TEAM_ID_VALUE_EDIT);
    teamValueEdit.setName(TEAM_NAME_VALUE_EDIT);
    teamValueEdit.setRating(RATING_VALUE_EDIT);
    teamValueEdit.setOrganization(organization);
    teamValueEdit.setMembers(teamValueEditMembers);

    memberDB = new Member();
    memberDB.setId(ID_VALUE);
    memberDB.setName(NAME_VALUE);
    memberDB.setTeams(memberDBTeams);
    memberDBTeams.add(teamValue);
    
    memberDBNoTeam = new Member();
    memberDBNoTeam.setId(ID_VALUE);
    memberDBNoTeam.setName(NAME_VALUE);

    memberEdit = new Member();
    memberEdit.setId(ID_VALUE);
    memberEdit.setName(NAME_VALUE_EDIT);
    memberEdit.setTeams(memberEditTeams);
    memberEditTeams.add(teamValueEdit);
    
    memberEditNoTeam = new Member();
    memberEditNoTeam.setId(ID_VALUE);
    memberEditNoTeam.setName(NAME_VALUE_EDIT);

    members = new ArrayList<>();
    members.add(memberDB);

    teams = new ArrayList<>();
    teams.add(teamValue);

  }

  @Before
  public void init() {
    Mockito.reset(memberService);
    when(teamService.getTeam(TEAM_ID_VALUE)).thenReturn(teamValue);
    when(teamService.getTeam(TEAM_ID_VALUE_EDIT)).thenReturn(teamValueEdit);
    when(teamService.getTeams()).thenReturn(teams);

    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

  }

  @Test
  public void testAddPageGet() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_ADD))
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_TEAMS, IsIterableContainingInOrder.contains(teams.toArray())))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, allOf(
        hasProperty(MemberController.MODEL_ATTRIBUTE_ID, nullValue()),
        hasProperty(MemberController.MODEL_ATTRIBUTE_NAME, IsEmptyString.isEmptyOrNullString()),
        hasProperty(MemberController.MODEL_ATTRIBUTE_TEAMS, nullValue())
    )))
    ;
    
    verifyZeroInteractions(memberService);
  }
  
  @Test
  public void testAddPagePost_Exception() throws Exception {
    when(memberService.addMember(isA(Member.class))).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE.toString())
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
    ;
    
    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).addMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);
  }
  
  @Test
  public void testAddPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE.toString())
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(MemberController.VIEW_MODEL_MEMBER, MemberController.MODEL_ATTRIBUTE_NAME));

    verifyNoMoreInteractions(memberService);
  }
  
  @Test
  public void testAddPagePost_SuccessWithNoTeam() throws Exception {
    when(memberService.addMember(isA(Member.class))).thenReturn(memberDB);
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_ADD_SUCCESS))));

    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).addMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);

    Member formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE));
    assertThat(formObject.getTeams(), nullValue());
  }
  
  @Test
  public void testAddPagePost_SuccessWithTeam() throws Exception {
    when(memberService.addMember(isA(Member.class))).thenReturn(memberDB);
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_ADD_SUCCESS))));

    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).addMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);

    Member formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE));
    assertThat(formObject.getTeams(), IsIterableContainingInOrder.contains(memberDB.getTeams().toArray()));
  }

  @Test
  public void testListPage_Exception() throws Exception {
    when(memberService.getMembers()).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(LinkController.VIEW_HOME))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
      ;
    
    verify(memberService, times(1)).getMembers();
    verifyNoMoreInteractions(memberService);
  }

  @Test
  public void testListPage_Success() throws Exception {
    when(memberService.getMembers()).thenReturn(members);
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(MemberController.VIEW_LIST))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBERS, hasSize(1)))
      .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBERS, IsIterableContainingInOrder.contains(members.toArray())))
      ;
    
    verify(memberService, times(1)).getMembers();
    verifyNoMoreInteractions(memberService);
  }
  
  @Test
  public void testEditPageGet_Exception() throws Exception {
    when(memberService.getMember(ID_VALUE)).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
    ;
    verify(memberService, times(1)).getMember(ID_VALUE);
    verifyZeroInteractions(memberService);
  }
  
  @Test
  public void testEditPageGet_InvalidId() throws Exception {
    when(memberService.getMember(ID_VALUE)).thenThrow(new RuntimeException(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)));
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)))
    ;
    verify(memberService, times(1)).getMember(ID_VALUE);
    verifyZeroInteractions(memberService);
  }
  
  @Test
  public void testEditPageGet_Success() throws Exception {
    when(memberService.getMember(ID_VALUE)).thenReturn(memberDB);
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_TEAMS, IsIterableContainingInOrder.contains(teams.toArray())))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, allOf(
        hasProperty(MemberController.MODEL_ATTRIBUTE_ID, is(ID_VALUE)),
        hasProperty(MemberController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE)),
        hasProperty(MemberController.MODEL_ATTRIBUTE_TEAMS, IsIterableContainingInOrder.contains(memberDB.getTeams().toArray()))
    )))
    ;
    verify(memberService, times(1)).getMember(ID_VALUE);
    verifyZeroInteractions(memberService);
  }
  
  @Test
  public void testEditPagePost_Exception() throws Exception {
    when(memberService.updateMember(isA(Member.class))).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE.toString())
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
    ;
    
    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).updateMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);
  }
  
  @Test
  public void testEditPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(MemberController.VIEW_MODEL_MEMBER, MemberController.MODEL_ATTRIBUTE_NAME));

    verifyNoMoreInteractions(memberService);
  }
  
  @Test
  public void testEditPagePost_SuccessWithNoTeam() throws Exception {
    when(memberService.updateMember(isA(Member.class))).thenReturn(memberEditNoTeam);
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS))));

    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).updateMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);

    Member formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE_EDIT));
    assertThat(formObject.getTeams(), nullValue());
  }
  
  @Test
  public void testEditPagePost_SuccessWithTeam() throws Exception {
    when(memberService.updateMember(isA(Member.class))).thenReturn(memberEdit);
    
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE_EDIT.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS))));

    ArgumentCaptor<Member> formObjectArgument = ArgumentCaptor.forClass(Member.class);
    verify(memberService, times(1)).updateMember(formObjectArgument.capture());
    verifyNoMoreInteractions(memberService);

    Member formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE_EDIT));
    assertThat(formObject.getTeams(), IsIterableContainingInOrder.contains(memberEdit.getTeams().toArray()));
  }
  
  @Test
  public void testDeletePageGet_InvalidId() throws Exception {
    when(memberService.deleteMember(ID_VALUE)).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_DELETE + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is("e")))
    ;
    verify(memberService, times(1)).deleteMember(ID_VALUE);
    verifyZeroInteractions(memberService);
  }
  
  @Test
  public void testDeletePageGet_Success() throws Exception {
    when(memberService.deleteMember(ID_VALUE)).thenReturn(memberDB);
    
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_DELETE + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_DELETE_SUCCESS))))
    ;
    verify(memberService, times(1)).deleteMember(ID_VALUE);
    verifyZeroInteractions(memberService);
  }

}
