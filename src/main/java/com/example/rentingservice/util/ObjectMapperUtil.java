package com.example.rentingservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private ObjectMapperUtil() {
        throw new IllegalArgumentException();
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convert(Object source, Class<T> toValueType) {
        return objectMapper.convertValue(source, toValueType);
    }
}
