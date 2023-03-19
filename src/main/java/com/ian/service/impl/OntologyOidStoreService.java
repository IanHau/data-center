package com.ian.service.impl;

import cn.hutool.db.sql.Condition;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ian.controller.req.GermplasmReq;
import com.ian.entity.OntologyOid;
import com.ian.entity.OntologyProperty;
import com.ian.entity.Record;
import com.migozi.ApplicationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 平台OID数据存储服务
 *
 * @author ianhau
 * @date 2023-03-21 15:29
 */
@Service
public class OntologyOidStoreService {

    @Resource
    private OntologyOidService ontologyOidService;

    @Resource
    private OntologyPropertyService ontologyPropertyService;

//    @Resource
//    private WorkbookUtils workbookUtils;

    @Resource
    private MongoTemplate mongoTemplate;

    public Record loadRecord(String oid, String id) {
        OntologyOid ontologyOid = ontologyOidService.loadByOid(oid);
        String tableName = ontologyOid.getTableName();
        String idName = "_id";
        return ontologyOidService.loadRecord(tableName, idName, id);
    }

    public void saveRecord(String oid, Map<String, Object> data) {
        data.forEach((key, value) -> {
            OntologyProperty ontologyProperty = ontologyPropertyService.loadByTerm(key);
            if (ObjectUtils.isEmpty(ontologyProperty)) {
                return;
            }
            if (Boolean.TRUE.equals(ontologyProperty.getRequired()) && StringUtils.isBlank(value.toString())) {
                throw new ApplicationException("必填项不能为空");
            }
        });

        ontologyOidService.saveRecord(ontologyOidService.loadByOid(oid), data);
    }

    public void removeRecord(String oid, String id) {
        OntologyOid ontologyOid = ontologyOidService.loadByOid(oid);
        String tableName = ontologyOid.getTableName();
        ontologyOidService.removeRecord(tableName, id);
    }

    public Object search(GermplasmReq input, Pageable pageable) {
        String tableName = input.getTableName();
        Condition condition = input.getCondition();
        Query query = new Query();
        if (condition != null) {
            query.addCriteria(Criteria.where(condition.getField()).is(condition.getValue()));
        }
        long total = mongoTemplate.count(query, tableName);
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query, Record.class, tableName), pageable, total);
    }
}
