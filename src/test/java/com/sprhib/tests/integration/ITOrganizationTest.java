package com.sprhib.tests.integration;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.text.IsEmptyString;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.sprhib.init.BaseTestConfig;
import com.sprhib.init.WebAppConfig;
import com.sprhib.model.Organization;
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
@DatabaseSetup(ITOrganizationTest.DB_INIT)
public class ITOrganizationTest {
  protected static final String PATH_XML_DATA = TestConstants.PATH_XML_DATA_PREFIX + OrganizationController.PATH;
  protected static final String DB_INIT = PATH_XML_DATA + TestConstants.PATH_XML_DATA;
  protected static final String DB_ADDED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_ADDED;
  protected static final String DB_UPDATED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_UPDATED;
  protected static final String DB_DELETED = PATH_XML_DATA + TestConstants.PATH_XML_DATA_DELETED;

  private static final Integer ID_VALUE_DB1 = 1;
  private static final Integer ID_VALUE_DB2 = 2;
  private static final Integer ID_VALUE_ADDED = 3;
  private static final Integer ID_VALUE_INVALID = 4;
  private static final String NAME_VALUE_DB1 = "org1";
  private static final String NAME_VALUE_DB2 = "org2";
  private static final String NAME_VALUE_ADDED = "org3";
  private static final String NAME_VALUE_INVALID = "invalid_name";
  private static final String NAME_VALUE_EDIT = "name2";

  private static Organization organizationViev;
  
  private MockMvc mockMvc;
  
  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private MessageSource messageSource;
  @Autowired
  private SessionFactory sessionFactory;

  @BeforeClass
  public static void initObjects() {
    organizationViev = new Organization();
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
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPageGet() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_ADD))
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, allOf(
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, nullValue()),
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, IsEmptyString.isEmptyOrNullString())
    )))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationViev)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(OrganizationController.VIEW_MODEL_ORGANIZATION, OrganizationController.MODEL_ATTRIBUTE_NAME));
    
  }
  
  @Test
  @ExpectedDatabase(value = DB_ADDED, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testAddPagePost_Success() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_ADDED)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationViev)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, is(ID_VALUE_ADDED)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(getMessage(OrganizationController.MESSAGE_KEY_ADD_SUCCESS))))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testListPage_Success() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(OrganizationController.VIEW_LIST))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATIONS, hasSize(2)))
      .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATIONS, hasItem(
          allOf(
                  hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
                  hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1))
          )
      )))
      .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATIONS, hasItem(
          allOf(
              hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB2)),
              hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB2))
          )
      )))
      ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_InvalidId() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
    ;
  }

  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPageGet_Success() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, allOf(
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ID_VALUE_DB1)),
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE_DB1))
    )))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationViev)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(OrganizationController.VIEW_MODEL_ORGANIZATION, OrganizationController.MODEL_ATTRIBUTE_NAME));
  }
  
  @Test
  @ExpectedDatabase(value = DB_UPDATED, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void testEditPagePost_Success() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE_DB1)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationViev)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(getMessage(OrganizationController.MESSAGE_KEY_EDIT_SUCCESS))));
  }
  
  @Test
  @ExpectedDatabase(value = DB_INIT, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void deleteEditPageGet_InvalidId() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_DELETE + "/" + ID_VALUE_INVALID))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
    ;
  }
  
  @Test
  @ExpectedDatabase(value = DB_DELETED, table = Constants.DB_TABLE_ORGANIZATION, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void deleteEditPageGet_Success() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_DELETE + "/" + ID_VALUE_DB1))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(getMessage(OrganizationController.MESSAGE_KEY_DELETE_SUCCESS))))
    ;
  }
  
  private String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

}
