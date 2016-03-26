package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends APageObject implements ISprHibHeader {

  public HeaderPage(WebDriver driver) {
    super(driver);
    assertTrue(isElementPresent(By.id("linkLogo")));
    assertTrue(isElementPresent(By.id("menu_home")));
    assertTrue(isElementPresent(By.id("menu_org")));
    assertTrue(isElementPresent(By.id("menu_org_add")));
    assertTrue(isElementPresent(By.id("menu_org_list")));
    assertTrue(isElementPresent(By.id("menu_team")));
    assertTrue(isElementPresent(By.id("menu_team_add")));
    assertTrue(isElementPresent(By.id("menu_team_list")));
    assertTrue(isElementPresent(By.id("menu_member")));
    assertTrue(isElementPresent(By.id("menu_member_add")));
    assertTrue(isElementPresent(By.id("menu_member_list")));
    assertTrue(isElementPresent(By.id("menu_lang_enUs")));
    assertTrue(isElementPresent(By.id("menu_lang_enGb")));
  }

  @Override
  public HomePage homeClicked() {
    driver.findElement(By.id("menu_home")).click();
    return new HomePage(driver);
  }

  @Override
  public HomePage logoClicked() {
    driver.findElement(By.id("linkLogo")).click();
    return new HomePage(driver);
  }
  
  @Override
   public void organizationClicked() {
     driver.findElement(By.id("menu_org")).click();
   }

  @Override
  public OrganizationAddPage organizationAddClicked() {
    driver.findElement(By.id("menu_org_add")).click();
    return new OrganizationAddPage(driver);
  }

  @Override
  public OrganizationListPage organizationListClicked() {
    driver.findElement(By.id("menu_org_list")).click();
    return new OrganizationListPage(driver);
  }
  
  @Override
  public void teamClicked() {
    driver.findElement(By.id("menu_team")).click();
  }

  @Override
  public TeamAddPage teamAddClicked() {
    driver.findElement(By.id("menu_team_add")).click();
    return new TeamAddPage(driver);
  }

  @Override
  public TeamListPage teamListClicked() {
    driver.findElement(By.id("menu_team_list")).click();
    return new TeamListPage(driver);
  }
  
  @Override
  public void memberClicked() {
    driver.findElement(By.id("menu_member")).click();
  }

  @Override
  public MemberAddPage memberAddClicked() {
    driver.findElement(By.id("menu_member_add")).click();
    return new MemberAddPage(driver);
  }

  @Override
  public MemberListPage memberListClicked() {
    driver.findElement(By.id("menu_member_list")).click();
    return new MemberListPage(driver);
  }

  @Override
  public void languageUsClicked() {
    driver.findElement(By.id("menu_lang_enUs")).click();
    changeLocale();
  }

  @Override
  public void languageGbClicked() {
    driver.findElement(By.id("menu_lang_enGb")).click();
    changeLocale();
  }

}
