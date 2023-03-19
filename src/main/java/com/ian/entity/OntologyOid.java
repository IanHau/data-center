package com.ian.entity;

import com.google.common.base.CaseFormat;
import com.migozi.db.mongo.CriteriaBuilder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author ianhau
 */
@Data
public class OntologyOid {
    public static final String COLLECTION_NAME = "ontologyOid";
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "oid")
    private String oid;
    @Field(name = "term")
    private String term;
    @Field(name = "parent")
    private String parent;
    @Field(name = "alias")
    private String alias;
    @Field(name = "definition")
    private String definition;

    public static final class CriteriaWrapper<T extends com.migozi.db.mongo.QueryBuilder> {
        private final CriteriaBuilder<T> criteria;

        public CriteriaWrapper(CriteriaBuilder criteria) {
            this.criteria = criteria;
        }

        public CriteriaBuilder<T> id() {
            criteria.setColumnName("id");
            return criteria;
        }

        public CriteriaBuilder<T> oid() {
            criteria.setColumnName("oid");
            return criteria;
        }

        public CriteriaBuilder<T> term() {
            criteria.setColumnName("term");
            return criteria;
        }

        public CriteriaBuilder<T> parent() {
            criteria.setColumnName("parent");
            return criteria;
        }

        public CriteriaBuilder<T> alias() {
            criteria.setColumnName("alias");
            return criteria;
        }
    }

    @Data
    public static class SearchCriteria {
        private String queryKey;
        private String term;
        private String oid;
    }

    public String getTableName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getTerm());
    }

}
