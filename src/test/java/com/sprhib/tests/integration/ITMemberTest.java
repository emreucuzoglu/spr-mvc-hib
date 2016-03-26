package com.sprhib.tests.integration;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.text.IsEmptyString;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.sprhib.controller.LinkController;
import com.sprhib.controller.MemberController;
import com.sprhib.init.BaseTestConfig;
import com.sprhib.init.WebAppConfig;
import com.sprhib.model.Member;
import com.sprhib.model.Organization;
import com.sprhib.model.Team;
import com.sprhib.service.IMessageService;
import com.sprhib.tests.TestConstants;
import com.sprhib.utilities.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = BaseTestConfig.class)
@TestExecutionListeners({ 
   DependencyInjectionTestExecutionListener.class,
   DirtiesContextTestExecutionListener.class,
   TransactionDbUnitTestExecutionListener.class })
@Category(IIntegrationTest.class)
@DatabaseSetup(value=ITMemberTest.DB_INIT, type=DatabaseOperation.CLEAN_INSERT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ITMemberTest {
  protected static final String PATH_XML_DATA = TestConstants.PATH_XML_DATA_PREFIX + MemberController.PATH;
  protected static final String DB_INIT = PATH_XML_DATA + TestConstants.PATH_XML_DATA;
  protected static final String DB_ADDED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_ADDED;
  protected static final String DB_UPDATED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_UPDATED;
  protected static final String DB_DELETED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_DELETED;
  protected static final String DB_ADDED_NO_TEAM = PATH_XML_DATA + "/dataAddedNoTeam.xml";
  protected static final String DB_UPDATED_NO_TEAM = PATH_XML_DATA + "/dataUpdatedNoTeam.xml";

  private static final Integer ID_VALUE_DB1 = 1;
  private static final Integer ID_VALUE_DB2 = 2;
  private static final Integer ID_VALUE_ADDED = 3;
  private static final Integer ID_VALUE_INVALID = 4;
  
  private static final String NAME_VALUE_DB1 = "name1";
  private static final String NAME_VALUE_DB2 = "name2";
  private static final String NAME_VALUE_ADDED = "name3";
  private static final String NAME_VALUE_INVALID = "invalid_name";
  private static final String NAME_VALUE_EDIT = "name";
  
  private static final Integer ORGANIZATION_ID_VALUE = 1;
  private static final String ORGANIZATION_NAME_VALUE = "org1";
  private static final Integer TEAM_ID_VALUE1 = 1;
  private static final Integer TEAM_ID_VALUE2 = 2;
  private static final String TEAM_NAME_VALUE1 = "team1";
  private static final String TEAM_NAME_VALUE2 = "team2";
  private static final Integer RATING_VALUE = 1;

  private static List<Team> teams;
  private static Member memberView;
  private static Member memberDB;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private IMessageService messageService;
  @Autowired
  private SessionFactory sessionFactory;

  @BeforeClass
  public static void initObjects() {
    Set<Member> team1Members = new HashSet<>();
    Set<Team> member1Teams = new HashSet<>();
    
    Organization organization = new Organization();
    organization.setId(ORGANIZATION_ID_VALUE);
    organization.setName(ORGANIZATION_NAME_VALUE);

    memberView = new Member();
    
    memberDB = new Member();
    memberDB.setId(ID_VALUE_DB1);
    memberDB.setName(NAME_VALUE_DB1);
    memberDB.setTeams(member1Teams);

    Team team1 = new Team();
    team1.setId(TEAM_ID_VALUE1);
    team1.setName(TEAM_NAME_VALUE1);
    team1.setRating(RATING_VALUE);
    team1.setOrganization(organization);
    team1.setMembers(team1Members);
    
    Team team2 = new Team();
    team2.setId(TEAM_ID_VALUE2);
    team2.setName(TEAM_NAME_VALUE2);
    team2.setRating(RATING_VALUE);
    team2.setOrganization(organization);
    team2.setMembers(team1Members);

    teams = new ArrayList<>();
    teams.add(team1);
    teams.add(team2);
    
    member1Teams.add(team1);

  }

  @Before
  public void init() throws SQLException {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }
  
  @After
  public void flush() {
    sessionFactory.getCurrentSession().flush();
  }

  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE1.toString())
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(MemberController.VIEW_MODEL_MEMBER, MemberController.MODEL_ATTRIBUTE_NAME));
  }
  
  @Test
  @ExpectedDatabase(value = DB_ADDED_NO_TEAM, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_SuccessWithNoTeam() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE_ADDED)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_ADD_SUCCESS))));
  }
  
  @Test
  @ExpectedDatabase(value = DB_ADDED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_SuccessWithTeam() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE1.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE_ADDED)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_ADD_SUCCESS))));
  }

  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testListPage_Success() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(MemberController.VIEW_LIST))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBERS, hasSize(2)))
      .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBERS, hasItem(
          allOf(
              hasProperty(MemberController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
              hasProperty(MemberController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1))
      )
  )))
  .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBERS, hasItem(
      allOf(
          hasProperty(MemberController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB2)),
          hasProperty(MemberController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB2))
      )
  )))
  ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_InvalidId() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE_INVALID)))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_Success() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_TEAMS, IsIterableContainingInOrder.contains(teams.toArray())))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, allOf(
        hasProperty(MemberController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
        hasProperty(MemberController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1)),
        hasProperty(MemberController.MODEL_ATTRIBUTE_TEAMS, IsIterableContainingInOrder.contains(memberDB.getTeams().toArray()))
    )))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE1.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(MemberController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + MemberController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(MemberController.VIEW_MODEL_MEMBER, MemberController.MODEL_ATTRIBUTE_NAME));
  }
  
  @Test
  @ExpectedDatabase(value = DB_UPDATED_NO_TEAM, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_SuccessWithNoTeam() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE_DB1)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS))));
  }
  
  @Test
  @ExpectedDatabase(value = DB_UPDATED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_SuccessWithTeam() throws Exception {
    mockMvc.perform(post(MemberController.PATH + MemberController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(MemberController.MODEL_ATTRIBUTE_TEAMS, TEAM_ID_VALUE2.toString())
        .param(MemberController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(MemberController.VIEW_MODEL_MEMBER, memberView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(MemberController.VIEW_MODEL_MEMBER, is(ID_VALUE_DB1)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS))));
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testDeletePageGet_InvalidId() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_DELETE + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE_INVALID))));
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_DELETED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testDeletePageGet_Success() throws Exception {
    mockMvc.perform(get(MemberController.PATH + MemberController.PATH_DELETE + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(MemberController.MESSAGE_KEY_DELETE_SUCCESS))))
    ;
  }

}
