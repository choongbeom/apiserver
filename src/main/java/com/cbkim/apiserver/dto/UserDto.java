package com.cbkim.apiserver.dto;

import com.cbkim.apiserver.entity.Users;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class UserDto {

    @ApiModelProperty(value = "user idx")
    private long idx;

    @ApiModelProperty(value = "user code")
    private String userCode;

    @ApiModelProperty(value = "user id")
    private String userId;

    @ApiModelProperty(value = "user password")
    private String userPw;

    @ApiModelProperty(value = "user status")
    private CodeManagementDto status;

    @ApiModelProperty(value = "user name")
    private String name;

    @ApiModelProperty(value = "user phone number")
    private String phone;

    @ApiModelProperty(value = "user email")
    private String email;

    @ApiModelProperty(value = "user nickName")
    private String nickName;

    @ApiModelProperty(value = "user image")
    private ImageDto image;

    @ApiModelProperty(value = "user memo")
    private String memo;

    @ApiModelProperty(value = "user address")
    private String address;

    @ApiModelProperty(value = "user sub address")
    private String addressSub;

    @ApiModelProperty(value = "user zipcode")
    private String zipcode;

    @ApiModelProperty(value = "user birthday")
    private String birth;

    @ApiModelProperty(value = "user gender")
    private String gender;

    @ApiModelProperty(value = "user height")
    private long height;

    @ApiModelProperty(value = "user weight")
    private long weight;

    // DTO -> Entity
    public Users insert()
    {
        Users user = new Users();

        user.setIdx(this.idx);
        user.setUserId(this.userId);
        user.setUserPw(this.userPw);
        user.setName(this.name);
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setNickName(this.nickName);
        if (this.image != null) user.setImage(this.image.insert());
        user.setMemo(this.memo);
        user.setAddress(this.address);
        user.setAddressSub(this.addressSub);
        user.setZipcode(this.zipcode);
        user.setBirth(this.birth);
        user.setGender(this.gender);
        user.setHeight(this.height);
        user.setWeight(this.weight);

        return user;
    }

    // Entity -> DTO
    public UserDto(Users user)
    {
        if(user == null) return;

        this.idx            = user.getIdx();
        this.userCode       = user.getUserCode();
        this.userId         = user.getUserId();
        this.status         = new CodeManagementDto(user.getStatus());
        this.name           = user.getName();
        this.image          = new ImageDto(user.getImage());
        this.phone          = user.getPhone();
        this.memo           = user.getMemo();
        this.email          = user.getEmail();
        this.nickName       = user.getNickName();
        this.address        = user.getAddress();
        this.addressSub     = user.getAddressSub();
        this.zipcode        = user.getZipcode();
        this.birth          = user.getBirth();
        this.gender         = user.getGender();
        this.height         = user.getWeight();
        this.weight         = user.getWeight();
    }
}
