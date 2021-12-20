package com.cbkim.apiserver.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cbkim.apiserver.entity.Images;
import com.cbkim.apiserver.util.RandomGenerator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class ImageDto{

    @ApiModelProperty(value = "이미지 툴팁")
    private String alt;

    @ApiModelProperty(value = "이미지 경로")
    private String src;

    @ApiModelProperty(value = "대표이미지 여부")
    private String titleYn;

    @ApiModelProperty(value = "이미지 컴포넌트 ID")
    private String imageRef;

    // DTO -> Entity
    public Images insert() {

        Images image = new Images();

        if(this.alt != null) image.setAlt(this.alt);
        if(this.src != null) image.setSrc(this.src);
        if(this.titleYn != null) image.setTitleYn(this.titleYn);
        if(this.imageRef != null) image.setImageRef(this.imageRef);

        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String time1 = format1.format(time);

        image.setCreateDate(time1);

        return image;
    }

    // Entity -> DTO
    public ImageDto(Images image) {

        if(image == null) return;

        this.alt = image.getAlt();
        this.titleYn = image.getTitleYn();
        this.src = image.getSrc();
        this.imageRef = image.getImageRef();
        if(this.imageRef == null) this.imageRef = "ID" + RandomGenerator.generate();
    }

}