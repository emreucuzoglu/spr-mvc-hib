package com.sprhib.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprhib.model.Member;

@Service
@Transactional
public class MemberServiceImpl extends ABaseService implements IMemberService {

  public Member addMember(Member member) {
    baseDao.create(member);
    return member;
  }

  public Member updateMember(Member member) {
    baseDao.update(member);
    return member;
  }

  public Member getMember(int id) {
    return baseDao.read(Member.class, id);
  }

  public Member deleteMember(int id) {
    return baseDao.delete(Member.class, id);
  }

  public List<Member> getMembers() {
    return baseDao.getModels(Member.class);
  }

}
