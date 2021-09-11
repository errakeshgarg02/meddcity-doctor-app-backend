/*
 * arg license
 *
 */

package com.arg.common.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 4654228195513914683L;

    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_description")
    private String errorDesc;
    @JsonProperty("user_description")
    private String userDesc;

    @JsonIgnore
    private Map<String, Object> additonalProperties;

    @JsonAnySetter
    public void setProperty(String key, Object value) {
        if (additonalProperties == null) {
            additonalProperties = new HashMap<>();
        }
        additonalProperties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperty() {
        if (additonalProperties == null) {
            additonalProperties = new HashMap<>();
        }
        return additonalProperties;
    }
}
