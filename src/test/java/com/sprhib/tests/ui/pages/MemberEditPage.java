package com.sprhib.tests.ui.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.sprhib.model.Member;
import com.sprhib.utilities.Constants;

public class MemberEditPage extends ASprHibPage {

  public MemberEditPage(WebDriver driver) {
    super(driver);
    assertEquals(getMessage(Constants.MESSAGE_KEY_MEMBER_EDIT_TITLE), driver.getTitle());
    assertEquals(getMessage(Constants.MESSAGE_KEY_MEMBER_EDIT_HEADER), driver.findElement(By.cssSelector("h1")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_MEMBER_EDIT_INTRO), driver.findElement(By.cssSelector("p")).getText());
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_TEAMS) + ":", driver.findElement(By.cssSelector("td")).getText());
    assertTrue(isElementPresent(By.id("teams")));
    assertEquals(getMessage(Constants.MESSAGE_KEY_FIELD_NAME) + ":", driver.findElement(By.xpath("//form[@id='member']/table/tbody/tr[2]/td")).getText());
    assertNotEquals("", driver.findElement(By.id("name")).getAttribute("value"));
    assertEquals(getMessage(Constants.MESSAGE_KEY_ACTION_EDIT), driver.findElement(By.cssSelector("input[type=\"submit\"]")).getAttribute("value"));
  }
  
  public HomePage editMember(Member member) {
    assertNotNull(member);
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(member.getName());
    
    if (member.getTeams() != null) {
       Select select = new Select(driver.findElement(By.id("teams")));
       select.deselectAll();
       member.getTeams().forEach((v) -> select.selectByVisibleText(v.getName()));  
    }
    
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    
    return new HomePage(driver);
  }
  
  public MemberEditPage editMemberInvalid(Member member) {
     assertNotNull(member);
     driver.findElement(By.id("name")).clear();
     driver.findElement(By.id("name")).sendKeys(member.getName());
     
     if (member.getTeams() != null) {
        Select select = new Select(driver.findElement(By.id("teams")));
        select.deselectAll();
        member.getTeams().forEach((v) -> select.selectByVisibleText(v.getName()));  
     }
     
     driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
     
     return this;
   }

}
