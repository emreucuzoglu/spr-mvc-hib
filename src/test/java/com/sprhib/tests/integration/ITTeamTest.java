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

import java.util.ArrayList;
import java.util.List;

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
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.sprhib.controller.LinkController;
import com.sprhib.controller.OrganizationController;
import com.sprhib.controller.TeamController;
import com.sprhib.init.BaseTestConfig;
import com.sprhib.init.WebAppConfig;
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
@DatabaseSetup(ITTeamTest.DB_INIT)
public class ITTeamTest {
  protected static final String PATH_XML_DATA = TestConstants.PATH_XML_DATA_PREFIX + TeamController.PATH;
  protected static final String DB_INIT = PATH_XML_DATA + TestConstants.PATH_XML_DATA;
  protected static final String DB_ADDED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_ADDED;
  protected static final String DB_UPDATED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_UPDATED;
  protected static final String DB_DELETED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_DELETED;
  
  private static final Integer ID_VALUE_DB1 = 1;
  private static final Integer ID_VALUE_DB2 = 2;
  private static final Integer ID_VALUE_ADDED = 3;
  private static final Integer ID_VALUE_INVALID = 4;
  
  private static final String NAME_VALUE_DB1 = "name1";
  private static final String NAME_VALUE_DB2 = "name2";
  private static final String NAME_VALUE_ADDED = "name3";
  private static final String NAME_VALUE_INVALID = "invalid_name";
  private static final String NAME_VALUE_EDIT = "name";

  private static final Integer RATING_VALUE = 1;
  private static final Integer RATING_VALUE_EDIT = 2;
  
  private static final Integer ORGANIZATION_ID_VALUE = 1;
  private static final Integer ORGANIZATION_ID_VALUE_EDIT = 2;
  
  private static final String ORGANIZATION_NAME_VALUE = "org1";
  private static final String ORGANIZATION_NAME_VALUE_EDIT = "org2";
  
  private static List<Organization> organizations;
  private static Team teamView;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private IMessageService messageService;
  @Autowired
  private SessionFactory sessionFactory;

  @BeforeClass
  public static void initObjects() {
     teamView = new Team();
     
     Organization organization;
     organizations = new ArrayList<>();
     
     organization = new Organization();
     organization.setId(ORGANIZATION_ID_VALUE);
     organization.setName(ORGANIZATION_NAME_VALUE);
     organizations.add(organization);
     
     organization = new Organization();
     organization.setId(ORGANIZATION_ID_VALUE_EDIT);
     organization.setName(ORGANIZATION_NAME_VALUE_EDIT);
     organizations.add(organization);
  }

  @Before
  public void init() {
     mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }
  
  @After
  public void flush() {
    sessionFactory.getCurrentSession().flush();
  }  
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_NoOrganization() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(TeamController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_ORGANIZATION));
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
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
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_NoRating() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(TeamController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_RATING));
  }
  
  @Test
  @ExpectedDatabase(value = DB_ADDED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_Success() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, is(ID_VALUE_ADDED)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_ADD_SUCCESS))));

  }

  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testListPage_Success() throws Exception {
    mockMvc.perform(get(TeamController.PATH + TeamController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(TeamController.VIEW_LIST))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAMS, hasSize(2)))
      .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAMS, hasItem(
          allOf(
                  hasProperty(TeamController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
                  hasProperty(TeamController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1)),
                  hasProperty(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ORGANIZATION_ID_VALUE))),
                  hasProperty(TeamController.MODEL_ATTRIBUTE_RATING, is(RATING_VALUE)),
                  hasProperty(TeamController.MODEL_ATTRIBUTE_MEMBERS, hasSize(0))
          )
      )))
      .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAMS, hasItem(
          allOf(
              hasProperty(TeamController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB2)),
              hasProperty(TeamController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB2)),
              hasProperty(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ORGANIZATION_ID_VALUE))),
              hasProperty(TeamController.MODEL_ATTRIBUTE_RATING, is(RATING_VALUE)),
              hasProperty(TeamController.MODEL_ATTRIBUTE_MEMBERS, hasSize(0))
          )
      )))
      ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_InvalidId() throws Exception {
    mockMvc.perform(get(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE_INVALID)))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_Success() throws Exception {
    mockMvc.perform(get(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(TeamController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(TeamController.VIEW_MODEL_ORGANIZATIONS, IsIterableContainingInOrder.contains(organizations.toArray())))
    .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, allOf(
        hasProperty(TeamController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
        hasProperty(TeamController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1)),
        hasProperty(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ORGANIZATION_ID_VALUE))),
        hasProperty(TeamController.MODEL_ATTRIBUTE_RATING, is(RATING_VALUE)),
        hasProperty(TeamController.MODEL_ATTRIBUTE_MEMBERS, hasSize(0))
    )))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_NoOrganization() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE.toString())
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(TeamController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_ORGANIZATION));
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_DB1)
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
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_NoRating() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE.toString())
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(TeamController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + TeamController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(TeamController.VIEW_MODEL_TEAM, TeamController.MODEL_ATTRIBUTE_RATING));
  }
  
  @Test
  @ExpectedDatabase(value = DB_UPDATED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_Success() throws Exception {
    mockMvc.perform(post(TeamController.PATH + TeamController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(TeamController.MODEL_ATTRIBUTE_ORGANIZATION, ORGANIZATION_ID_VALUE_EDIT.toString())
        .param(TeamController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .param(TeamController.MODEL_ATTRIBUTE_RATING, RATING_VALUE_EDIT.toString())
        .sessionAttr(TeamController.VIEW_MODEL_TEAM, teamView)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(TeamController.VIEW_MODEL_TEAM, is(ID_VALUE_DB1)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_EDIT_SUCCESS))));
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void deleteEditPageGet_InvalidId() throws Exception {
    mockMvc.perform(get(TeamController.PATH + TeamController.PATH_DELETE + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE_INVALID))))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_DELETED, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void deleteEditPageGet_Success() throws Exception {
    mockMvc.perform(get(TeamController.PATH + TeamController.PATH_DELETE + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(TeamController.MESSAGE_KEY_DELETE_SUCCESS))))
    ;
  }

}
