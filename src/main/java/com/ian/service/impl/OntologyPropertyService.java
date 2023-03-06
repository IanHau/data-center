package com.ian.service.impl;

import com.ian.entity.OntologyOid;
import com.ian.entity.OntologyProperty;
import com.ian.exception.ServiceException;
import com.ian.utils.OCID;
import com.migozi.ApplicationException;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static cn.hutool.core.lang.Validator.validateNotEmpty;

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

    public List<OntologyProperty> loadByOid(String oid) {
        return mongoTemplate.find(
                new Query(Criteria.where("ontologyOid").is(oid)),
                OntologyProperty.class, "ontologyProperty");
    }

    public void delete(String id) {
        mongoTemplate.remove(
                new Query(Criteria.where("_id").is(id)),
                OntologyProperty.class, "ontologyProperty");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void createProperty(OntologyProperty input) {
        String ontologyOid = input.getOntologyOid();
        List<OntologyProperty> ontologyProperties = loadByOid(ontologyOid);
        if (ontologyProperties.stream().anyMatch(it -> it.getName().equals(input.getName()))) {
            throw new ServiceException("400", "property already exist.");
        }
        create(input);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void create(OntologyProperty input) {
        validateNotEmpty(input.getOntologyOid(), "ontologyOid.empty");
        validateNotEmpty(input.getName(), "name.empty");
        validateNotEmpty(input.getShowName(), "showName.empty");
        validateNotEmpty(input.getRequired(), "required.empty");
        validateNotEmpty(input.getEditable(), "editable.empty");
        validateNotEmpty(input.getDataType(), "dataType.empty");
        OntologyProperty entity = new OntologyProperty();
        entity.setOntologyOid(input.getOntologyOid());
        entity.setName(input.getName());
        entity.setShowName(input.getShowName());
        entity.setRequired(input.getRequired());
        entity.setEditable(input.getEditable());
        entity.setDataType(input.getDataType());
        entity.setEnumTypeOid(input.getEnumTypeOid());

        entity.setId(OCID.get().toHexString().toLowerCase());

        mongoTemplate.insert(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void update(OntologyProperty input) {
        validateNotEmpty(input.getId(),"id required");
        OntologyProperty entity = new OntologyProperty();
        entity.setId(input.getId());
        entity.setOntologyOid(input.getOntologyOid());
        entity.setName(input.getName());
        entity.setShowName(input.getShowName());
        entity.setRequired(input.getRequired());
        entity.setEditable(input.getEditable());
        entity.setDataType(input.getDataType());
        entity.setEditable(input.getEditable());
        entity.setPriority(input.getPriority());
        entity.setEnumTypeOid(input.getEnumTypeOid());

        mongoTemplate.findAndReplace(
                new Query(Criteria.where("_id").is(input.getId())),
                entity,
                FindAndReplaceOptions.options().returnNew(),
                OntologyProperty.class,
                "ontologyProperty",
                OntologyProperty.class);
    }
}
