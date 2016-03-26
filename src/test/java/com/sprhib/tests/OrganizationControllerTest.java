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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sprhib.controller.LinkController;
import com.sprhib.controller.OrganizationController;
import com.sprhib.init.TestConfig;
import com.sprhib.init.WebAppConfig;
import com.sprhib.model.Organization;
import com.sprhib.service.IMessageService;
import com.sprhib.service.IOrganizationService;
import com.sprhib.utilities.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
public class OrganizationControllerTest {
  private static final Integer ID_VALUE = 1;
  private static final String NAME_VALUE = "name";
  private static final String NAME_VALUE_INVALID = "invalid_name";
  private static final String NAME_VALUE_EDIT = "name2";

  private static Organization organizationView;
  private static Organization organizationDB;
  private static Organization organizationEdit;
  private static List<Organization> organizations;
  
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private IOrganizationService organizationService;
  @Autowired
  private IMessageService messageService;

  @BeforeClass
  public static void initObjects() {
    organizationView = new Organization();

    organizationDB = new Organization();
    organizationDB.setId(ID_VALUE);
    organizationDB.setName(NAME_VALUE);

    organizationEdit = new Organization();
    organizationEdit.setId(ID_VALUE);
    organizationEdit.setName(NAME_VALUE_EDIT);

    organizations = new ArrayList<>();
    organizations.add(organizationDB);
  }

