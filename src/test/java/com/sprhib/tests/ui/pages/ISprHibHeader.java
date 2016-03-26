package com.sprhib.tests.ui.pages;

public interface ISprHibHeader {

   /**
    * @return
    */
   public HomePage homeClicked();

   /**
    * @return
    */
   public HomePage logoClicked();

   /**
    * @return
    */
   public void organizationClicked();

   /**
    * @return
    */
   public OrganizationAddPage organizationAddClicked();

   /**
    * @return
    */
   public OrganizationListPage organizationListClicked();

   /**
    * 
    */
   public void teamClicked();

   /**
    * @return
    */
   public TeamAddPage teamAddClicked();

   /**
    * @return
    */
   public TeamListPage teamListClicked();

   /**
    * 
    */
   public void memberClicked();

   /**
    * @return
    */
   public MemberAddPage memberAddClicked();

   /**
    * @return
    */
   public MemberListPage memberListClicked();

   /**
   * 
   */
   public void languageUsClicked();

   /**
   * 
   */
   public void languageGbClicked();

}
