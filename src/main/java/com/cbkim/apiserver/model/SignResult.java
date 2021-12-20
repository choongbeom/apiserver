package com.cbkim.apiserver.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignResult {
    String token;
    String code;
    String message;
}
