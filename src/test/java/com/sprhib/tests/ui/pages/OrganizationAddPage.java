package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.controller.OrganizationController;
import com.sprhib.model.Organization;
import com.sprhib.utilities.Constants;

public class OrganizationAddPage extends ASprHibPage {

  public OrganizationAddPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_ADD_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_ADD_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_ORGANIZATION_ADD_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME) + ":", driver.findElement(By.id("org_add_name_txt")).getText());
    assertEquals("", driver.findElement(By.id("name")).getAttribute("value"));
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTION_ADD), driver.findElement(By.cssSelector("input[type=\"submit\"]")).getAttribute("value"));
    assertTrue(driver.getCurrentUrl().endsWith(OrganizationController.PATH + OrganizationController.PATH_ADD));
    
  }

  public HomePage addOrganization(Organization organization) {
    assertNotNull(organization);
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(organization.getName());
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    
    return new HomePage(driver);
  }
  
  public OrganizationAddPage addOrganizationInvalid(Organization organization) {
     assertNotNull(organization);
     driver.findElement(By.id("name")).clear();
     driver.findElement(By.id("name")).sendKeys(organization.getName());
     driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
     
     return this;
   }

}
