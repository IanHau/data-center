package com.ian.entity;

import com.migozi.db.mongo.CriteriaBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.Nullable;

/**
 * @author ianhau
 */
public class OntologyProperty {
    public static final String COLLECTION_NAME = "ontologyProperty";

    public static class Relationships {
        public static final String ONTOLOGY_TERM = "ontologyTerm";
        public static final String CREATED_BY = "createdBy";
        public static final String UPDATED_BY = "updatedBy";
    }

    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "ontologyOid")
    private String ontologyOid;
    @Field(name = "ontologyTerm")
    @Nullable
    private OntologyTermRedundancy ontologyTerm;
    @Field(name = "name")
    private String name;
    @Field(name = "showName")
    private String showName;
    @Field(name = "required")
    private Boolean required;
    @Field(name = "editable")
    private Boolean editable;
    @Field(name = "priority")
    private Integer priority;
    @Field(name = "dataType")
    private String dataType;
    @Field(name = "enumTypeOid")
    private String enumTypeOid;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setOntologyOid(String ontologyOid) {
        this.ontologyOid = ontologyOid;
    }

    public String getOntologyOid() {
        return this.ontologyOid;
    }

    public void setOntologyTerm(OntologyTermRedundancy ontologyTerm) {
        this.ontologyTerm = ontologyTerm;
    }

    public OntologyTermRedundancy getOntologyTerm() {
        return this.ontologyTerm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return this.showName;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getRequired() {
        return this.required;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getEditable() {
        return this.editable;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setEnumTypeOid(String enumTypeOid) {
        this.enumTypeOid = enumTypeOid;
    }

    public String getEnumTypeOid() {
        return this.enumTypeOid;
    }

    public static final class CriteriaWrapper<T extends com.migozi.db.mongo.QueryBuilder> {
        private CriteriaBuilder<T> criteria;
        private OntologyTermCriteriaWrapper ontologyTermCriteria;
        private ExternalOntologyCriteriaWrapper externalOntologyCriteria;

        public CriteriaWrapper(CriteriaBuilder criteria) {
            this.criteria = criteria;
        }

        public CriteriaBuilder<T> id() {
            criteria.setColumnName("id");
            return criteria;
        }

        public CriteriaBuilder<T> ontologyOid() {
            criteria.setColumnName("ontologyOid");
            return criteria;
        }

        public CriteriaBuilder<T> name() {
            criteria.setColumnName("name");
            return criteria;
        }

        public CriteriaBuilder<T> showName() {
            criteria.setColumnName("showName");
            return criteria;
        }

        public CriteriaBuilder<T> required() {
            criteria.setColumnName("required");
            return criteria;
        }

        public CriteriaBuilder<T> hidden() {
            criteria.setColumnName("hidden");
            return criteria;
        }

        public CriteriaBuilder<T> searchable() {
            criteria.setColumnName("searchable");
            return criteria;
        }

        public CriteriaBuilder<T> editable() {
            criteria.setColumnName("editable");
            return criteria;
        }

        public CriteriaBuilder<T> priority() {
            criteria.setColumnName("priority");
            return criteria;
        }

        public CriteriaBuilder<T> dataType() {
            criteria.setColumnName("dataType");
            return criteria;
        }

        public CriteriaBuilder<T> defaultValue() {
            criteria.setColumnName("defaultValue");
            return criteria;
        }

        public CriteriaBuilder<T> identifier() {
            criteria.setColumnName("identifier");
            return criteria;
        }

        public CriteriaBuilder<T> length() {
            criteria.setColumnName("length");
            return criteria;
        }

        public CriteriaBuilder<T> enumTypeOid() {
            criteria.setColumnName("enumTypeOid");
            return criteria;
        }

        public OntologyTermCriteriaWrapper<QueryBuilder> ontologyTerm() {
            if (ontologyTermCriteria == null) {
                ontologyTermCriteria = new OntologyTermCriteriaWrapper<QueryBuilder>(criteria);
            }
            criteria.setPrefix("ontologyTerm");
            return ontologyTermCriteria;
        }

        public ExternalOntologyCriteriaWrapper<QueryBuilder> externalOntology() {
            if (externalOntologyCriteria == null) {
                externalOntologyCriteria = new ExternalOntologyCriteriaWrapper<QueryBuilder>(criteria);
            }
            criteria.setPrefix("externalOntology");
            return externalOntologyCriteria;
        }
    }

    public static final class OntologyTermCriteriaWrapper<T extends com.migozi.db.mongo.QueryBuilder> {
        private CriteriaBuilder<T> criteria;

        public OntologyTermCriteriaWrapper(CriteriaBuilder criteria) {
            this.criteria = criteria;
        }

        public CriteriaBuilder<T> term() {
            criteria.setColumnName("term");
            return criteria;
        }
    }

    public static final class ExternalOntologyCriteriaWrapper<T extends com.migozi.db.mongo.QueryBuilder> {
        private CriteriaBuilder<T> criteria;

        public ExternalOntologyCriteriaWrapper(CriteriaBuilder criteria) {
            this.criteria = criteria;
        }

        public CriteriaBuilder<T> iri() {
            criteria.setColumnName("iri");
            return criteria;
        }

        public CriteriaBuilder<T> term() {
            criteria.setColumnName("term");
            return criteria;
        }
    }

    public static final class QueryBuilder extends com.migozi.db.mongo.QueryBuilder<CriteriaWrapper<QueryBuilder>> {
        public QueryBuilder() {
            super(CriteriaWrapper.class);
        }
    }

    public static class OntologyTermRedundancy {
        public final static class Fields {
            public static final String TERM = "term";
        }

        @Field(name = "term")
        private String term;

        public void setTerm(String term) {
            this.term = term;
        }

        public String getTerm() {
            return this.term;
        }

        public void copyFromOntologyOid(OntologyOid from) {
            this.term = from.getTerm();
        }

        public OntologyOid toOntologyOid() {
            OntologyOid to = new OntologyOid();
            to.setTerm(this.term);
            return to;
        }

        public static OntologyTermRedundancy from(OntologyOid source) {
            OntologyTermRedundancy result = new OntologyTermRedundancy();
            result.copyFromOntologyOid(source);
            return result;
        }
    }
}
