package com.cbkim.apiserver.model.sns;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KaKaoProfileApiResponse {

  private String has_email;
  private String email_needs_agreement;
  private String is_email_valid;
  private String is_email_verified;
  private String email;

}