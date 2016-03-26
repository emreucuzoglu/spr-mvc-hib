package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.utilities.Constants;

public class TeamListPage extends ASprHibPage {

  public TeamListPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_LIST_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_LIST_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_TEAM_LIST_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_ID), driver.findElement(By.cssSelector("th")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME), driver.findElement(By.xpath("//th[2]")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_RATING), driver.findElement(By.xpath("//th[3]")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_ORGANIZATION), driver.findElement(By.xpath("//th[4]")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTIONS), driver.findElement(By.xpath("//th[5]")).getText());
  }

  public TeamEditPage teamEditClicked(int rowNumber) {
    driver.findElement(By.xpath("(//a[contains(text(),'" + getMessage(Constants.MESSAGE_KEY_ACTION_EDIT) + "')])[" + rowNumber + "]")).click();
    return new TeamEditPage(driver);
  }

  public HomePage teamDeleteClicked(int rowNumber) {
    driver.findElement(By.xpath("(//a[contains(text(),'" + getMessage(Constants.MESSAGE_KEY_ACTION_DELETE) + "')])[" + rowNumber + "]")).click();
    return new HomePage(driver);
  }
}