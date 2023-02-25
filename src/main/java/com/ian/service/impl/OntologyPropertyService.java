package com.ian.service.impl;

import com.ian.entity.OntologyProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@Service
public class OntologyPropertyService {
    @Resource
    MongoTemplate mongoTemplate;

    public List<OntologyProperty> ontologyProperties(String oid) {
        return mongoTemplate.find(
                new Query(Criteria.where("ontologyOid").is(oid)),
                OntologyProperty.class, "ontologyProperty");
    }
}
