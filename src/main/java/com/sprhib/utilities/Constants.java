package com.sprhib.utilities;

import org.springframework.stereotype.Component;

@Component
public final class Constants {
  public static final String PATTERN_NAME = "(([A-Za-z0-9]+)( )?)+";

  public static final String DB_TABLE_ORGANIZATION = "organizations";
  public static final String DB_TABLE_TEAM = "teams";
  public static final String DB_TABLE_MEMBER = "members";

  // Message codes
  // Generic
  public static final String MESSAGE_KEY_RECORD_NOT_FOUND = "000.001.001";
  // Fields
  public static final String MESSAGE_KEY_NAME_REQUIRED = "000.002.001";
  public static final String MESSAGE_KEY_NAME_NOT_VALID = "000.002.002";
  public static final String MESSAGE_KEY_ORGANIZATION_REQUIRED = "000.003.001";
  public static final String MESSAGE_KEY_RATING_REQUIRED = "000.004.001";
  // Actions
  public static final String MESSAGE_KEY_ACTIONS = "actions";
  public static final String MESSAGE_KEY_ACTION_ADD = "action.add";
  public static final String MESSAGE_KEY_ACTION_EDIT = "action.edit";
  public static final String MESSAGE_KEY_ACTION_DELETE = "action.delete";
  // Fields
  public static final String MESSAGE_KEY_FIELD_ID = "field.id";
  public static final String MESSAGE_KEY_FIELD_NAME = "field.name";
  public static final String MESSAGE_KEY_FIELD_TEAMS = "field.teams";
  public static final String MESSAGE_KEY_FIELD_ORGANIZATION = "field.organization";
  public static final String MESSAGE_KEY_FIELD_RATING = "field.rating";
  // Header
  public static final String MESSAGE_KEY_HEADER_MENU = "header.menu";
  public static final String MESSAGE_KEY_HEADER_MENU_ADD = "header.menu.add";
  public static final String MESSAGE_KEY_HEADER_MENU_LIST = "header.menu.list";
  public static final String MESSAGE_KEY_HEADER_MENU_ORGANIZATION = "header.menu.organization";
  public static final String MESSAGE_KEY_HEADER_MENU_TEAM = "header.menu.team";
  public static final String MESSAGE_KEY_HEADER_MENU_MEMBERS = "header.menu.member";
  // Footer
  public static final String MESSAGE_KEY_FOOTER_BACK = "footer.back";
  // Home
  public static final String MESSAGE_KEY_HOME_TITLE = "home.title";
  public static final String MESSAGE_KEY_HOME_HEADER = "home.header";
  // Organization
  public static final String MESSAGE_KEY_ORGANIZATION_ADD_TITLE = "org.add.title";
  public static final String MESSAGE_KEY_ORGANIZATION_ADD_HEADER = "org.add.header";
  public static final String MESSAGE_KEY_ORGANIZATION_ADD_INTRO = "org.add.intro";

  public static final String MESSAGE_KEY_ORGANIZATION_EDIT_TITLE = "org.edit.title";
  public static final String MESSAGE_KEY_ORGANIZATION_EDIT_HEADER = "org.edit.header";
  public static final String MESSAGE_KEY_ORGANIZATION_EDIT_INTRO = "org.edit.intro";

  public static final String MESSAGE_KEY_ORGANIZATION_LIST_TITLE = "org.list.title";
  public static final String MESSAGE_KEY_ORGANIZATION_LIST_HEADER = "org.list.header";
  public static final String MESSAGE_KEY_ORGANIZATION_LIST_INTRO = "org.list.intro";
  // Team
  public static final String MESSAGE_KEY_TEAM_ADD_TITLE = "team.add.title";
  public static final String MESSAGE_KEY_TEAM_ADD_HEADER = "team.add.header";
  public static final String MESSAGE_KEY_TEAM_ADD_INTRO = "team.add.intro";

  public static final String MESSAGE_KEY_TEAM_EDIT_TITLE = "team.edit.title";
  public static final String MESSAGE_KEY_TEAM_EDIT_HEADER = "team.edit.header";
  public static final String MESSAGE_KEY_TEAM_EDIT_INTRO = "team.edit.intro";

  public static final String MESSAGE_KEY_TEAM_LIST_TITLE = "team.list.title";
  public static final String MESSAGE_KEY_TEAM_LIST_HEADER = "team.list.header";
  public static final String MESSAGE_KEY_TEAM_LIST_INTRO = "team.list.intro";
  // Member
  public static final String MESSAGE_KEY_MEMBER_ADD_TITLE = "member.add.title";
  public static final String MESSAGE_KEY_MEMBER_ADD_HEADER = "member.add.header";
  public static final String MESSAGE_KEY_MEMBER_ADD_INTRO = "member.add.intro";

  public static final String MESSAGE_KEY_MEMBER_EDIT_TITLE = "member.edit.title";
  public static final String MESSAGE_KEY_MEMBER_EDIT_HEADER = "member.edit.header";
  public static final String MESSAGE_KEY_MEMBER_EDIT_INTRO = "member.edit.intro";

  public static final String MESSAGE_KEY_MEMBER_LIST_TITLE = "member.list.title";
  public static final String MESSAGE_KEY_MEMBER_LIST_HEADER = "member.list.header";
  public static final String MESSAGE_KEY_MEMBER_LIST_INTRO = "member.list.intro";

  private Constants() {

  }

}
