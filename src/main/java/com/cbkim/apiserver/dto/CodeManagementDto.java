package com.cbkim.apiserver.dto;

import com.cbkim.apiserver.entity.CodeManagement;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class CodeManagementDto {
    private long idx;
    private String code;
    private String codeValue;
    private String parentCode;
    private String description;

    public CodeManagement insert()
    {
        CodeManagement codeManagement = new CodeManagement();

        codeManagement.setIdx(this.idx);

        return codeManagement;
    }

    // Entity -> DTO
    public CodeManagementDto(CodeManagement codeManagement)
    {
        if(codeManagement == null) return;
        
        this.idx            = codeManagement.getIdx(); 
        this.code           = codeManagement.getCode(); 
        this.codeValue      = codeManagement.getCodeValue(); 
        this.parentCode     = codeManagement.getParentCode(); 
        this.description    = codeManagement.getDescription(); 
    }  
}
