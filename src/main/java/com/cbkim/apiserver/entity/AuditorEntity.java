package com.cbkim.apiserver.entity;

import lombok.Getter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Auditing: 엔티티의 변경 시점에 언제, 누가 변경했는지에 대한 정보를 기록하는 기능
@Getter
@MappedSuperclass 
@EntityListeners(value = { AuditingEntityListener.class })
public class AuditorEntity {
    @CreatedDate
    @Column(name = "create_dt", nullable = false, updatable = false, columnDefinition = "datetime comment '생성일자'")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_dt", nullable = false, columnDefinition = "datetime comment '수정일자'")
    private LocalDateTime updateDate;

    @CreatedBy
    @Column(name = "create_by", updatable = false, columnDefinition = "varchar(100) comment '생성자'")    
    private String createBy;
 
    @LastModifiedBy
    @Column(name = "update_by", columnDefinition = "varchar(100) comment '수정자'")    
    private String updateBy;
}
