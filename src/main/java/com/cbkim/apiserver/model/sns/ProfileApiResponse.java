package com.cbkim.apiserver.model.sns;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProfileApiResponse {

  private String name;
  private String email;
  private String id;
  private String verified_email;
  private String picture;
  private String hd;

  // kakao
  private KaKaoProfileApiResponse kakao_account;

  // naver
  private String resultcode;
  private String message;
  private NaverProfileApiResponse response;
}
