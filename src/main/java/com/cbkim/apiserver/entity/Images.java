package com.cbkim.apiserver.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Images {
    private String alt;                 // 이미지 툴팁
    private String src;                 // 이미지 경로
    private String originalName;        // 원본 파일 명
    private String titleYn = "N";       // 대표이미지 여부
    private String createDate;          // 생성일자
    private String imageRef;            // 이미지 컴포넌트 ID
}