package com.ian.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableMap;
import com.ian.config.OidGenerator;
import com.ian.constant.OIDStr;
import com.ian.controller.resp.OidTreeVO;
import com.ian.controller.resp.OntologyOidVO;
import com.ian.entity.OntologyOid;
import com.ian.entity.OntologyProperty;
import com.ian.entity.Record;
import com.ian.exception.ServiceException;
import com.ian.utils.OCID;
import com.migozi.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.Document;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
@Slf4j
public class OntologyOidService {

    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    OntologyPropertyService propertyService;
    @Resource
    OidGenerator oidGenerator;
    private final Cache<String, List<OidTreeVO>> treeCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void createOid(OntologyOid input) {
        validateNotEmpty(input.getTerm(), "term.empty");
        validateNotEmpty(input.getAlias(), "alias.empty");
        validateNotEmpty(input.getDefinition(), "definition.empty");
        String iriString = oidGenerator.next();
        input.setOid(iriString);
        create(input);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void create(OntologyOid input) {
        validateNotEmpty(input.getTerm(), "term.empty");
        validateNotEmpty(input.getAlias(), "alias.empty");
        validateNotEmpty(input.getDefinition(), "definition.empty");
        OntologyOid entity = new OntologyOid();
        entity.setTerm(input.getTerm());
        entity.setParent(input.getParent());
        entity.setAlias(input.getAlias());
        entity.setOid(input.getOid());
        entity.setDefinition(input.getDefinition());

        entity.setId(OCID.get().toHexString().toLowerCase());

        termDuplicationCheck(null, entity.getTerm());
        oidDuplicationCheck(null, entity.getOid());
        mongoTemplate.insert(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void delete(String id) {
        OntologyOid ontologyOid = load(id);
        if (ontologyOid == null) {
            throw new ServiceException("400", "ontology id not exist.");
        }
        List<OntologyOid> children = loadByParent(ontologyOid.getOid());
        if (CollectionUtils.isNotEmpty(children)) {
            throw new ServiceException("400", "存在子节点");
        }
        mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), OntologyOid.class);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void update(OntologyOid input) {
        OntologyOid entity = new OntologyOid();
        if (input.getId() == null) {
            throw new ServiceException("400", "id.empty");
        }
        entity.setTerm(input.getTerm());
        entity.setParent(input.getParent());
        entity.setAlias(input.getAlias());
        entity.setOid(input.getOid());
        entity.setDefinition(input.getDefinition());

        OntologyOid curr = mongoTemplate.findById(input.getId(), OntologyOid.class);
        termDuplicationCheck(curr, entity.getTerm());
        oidDuplicationCheck(curr, entity.getOid());

        mongoTemplate.findAndReplace(
                new Query(Criteria.where("_id").is(input.getId())),
                entity,
                FindAndReplaceOptions.options().returnNew(),
                OntologyOid.class,
                "ontologyOid",
                OntologyOid.class);
    }

    public OntologyOid load(String id) {
        return mongoTemplate.findById(id, OntologyOid.class);
    }

    public List<OntologyOid> list() {
        return mongoTemplate.findAll(OntologyOid.class, "ontologyOid");
    }

    public Set<OntologyOidVO> loadLeaves(String oid, String term) {
        OntologyOid ontologyOid = null;
        if (StringUtils.isNotBlank(oid)) {
            ontologyOid = loadByOid(oid);
        } else {
            ontologyOid = loadByTerm(term);
        }
        if (ontologyOid == null) {
            throw new ServiceException("400", "数据不存在");
        }
        Set<OntologyOidVO> leaves = new TreeSet<>(Comparator.comparing(OntologyOidVO::getTerm));
        travel(ontologyOid, leaves);
        return leaves;
    }


    public List<OidTreeVO> tree() {
        List<OidTreeVO> cache = treeCache.getIfPresent("tree");
        if (CollectionUtils.isNotEmpty(cache)) {
            log.info("read cache success");
            return cache;
        }
        List<OidTreeVO> tree = tree(null);
        treeCache.put("tree", tree);
        return tree;
    }

    public List<OidTreeVO> tree(String oid) {
        List<OntologyOid> firstLevels = loadByParent(oid);
        if (CollectionUtils.isEmpty(firstLevels)) {
            return new ArrayList<>();
        }
        return firstLevels.stream().map(f -> OidTreeVO.build(f, tree(f.getOid()))).collect(Collectors.toList());
    }

    private void travel(OntologyOid ontologyOid, Set<OntologyOidVO> leaves) {
        List<OntologyOid> children = loadByParent(ontologyOid.getOid());
        if (CollectionUtils.isEmpty(children)) {
            fillOntologyProperties(ontologyOid, leaves);
            log.info("leaves added. term:[{}]", ontologyOid.getTerm());
            return;
        }
        children.forEach(it -> travel(it, leaves));
    }

    public Record loadRecord(String tableName, String idName, String id) {
        Document document = mongoTemplate.getCollection(tableName)
                .find(new Document(ImmutableMap.of(idName, id))).first();
        if (document == null) {
            throw new ApplicationException("record not exist");
        }
        return Record.fromMap(document);
    }

    private void fillOntologyProperties(OntologyOid ontologyOid, Set<OntologyOidVO> leaves) {
        List<OntologyProperty> ontologyProperties = propertyService.loadByOid(ontologyOid.getOid());
        leaves.add(OntologyOidVO.builder().oid(ontologyOid.getOid())
                .term(ontologyOid.getTerm())
                .alias(ontologyOid.getAlias())
                .properties(ontologyProperties)
                .build());
    }

    public OntologyOid loadByOid(String oid) {
        return mongoTemplate.findOne(new Query(Criteria.where("oid").is(oid)), OntologyOid.class);
    }

    public OntologyOid loadByTerm(String term) {
        return mongoTemplate.findOne(new Query(Criteria.where("term").is(term)), OntologyOid.class);
    }

    protected void termDuplicationCheck(OntologyOid curr, String term) {
        OntologyOid template = new OntologyOid();
        template.setTerm(term);
        if (duplicateCheck(curr, template)) {
            return;
        }
        throw new ServiceException("600", "duplicated.term");
    }

    protected void oidDuplicationCheck(OntologyOid curr, String oid) {
        OntologyOid template = new OntologyOid();
        template.setOid(oid);
        if (duplicateCheck(curr, template)) {
            return;
        }
        throw new ServiceException("600", "duplicated.oid");
    }

    private boolean duplicateCheck(OntologyOid curr, OntologyOid template) {
        Query query = Query.query(Criteria.byExample(template));
        OntologyOid existed = mongoTemplate.findOne(query, OntologyOid.class);
        if (existed == null) {
            return true;
        }
        return curr != null && curr.getId().equals(existed.getId());
    }

    private List<OntologyOid> loadByParent(String oid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("parent").is(oid));
        return mongoTemplate.find(query, OntologyOid.class);
    }

    public void saveRecord(OntologyOid ontologyOid, Map<String, Object> data) throws DuplicateKeyException {
        String identifier = "_id";
        String tableName = ontologyOid.getTableName();
        data.computeIfAbsent(identifier, key -> OCID.get().toHexString().toLowerCase());
        try {
            mongoTemplate.insert(data, tableName);
        } catch (DuplicateKeyException e) {
            log.error("data:{},ontologyOid:{}, e:{} ", data, ontologyOid.getOid(), ExceptionUtils.getStackTrace(e));
            throw new ServiceException("400", "duplicate code");
        }
    }

    public void removeRecord(String tableName, String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.getCollection(tableName).deleteOne(query.getQueryObject());
    }

    public List<OntologyOid> allTable() {
        return loadByParent(OIDStr.GERMPLASM_OID);
    }
}
