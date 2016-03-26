package com.sprhib.tests.ui;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.LinkController;
import com.sprhib.tests.TestConstants;
import com.sprhib.tests.ui.pages.HomePage;

@Category(IUITest.class)
public class HomePageTest {
  private static final String PATH = TestConstants.SELENIUM_BASE_URL + LinkController.PATH;
  private WebDriver driver;

  @Before
  public void setUp() throws Exception {
    driver = DriverFactory.buildDefaultDriver();
    driver.manage().timeouts().implicitlyWait(TestConstants.SELENIUM_WAIT_TIME, TimeUnit.SECONDS);
  }
  
  @Test
  public void testLinks_Success() throws Exception {
    driver.get(PATH);
    HomePage page = new HomePage(driver);
    page.logoClicked();
    page.homeClicked();
    page.organizationClicked();
    page.organizationAddClicked();
    page.organizationClicked();
    page.organizationListClicked();
    page.teamClicked();
    page.teamAddClicked();
    page.teamClicked();
    page.teamListClicked();
    page.memberClicked();
    page.memberAddClicked();
    page.memberClicked();
    page.memberListClicked();
    page.languageUsClicked();
    page.languageGbClicked();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }
}