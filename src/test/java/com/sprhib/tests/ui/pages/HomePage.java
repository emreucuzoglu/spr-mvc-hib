package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.sprhib.utilities.Constants;

public class HomePage extends ASprHibPage {

  public HomePage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_HOME_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_HOME_HEADER), driver.findElement(By.cssSelector("h1")).getText());
  }

}
