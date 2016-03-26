package com.sprhib.tests.ui;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.OrganizationController;
import com.sprhib.model.Organization;
import com.sprhib.tests.TestConstants;
import com.sprhib.tests.ui.pages.ASprHibPage;
import com.sprhib.tests.ui.pages.HomePage;
import com.sprhib.tests.ui.pages.OrganizationAddPage;
import com.sprhib.tests.ui.pages.OrganizationEditPage;
import com.sprhib.utilities.Constants;

@Category(IUITest.class)
public class OrganizationTest {
   private static final int ROW_FIRST = 1;
   private static final String NAME_VALUE = "name";
   private static final String NAME_VALUE_EDIT = "name edit";
   private static final String NAME_VALUE_INVALID = "name_invalid";
   
   private WebDriver driver;
   
   public static Organization createOrganization(String name) {
      Organization organization = new Organization();
      organization.setName(name);
      return organization;
   }
   
   public static OrganizationAddPage addOrganization(WebDriver driver, ASprHibPage page, Organization organization){
      if (page == null) {
         page = new HomePage(driver);
      }
      
      page.organizationClicked();
      OrganizationAddPage orgAddPage = page.organizationAddClicked();
      orgAddPage.addOrganization(organization);
      assertEquals(page.getMessage(OrganizationController.MESSAGE_KEY_ADD_SUCCESS), driver.findElement(By.id("home_message")).getText());
      return orgAddPage;
   }
   
   public static void deleteOrganization(WebDriver driver, ASprHibPage page, int rowNumber){
      if (page == null) {
         page = new HomePage(driver);
      }
      
      page.organizationClicked();
      page = page.organizationListClicked().organizationDeleteClicked(rowNumber);
      assertEquals(page.getMessage(OrganizationController.MESSAGE_KEY_DELETE_SUCCESS), driver.findElement(By.id("home_message")).getText());
   }
   
   @Before
   public void setUp() throws Exception {
      driver = DriverFactory.buildDefaultDriver();
      driver.manage().timeouts().implicitlyWait(TestConstants.SELENIUM_WAIT_TIME, TimeUnit.SECONDS);
   }
   
   @Test
   public void testOrganization_Success() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);
      
      addOrganization(driver, homePage, createOrganization(NAME_VALUE));
      
      homePage.organizationClicked();
      OrganizationEditPage orgEditPage = homePage.organizationListClicked().organizationEditClicked(ROW_FIRST);
      homePage = orgEditPage.editOrganization(createOrganization(NAME_VALUE_EDIT));
      assertEquals(homePage.getMessage(OrganizationController.MESSAGE_KEY_EDIT_SUCCESS), driver.findElement(By.id("home_message")).getText());

      deleteOrganization(driver, homePage, ROW_FIRST);
   }

   @Test
   public void testOrganizationAdd_InvalidName() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);
      
      homePage.organizationClicked();
      OrganizationAddPage orgAddPage = homePage.organizationAddClicked();
      orgAddPage.addOrganizationInvalid(createOrganization(NAME_VALUE_INVALID));
      assertEquals(orgAddPage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
   }

   @Test
   public void testOrganizationEdit_InvalidName() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);

      addOrganization(driver, homePage, createOrganization(NAME_VALUE));

      homePage.organizationClicked();
      OrganizationEditPage orgEditPage = homePage.organizationListClicked().organizationEditClicked(ROW_FIRST);
      orgEditPage.editOrganizationInvalid(createOrganization(NAME_VALUE_INVALID));
      assertEquals(orgEditPage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
      
      deleteOrganization(driver, orgEditPage, ROW_FIRST);
   }

   @After
   public void tearDown() throws Exception {
      driver.quit();
   }
   
}