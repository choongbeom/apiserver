package com.cbkim.apiserver.util.converter;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import com.cbkim.apiserver.entity.Images;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ImageAttributeConverter implements AttributeConverter<Images, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public String convertToDatabaseColumn(Images attribute) {
        try {
           return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @Override
    public Images convertToEntityAttribute(String dbData) {
        // Json 문자열 -> AdditionalData로 변환
        try {
            return objectMapper.readValue(dbData, Images.class);
        } catch (IOException e) {
            return null;
        }
    }

}