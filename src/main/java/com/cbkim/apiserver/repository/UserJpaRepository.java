package com.cbkim.apiserver.repository;

import com.cbkim.apiserver.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long>{
    Users findByIdx(long idx);
    Users findByUserId(String userid);
    Users findByUserCode(String userCode);
    Users findByNickName(String nickName);
    Users findByInvitaionCode(String invitaionCode);
}
