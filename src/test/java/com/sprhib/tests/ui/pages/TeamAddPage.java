package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.sprhib.model.Team;
import com.sprhib.utilities.Constants;

public class TeamAddPage extends ASprHibPage {

  public TeamAddPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_ADD_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_ADD_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_ADD_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_ORGANIZATION) + ":", driver.findElement(By.cssSelector("td")).getText());
    assertTrue(isElementPresent(By.id("organization")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME) + ":", driver.findElement(By.xpath("//form[@id='team']/table/tbody/tr[2]/td")).getText());
    assertEquals("", driver.findElement(By.id("name")).getAttribute("value"));
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_RATING) + ":", driver.findElement(By.xpath("//form[@id='team']/table/tbody/tr[3]/td")).getText());
    assertEquals("", driver.findElement(By.id("rating")).getAttribute("value"));
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTION_ADD), driver.findElement(By.cssSelector("input[type=\"submit\"]")).getAttribute("value"));
  }

  public HomePage addTeam(Team team) {
    assertNotNull(team);
    new Select(driver.findElement(By.id("organization"))).selectByVisibleText(team.getOrganization().getName());
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(team.getName());
    driver.findElement(By.id("rating")).clear();
    driver.findElement(By.id("rating")).sendKeys(team.getRating().toString());
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    return new HomePage(driver);
  }
  
  public TeamAddPage addTeamInvalid(Team team) {
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
