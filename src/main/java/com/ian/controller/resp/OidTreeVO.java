package com.ian.controller.resp;

import com.ian.entity.OntologyOid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author ianhau
 */
@Data
@AllArgsConstructor
public class OidTreeVO {

    private List<OidTreeVO> children;

    private String id;
    private java.lang.String oid;
    private java.lang.String term;
    private java.lang.String parent;
    private java.lang.String alias;
    private java.lang.String definition;

    public static OidTreeVO build(OntologyOid oid, List<OidTreeVO> children) {
        return new OidTreeVO(children, oid.getId(), oid.getOid(), oid.getTerm(), oid.getParent(), oid.getAlias(), oid.getDefinition());
    }
}