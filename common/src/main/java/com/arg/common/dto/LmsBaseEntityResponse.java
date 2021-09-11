/*
 * arg license
 *
 */

package com.arg.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class LmsBaseEntityResponse implements Serializable {

    private static final long serialVersionUID = 2795741750734090865L;

    @Column(name = "created_date")
    protected LocalDateTime createdOn;

    @Column(name = "last_modified_date")
    protected LocalDateTime updatedOn;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "modified_by")
    protected String modifiedBy;
}
