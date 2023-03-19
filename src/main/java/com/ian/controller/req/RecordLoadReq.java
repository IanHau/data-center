package com.ian.controller.req;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author ianhau
 */
@Data
@AllArgsConstructor
public class RecordLoadReq {
    private String ontologyOid;
    private String id;
}
