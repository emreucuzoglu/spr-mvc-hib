package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.OrganizationController;
import com.sprhib.model.Organization;
import com.sprhib.tests.TestConstants;
import com.sprhib.utilities.Constants;

public class OrganizationEditPage extends ASprHibPage {

  public OrganizationEditPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_EDIT_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_EDIT_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_EDIT_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME) + ":", driver.findElement(By.id("org_edit_name_txt")).getText());
    assertTrue(isElementPresent(By.id("name")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTION_EDIT), driver.findElement(By.cssSelector("input[type=\"submit\"]")).getAttribute("value"));
    assertTrue(driver.getCurrentUrl().matches(".*" + TestConstants.CHAR_ESCAPE + OrganizationController.PATH + TestConstants.CHAR_ESCAPE 
          + OrganizationController.PATH_EDIT + TestConstants.CHAR_ESCAPE + "/[0-9]+"));
  }

  public HomePage editOrganization(Organization organization) {
    assertNotNull(organization);
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(organization.getName());
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

    return new HomePage(driver);
  }
  
  public OrganizationEditPage editOrganizationInvalid(Organization organization) {
     assertNotNull(organization);
     driver.findElement(By.id("name")).clear();
     driver.findElement(By.id("name")).sendKeys(organization.getName());
     driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

     return this;
   }
}