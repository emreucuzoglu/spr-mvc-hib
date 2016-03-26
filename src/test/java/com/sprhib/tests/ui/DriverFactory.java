package com.sprhib.tests.ui;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {

   public static RemoteWebDriver buildDefaultDriver() {
      return buildFirefoxDriver();
   }

   public static ChromeDriver buildChromeDriver() {
      return new ChromeDriver();
   }

   public static EdgeDriver buildEdgeDriver() {
      return new EdgeDriver();
   }

   public static FirefoxDriver buildFirefoxDriver() {
      return new FirefoxDriver();
   }

   public static InternetExplorerDriver buildInternetExplorerDriver() {
      return new InternetExplorerDriver();
   }

   public static MarionetteDriver buildMarionetteDriver() {
      return new MarionetteDriver();
   }

   public static OperaDriver buildOperaDriver() {
      return new OperaDriver();
   }

   public static SafariDriver buildSafariDriver() {
      return new SafariDriver();
   }

}
