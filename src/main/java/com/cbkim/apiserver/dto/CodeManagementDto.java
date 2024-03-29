package com.cbkim.apiserver.dto;

import com.cbkim.apiserver.entity.CodeManagement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class CodeManagementDto {

    @ApiModelProperty(value = "Management Code idx")
    private long idx;

    @ApiModelProperty(value = "Management Code")
    private String code;

    @ApiModelProperty(value = "Management Code value")
    private String codeValue;

    @ApiModelProperty(value = "Management Parent Code")
    private String parentCode;

    @ApiModelProperty(value = "Management Code description")
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