  @Before
  public void init() {
    Mockito.reset(organizationService);
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void testAddPageGet() throws Exception {
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_ADD))
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, allOf(
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, nullValue()),
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, IsEmptyString.isEmptyOrNullString())
    )));
    
    verifyZeroInteractions(organizationService);
  }
  
  @Test
  public void testAddPagePost_Exception() throws Exception {
    when(organizationService.addOrganization(isA(Organization.class))).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
    ;
    
    ArgumentCaptor<Organization> formObjectArgument = ArgumentCaptor.forClass(Organization.class);
    verify(organizationService, times(1)).addOrganization(formObjectArgument.capture());
    verifyNoMoreInteractions(organizationService);
  }
  
  @Test
  public void testAddPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_ADD))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_ADD + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(OrganizationController.VIEW_MODEL_ORGANIZATION, OrganizationController.MODEL_ATTRIBUTE_NAME));

    verifyNoMoreInteractions(organizationService);
  }
  
  @Test
  public void testAddPagePost_Success() throws Exception {
    when(organizationService.addOrganization(isA(Organization.class))).thenReturn(organizationDB);
    
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_ADD)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationView)
       )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, is(ID_VALUE)))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(OrganizationController.MESSAGE_KEY_ADD_SUCCESS))));

    ArgumentCaptor<Organization> formObjectArgument = ArgumentCaptor.forClass(Organization.class);
    verify(organizationService, times(1)).addOrganization(formObjectArgument.capture());
    verifyNoMoreInteractions(organizationService);

    Organization formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE));
  }

  @Test
  public void testListPage_Exception() throws Exception {
    when(organizationService.getOrganizations()).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(LinkController.VIEW_HOME))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
      ;
    
    verify(organizationService, times(1)).getOrganizations();
    verifyNoMoreInteractions(organizationService);
  }
  
  @Test
  public void testListPage_Success() throws Exception {
    when(organizationService.getOrganizations()).thenReturn(organizations);
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_LIST))
      .andExpect(status().isOk())
      .andExpect(view().name(OrganizationController.VIEW_LIST))
      .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_LIST + WebAppConfig.VIEW_SUFFIX))
      .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATIONS, hasSize(1)))
      .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATIONS, IsIterableContainingInOrder.contains(organizations.toArray())))
      ;
    
    verify(organizationService, times(1)).getOrganizations();
    verifyNoMoreInteractions(organizationService);
  }
  
  @Test
  public void testEditPageGet_Exception() throws Exception {
    when(organizationService.getOrganization(ID_VALUE)).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
    ;
    verify(organizationService, times(1)).getOrganization(ID_VALUE);
    verifyZeroInteractions(organizationService);
  }
  
  @Test
  public void testEditPageGet_InvalidId() throws Exception {
    when(organizationService.getOrganization(ID_VALUE)).thenThrow(new RuntimeException(messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)));
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, messageService.getMessage(Constants.MESSAGE_KEY_RECORD_NOT_FOUND, ID_VALUE)))
    ;
    verify(organizationService, times(1)).getOrganization(ID_VALUE);
    verifyZeroInteractions(organizationService);
  }

  @Test
  public void testEditPageGet_Success() throws Exception {
    when(organizationService.getOrganization(ID_VALUE)).thenReturn(organizationDB);
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(OrganizationController.VIEW_MODEL_ORGANIZATION, allOf(
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_ID, is(ID_VALUE)),
        hasProperty(OrganizationController.MODEL_ATTRIBUTE_NAME, is(NAME_VALUE))
    )))
    ;
    verify(organizationService, times(1)).getOrganization(ID_VALUE);
    verifyZeroInteractions(organizationService);
  }
  
  @Test
  public void testEditPagePost_InvalidName() throws Exception {
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_INVALID)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationView)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(OrganizationController.VIEW_EDIT))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + OrganizationController.VIEW_EDIT + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attributeHasFieldErrors(OrganizationController.VIEW_MODEL_ORGANIZATION, OrganizationController.MODEL_ATTRIBUTE_NAME));
    
    verifyNoMoreInteractions(organizationService);
  }
  
  @Test
  public void testEditPagePost_Success() throws Exception {
    when(organizationService.updateOrganization(isA(Organization.class))).thenReturn(organizationEdit);
    
    mockMvc.perform(post(OrganizationController.PATH + OrganizationController.PATH_EDIT + "/" + ID_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param(OrganizationController.MODEL_ATTRIBUTE_NAME, NAME_VALUE_EDIT)
        .sessionAttr(OrganizationController.VIEW_MODEL_ORGANIZATION, organizationView)
        )
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME + WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(OrganizationController.MESSAGE_KEY_EDIT_SUCCESS))));
    
    ArgumentCaptor<Organization> formObjectArgument = ArgumentCaptor.forClass(Organization.class);
    verify(organizationService, times(1)).updateOrganization(formObjectArgument.capture());
    verifyNoMoreInteractions(organizationService);

    Organization formObject = formObjectArgument.getValue();
    assertThat(formObject.getName(), is(NAME_VALUE_EDIT));
  }
  
  @Test
  public void deleteEditPageGet_InvalidId() throws Exception {
    when(organizationService.deleteOrganization(ID_VALUE)).thenThrow(new RuntimeException("e"));
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_DELETE + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, notNullValue()))
    ;
    verify(organizationService, times(1)).deleteOrganization(ID_VALUE);
    verifyZeroInteractions(organizationService);
  }
  
  @Test
  public void deleteEditPageGet_Success() throws Exception {
    when(organizationService.deleteOrganization(ID_VALUE)).thenReturn(organizationDB);
    
    mockMvc.perform(get(OrganizationController.PATH + OrganizationController.PATH_DELETE + "/" + ID_VALUE))
    .andExpect(status().isOk())
    .andExpect(view().name(LinkController.VIEW_HOME))
    .andExpect(forwardedUrl(WebAppConfig.VIEW_PATH + LinkController.VIEW_HOME+ WebAppConfig.VIEW_SUFFIX))
    .andExpect(model().attribute(LinkController.VIEW_ATTRIBUTE_MESSAGE, is(messageService.getMessage(OrganizationController.MESSAGE_KEY_DELETE_SUCCESS))))
    ;
    verify(organizationService, times(1)).deleteOrganization(ID_VALUE);
    verifyZeroInteractions(organizationService);
  }
}
