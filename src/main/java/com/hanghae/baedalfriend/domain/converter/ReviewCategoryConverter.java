package com.hanghae.baedalfriend.domain.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
@Slf4j
public class ReviewCategoryConverter implements AttributeConverter<ReviewCategory, String> {

    @Override
    public String convertToDatabaseColumn(ReviewCategory reviewCategory) {
        if (reviewCategory == null)
            return null;
        return reviewCategory.getValue();
    }

    @Override
    public ReviewCategory convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        try {
            return ReviewCategory.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            log.error("failure to convert cause unexpected code [{}]", dbData, e);
            throw e;
        }
    }
}
