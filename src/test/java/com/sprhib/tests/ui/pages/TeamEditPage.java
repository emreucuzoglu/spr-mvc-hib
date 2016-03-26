package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.sprhib.model.Team;
import com.sprhib.utilities.Constants;

public class TeamEditPage extends ASprHibPage {

  public TeamEditPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_EDIT_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_EDIT_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_EDIT_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_ORGANIZATION) + ":", driver.findElement(By.cssSelector("td")).getText());
    assertTrue(isElementPresent(By.id("organization")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME) + ":", driver.findElement(By.xpath("//form[@id='team']/table/tbody/tr[2]/td")).getText());
    assertTrue(isElementPresent(By.id("name")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_RATING) + ":", driver.findElement(By.xpath("//form[@id='team']/table/tbody/tr[3]/td")).getText());
    assertTrue(isElementPresent(By.id("rating")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTION_EDIT), driver.findElement(By.cssSelector("input[type=\"submit\"]")).getAttribute("value"));
  }

  public HomePage editTeam(Team team) {
    assertNotNull(team);
    new Select(driver.findElement(By.id("organization"))).selectByVisibleText(team.getOrganization().getName());
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(team.getName());
    driver.findElement(By.id("rating")).clear();
    driver.findElement(By.id("rating")).sendKeys(team.getRating().toString());
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    return new HomePage(driver);
  }
  
  public TeamEditPage editTeamInvalid(Team team) {
     assertNotNull(team);
     new Select(driver.findElement(By.id("organization"))).selectByVisibleText(team.getOrganization().getName());
     driver.findElement(By.id("name")).clear();
     driver.findElement(By.id("name")).sendKeys(team.getName());
     driver.findElement(By.id("rating")).clear();
     driver.findElement(By.id("rating")).sendKeys(team.getRating().toString());
     driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
     return this;
   }
}
