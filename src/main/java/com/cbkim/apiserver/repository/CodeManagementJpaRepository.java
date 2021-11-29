package com.cbkim.apiserver.repository;

import com.cbkim.apiserver.entity.CodeManagement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeManagementJpaRepository extends JpaRepository<CodeManagement, Long>{
    CodeManagement findByCode(String code);
}
