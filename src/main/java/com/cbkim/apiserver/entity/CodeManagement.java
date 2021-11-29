package com.cbkim.apiserver.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "code_management",
    // 인텍싱 설정
    indexes = {
        @Index(columnList = "code")
    }
)
public class CodeManagement extends AuditorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long idx;

    // ROOT: ROOT01
    @Column(name = "code", nullable = false, unique = true, columnDefinition = "varchar(10) comment '코드'")
    private String code;
    
    @Column(name = "code_value", columnDefinition = "varchar(1024) comment '코드값'")
    private String codeValue;
    
    // ROOT: ROOT00
    @Column(name = "parent_code", nullable = false, columnDefinition = "varchar(10) comment '부모코드'")
    private String parentCode;

    // ROOT 코드
    @Column(name = "description", columnDefinition = "varchar(1024) comment '코드설명'")
    private String description;
}
