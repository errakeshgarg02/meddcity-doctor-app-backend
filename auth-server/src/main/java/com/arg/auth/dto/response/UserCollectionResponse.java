/*
 * arg license
 *
 */

package com.arg.auth.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCollectionResponse implements Serializable {

    private static final long serialVersionUID = 6050834829766149204L;

    private Integer count;

    private List<UserResponse> users;

}
