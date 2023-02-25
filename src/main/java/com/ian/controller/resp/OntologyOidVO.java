package com.ian.controller.resp;

import com.ian.entity.OntologyProperty;
import lombok.*;

import java.util.List;

/**
 * @author ianhau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class OntologyOidVO {

    @EqualsAndHashCode.Include
    private java.lang.String oid;
    private java.lang.String term;
    private java.lang.String alias;

    private List<OntologyProperty> properties;

}