package com.ian.controller.req;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author ianhau
 */
@Data
@AllArgsConstructor
public class RecordSaveReq {
    private String ontologyOid;
    private Map<String, Object> data;
}