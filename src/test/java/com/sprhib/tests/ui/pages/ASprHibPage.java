package com.sprhib.tests.ui.pages;

import org.openqa.selenium.WebDriver;

public abstract class ASprHibPage extends APageObject implements ISprHibHeader, ISprHibFooter {

   private HeaderPage header;
   private FooterPage footer;

   public ASprHibPage(WebDriver driver) {
      super(driver);

      this.header = new HeaderPage(driver);
      this.footer = new FooterPage(driver);
   }

   @Override
   public HomePage homeClicked() {
      return header.homeClicked();
   }

   @Override
   public HomePage logoClicked() {
      return header.logoClicked();
   }

   @Override
   public void organizationClicked() {
      header.organizationClicked();
   }

   @Override
   public OrganizationAddPage organizationAddClicked() {
      return header.organizationAddClicked();
   }

   @Override
   public OrganizationListPage organizationListClicked() {
      return header.organizationListClicked();
   }
   
   @Override
   public void teamClicked() {
      header.teamClicked();
   }

   @Override
   public TeamAddPage teamAddClicked() {
      return header.teamAddClicked();
   }

   @Override
   public TeamListPage teamListClicked() {
      return header.teamListClicked();
   }
   
   @Override
   public void memberClicked() {
      header.memberClicked();
   }

   @Override
   public MemberAddPage memberAddClicked() {
      return header.memberAddClicked();
   }

   @Override
   public MemberListPage memberListClicked() {
      return header.memberListClicked();
   }

   @Override
   public void languageUsClicked() {
      header.languageUsClicked();
   }

   @Override
   public void languageGbClicked() {
      header.languageGbClicked();
   }

}
