package com.cbkim.apiserver.dto;

import com.cbkim.apiserver.entity.Users;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class UserDto {

    private long idx;
    private String userCode;
    private String userId;
    private String userPw;
    private CodeManagementDto status;
    private String name;
    private String phone;   
    private String email;
    private String nickName;
    private ImageDto image; 
    private String memo;
    private String address;   
    private String addressSub; 
    private String zipcode; 
    private String birth;
    private String gender;
    private long height;
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
