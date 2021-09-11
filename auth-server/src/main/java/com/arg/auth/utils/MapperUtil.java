/*
 * arg license
 *
 */

package com.arg.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MapperUtil {

    @Autowired
    private ObjectMapper mapper;

    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(String data, Class<T> type) {
        try {
            return mapper.readValue(data, type);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(String data, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(data, typeReference);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T convertObject(Object data, Class<T> type) {
        try {
            return mapper.convertValue(data, type);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T convertObject(Object data, TypeReference<T> typeReference) {
        try {
            return mapper.convertValue(data, typeReference);
        } catch (Exception e) {
            log.error("Parsing error ", e);
            throw new RuntimeException(e);
        }
    }

}
