package com.sprhib.tests.ui.pages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.sprhib.init.I18Config;

public class APageObject {
  protected WebDriver driver;
  protected ResourceBundle messages;
  protected MessageFormat formatter = new MessageFormat("");
  protected StringBuffer verificationErrors = new StringBuffer();
  private boolean acceptNextAlert = true;

  public APageObject(WebDriver driver) {
    this.driver = driver;
    changeLocale();
  }

  public String getMessage(String key, Object... args) {
    formatter.applyPattern(messages.getString(key));
    return formatter.format(args);
  }

  protected void changeLocale() {
    String[] tmpArr = null;
    
    try {
       tmpArr = driver.manage().getCookieNamed(I18Config.COOKIE_NAME_LOCALE).getValue().split("_");
   } catch (Exception e) {
      tmpArr = new String[]{"en", "US"};
   }

    Locale newLocale = new Locale(tmpArr[0], tmpArr[1]);
    messages = ResourceBundle.getBundle(I18Config.PATH_MESSAGE_FILE.replaceFirst("classpath:", ""), newLocale);
    formatter.setLocale(newLocale);
  }

  protected boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  protected String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }

  protected boolean checkVerificationErrors() {
    return "".equals(verificationErrors.toString());
  }

}
