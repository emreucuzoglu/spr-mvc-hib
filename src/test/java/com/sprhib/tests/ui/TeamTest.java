package com.sprhib.tests.ui;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.TeamController;
import com.sprhib.model.Team;
import com.sprhib.tests.TestConstants;
import com.sprhib.tests.ui.pages.ASprHibPage;
import com.sprhib.tests.ui.pages.HomePage;
import com.sprhib.tests.ui.pages.TeamAddPage;
import com.sprhib.tests.ui.pages.TeamEditPage;
import com.sprhib.utilities.Constants;

@Category(IUITest.class)
public class TeamTest {
   private static final int ROW_FIRST = 1;
   private static final Integer ID_VALUE = new Integer(1);

   private static final String NAME_VALUE = "name";
   private static final String NAME_VALUE_EDIT = "name edit";
   private static final String NAME_VALUE_INVALID = "name_invalid";

   private static final Integer RATING_VALUE = new Integer(1);
   private static final Integer RATING_VALUE_EDIT = new Integer(2);

   private WebDriver driver;
   
   public static Team createTeam(String name){
      Team team = new Team();
      team.setName(name);
      team.setOrganization(OrganizationTest.createOrganization(NAME_VALUE));
      team.getOrganization().setId(ID_VALUE);
      team.setRating(RATING_VALUE);
      
      return team;
   }
   
   public static TeamAddPage addTeam(WebDriver driver, ASprHibPage page, Team team){
      if (page == null) {
         page = new HomePage(driver);
      }
      
      OrganizationTest.addOrganization(driver, page,  OrganizationTest.createOrganization(team.getName()));
      
      page.teamClicked();
      TeamAddPage teamAddPage = page.teamAddClicked();
      teamAddPage.addTeam(team);
      assertEquals(page.getMessage(TeamController.MESSAGE_KEY_ADD_SUCCESS), driver.findElement(By.id("home_message")).getText());
      return teamAddPage;
   }
   
   public static void deleteTeam(WebDriver driver, ASprHibPage page, int rowNumber){
      if (page == null) {
         page = new HomePage(driver);
      }
      
      page.teamClicked();
      page = page.teamListClicked().teamDeleteClicked(rowNumber);
      assertEquals(page.getMessage(TeamController.MESSAGE_KEY_DELETE_SUCCESS), driver.findElement(By.id("home_message")).getText());
   }

   @Before
   public void setUp() throws Exception {
      driver = DriverFactory.buildDefaultDriver();
      driver.manage().timeouts().implicitlyWait(TestConstants.SELENIUM_WAIT_TIME, TimeUnit.SECONDS);
   }

   @Test
   public void testTeam_Success() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);
      
      addTeam(driver, homePage, createTeam(NAME_VALUE));
      
      Team team = createTeam(NAME_VALUE_EDIT);
      team.setRating(RATING_VALUE_EDIT);
      
      homePage.teamClicked();
      TeamEditPage teamEditPage = homePage.teamListClicked().teamEditClicked(ROW_FIRST);
      homePage = teamEditPage.editTeam(team);
      assertEquals(homePage.getMessage(TeamController.MESSAGE_KEY_EDIT_SUCCESS), driver.findElement(By.id("home_message")).getText());
      
      deleteTeam(driver, homePage, ROW_FIRST);
      OrganizationTest.deleteOrganization(driver, teamEditPage, ROW_FIRST);
   }

   @Test
   public void testTeamAdd_InvalidName() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);
      
      OrganizationTest.addOrganization(driver, homePage, OrganizationTest.createOrganization(NAME_VALUE));

      homePage.teamClicked();
      TeamAddPage teamAddPage = homePage.teamAddClicked();
      teamAddPage.addTeamInvalid(createTeam(NAME_VALUE_INVALID));
      assertEquals(teamAddPage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
      
      OrganizationTest.deleteOrganization(driver, teamAddPage, ROW_FIRST);
   }

   @Test
   public void testTeamEdit_InvalidName() throws Exception {
      driver.get(TestConstants.SELENIUM_BASE_URL);
      HomePage homePage = new HomePage(driver);

      TeamAddPage teamAddPage = addTeam(driver, homePage, createTeam(NAME_VALUE));

      Team team = createTeam(NAME_VALUE_INVALID); 
      team.setRating(RATING_VALUE_EDIT);
      
      homePage.teamClicked();
      TeamEditPage teamEditPage = homePage.teamListClicked().teamEditClicked(ROW_FIRST);
      teamEditPage = teamEditPage.editTeamInvalid(team);
      assertEquals(teamAddPage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
      
      OrganizationTest.deleteOrganization(driver, teamEditPage, ROW_FIRST);
   }

   @After
   public void tearDown() throws Exception {
      driver.quit();
   }
   
}