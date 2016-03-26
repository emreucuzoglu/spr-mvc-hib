package com.sprhib.tests.ui;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.MemberController;
import com.sprhib.model.Member;
import com.sprhib.tests.TestConstants;
import com.sprhib.tests.ui.pages.ASprHibPage;
import com.sprhib.tests.ui.pages.HomePage;
import com.sprhib.tests.ui.pages.MemberAddPage;
import com.sprhib.tests.ui.pages.MemberEditPage;
import com.sprhib.utilities.Constants;

@Category(IUITest.class)
public class MemberTest {
  private static final int ROW_FIRST = 1;
  private static final String NAME_VALUE = "name";
  private static final String NAME_VALUE_EDIT = "name edit";
  private static final String NAME_VALUE_INVALID = "name_invalid";
  
  private WebDriver driver;
  
  public static Member createMember(String name) {
     Member member = new Member();
     member.setName(name);

     return member;
  }

  public static MemberAddPage addMemberAndTeam(WebDriver driver, ASprHibPage page, Member member){
     if (page == null) {
        page = new HomePage(driver);
     }
     
     TeamTest.addTeam(driver, page, TeamTest.createTeam(member.getName()));
     return addMember(driver, page, member);
  }
  
  public static MemberAddPage addMember(WebDriver driver, ASprHibPage page, Member member){
     if (page == null) {
        page = new HomePage(driver);
     }
     
     page.memberClicked();
     MemberAddPage memberAddPage = page.memberAddClicked();
     memberAddPage.addMember(member);
     assertEquals(page.getMessage(MemberController.MESSAGE_KEY_ADD_SUCCESS), driver.findElement(By.id("home_message")).getText());
     return memberAddPage;
  }
  
  public static void deleteMember(WebDriver driver, ASprHibPage page, int rowNumber){
     if (page == null) {
        page = new HomePage(driver);
     }
     
     page.memberClicked();
     page = page.memberListClicked().memberDeleteClicked(rowNumber);
     assertEquals(page.getMessage(MemberController.MESSAGE_KEY_DELETE_SUCCESS), driver.findElement(By.id("home_message")).getText());
  }
  
  @Before
  public void setUp() throws Exception {
    driver = DriverFactory.buildDefaultDriver();
    driver.manage().timeouts().implicitlyWait(TestConstants.SELENIUM_WAIT_TIME, TimeUnit.SECONDS);
  }

  @Test
  public void testMemberWithTeam_Success() throws Exception {
    driver.get(TestConstants.SELENIUM_BASE_URL);
    HomePage homePage = new HomePage(driver);
    
    Member member = createMember(NAME_VALUE);
    member.setTeams(new HashSet<>());
    member.getTeams().add(TeamTest.createTeam(NAME_VALUE));
    
    addMemberAndTeam(driver, homePage, member);
    
    // unselect team
    member = createMember(NAME_VALUE_EDIT);

    homePage.memberClicked();
    MemberEditPage memberEditPage = homePage.memberListClicked().memberEditClicked(ROW_FIRST);
    homePage = memberEditPage.editMember(member);
    assertEquals(homePage.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS), driver.findElement(By.id("home_message")).getText());

    homePage.memberClicked();
    homePage = homePage.memberListClicked().memberDeleteClicked(ROW_FIRST);
    assertEquals(homePage.getMessage(MemberController.MESSAGE_KEY_DELETE_SUCCESS), driver.findElement(By.id("home_message")).getText());
    
    TeamTest.deleteTeam(driver, homePage, ROW_FIRST);
    OrganizationTest.deleteOrganization(driver, homePage, ROW_FIRST);
  }
  
  @Test
  public void testMemberWithoutTeam_Success() throws Exception {
    driver.get(TestConstants.SELENIUM_BASE_URL);
    HomePage homePage = new HomePage(driver);
    
    addMember(driver, homePage, createMember(NAME_VALUE));
    
    homePage.memberClicked();
    MemberEditPage memberEditPage = homePage.memberListClicked().memberEditClicked(ROW_FIRST);
    homePage = memberEditPage.editMember(createMember(NAME_VALUE_EDIT));
    assertEquals(homePage.getMessage(MemberController.MESSAGE_KEY_EDIT_SUCCESS), driver.findElement(By.id("home_message")).getText());

    homePage.memberClicked();
    homePage = homePage.memberListClicked().memberDeleteClicked(ROW_FIRST);
    assertEquals(homePage.getMessage(MemberController.MESSAGE_KEY_DELETE_SUCCESS), driver.findElement(By.id("home_message")).getText());
  }

  @Test
  public void testMemberAdd_InvalidName() throws Exception {
    driver.get(TestConstants.SELENIUM_BASE_URL);
    HomePage homePage = new HomePage(driver);
    
    homePage.memberClicked();
    MemberAddPage memberAddPage = homePage.memberAddClicked();
    memberAddPage = memberAddPage.addMemberInvalid(createMember(NAME_VALUE_INVALID));
    assertEquals(memberAddPage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
  }

  @Test
  public void testMemberEdit_InvalidName() throws Exception {
    driver.get(TestConstants.SELENIUM_BASE_URL);
    HomePage homePage = new HomePage(driver);
    
    addMember(driver, homePage, createMember(NAME_VALUE));

    homePage.memberClicked();
    MemberEditPage memberEditPage = homePage.memberListClicked().memberEditClicked(ROW_FIRST);
    memberEditPage = memberEditPage.editMemberInvalid(createMember(NAME_VALUE_INVALID));
    assertEquals(homePage.getMessage(Constants.MESSAGE_KEY_NAME_NOT_VALID), driver.findElement(By.id("name.errors")).getText());
    
    deleteMember(driver, memberEditPage, ROW_FIRST);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }
  
  
}