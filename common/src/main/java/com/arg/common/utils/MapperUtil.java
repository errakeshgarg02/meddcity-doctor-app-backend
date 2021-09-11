/*
 * arg license
 *
 */

package com.arg.common.utils;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return "";
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String data, Class<T> type) {
        try {
            return mapper.readValue(data, type);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String data, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(data, typeReference);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertObject(Object data, Class<T> type) {
        try {
            return mapper.convertValue(data, type);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertObject(Object data, TypeReference<T> typeReference) {
        try {
            return mapper.convertValue(data, typeReference);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

}
