package com.ian.config;

import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhourong
 * @date 2022-07-21 11:59
 * <p>
 * OID 生成器
 */
@Component
public class OidGenerator {

    private static final String PREFIX = "/ISO/Member-Body/CN/1125/";

    public String next() {
        return IRI.getNextDocumentIRI(PREFIX).getIRIString();
    }
}
