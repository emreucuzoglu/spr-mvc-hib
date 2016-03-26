package com.sprhib.service;

import java.util.List;

import com.sprhib.model.Member;

public interface IMemberService {

  /**
   * @param team
   * @return
   */
  public Member addMember(Member team);

  /**
   * @param team
   * @return
   */
  public Member updateMember(Member team);

  /**
   * @param id
   * @return
   */
  public Member getMember(int id);

  /**
   * @param id
   * @return
   */
  public Member deleteMember(int id);

  /**
   * @return
   */
  public List<Member> getMembers();

}
