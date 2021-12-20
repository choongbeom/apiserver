package com.cbkim.apiserver.model.sns;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverProfileApiResponse {

  private String id;
  private String email;
  private String nickname;
  private String profile_image;
  private String age;
  private String gender;
  private String mobile;
  private String name;
  private String birthday;
  private String birthyear;
}