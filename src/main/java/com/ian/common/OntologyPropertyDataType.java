package com.ian.common;


import com.ian.utils.DateUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ian.common.Operator.*;

/**
 * @author ianhau
 */
public enum OntologyPropertyDataType {
    /***/
    Enum {
        @Override
        public Criteria format(java.lang.String k, Object v) {
            if (v instanceof List) {
                List list = (List) v;
                return Criteria.where(k).in(list);
            }
            return Criteria.where(k).ne("").ne(null);
        }
    },
    String {
        @Override
        public Criteria format(java.lang.String k, Object v) {

            if (v instanceof String) {
                return Criteria.where(k).is(v);
            }
            if (v instanceof Map) {
                Map valueMap = (Map) v;
                if (valueMap.size() == 0) {
                    return Criteria.where(k).ne("").ne(null);
                }
                return Criteria.where(k).regex(".*" + valueMap.get("$regex").toString() + ".*");
            }
            return Criteria.where(k).ne("").ne(null);
        }
    },
    Decimal {
        @Override
        public Criteria format(java.lang.String k, Object v) {
            return getCriteria(k, v);
        }
    },
    Boolean {
        @Override
        public Criteria format(java.lang.String k, Object v) {
            if (v instanceof Map) {
                return Criteria.where(k).ne("").ne(null);
            }
            java.lang.Boolean b = (Boolean) v;
            return Criteria.where(k).in(b);

        }
    },
    Date {
        @Override
        public Criteria format(java.lang.String k, Object v) {
            if (v instanceof String) {
                String str = (String) v;
                java.util.Date date = DateUtils.getDate(str);
                return Criteria.where(k).is(date);
            } else if (v instanceof Map) {
                Map<String, Object> valueMap = (Map) v;
                if (valueMap.size() == 0) {
                    return Criteria.where(k).ne("").ne(null);
                }
                List<Criteria> collect = valueMap.entrySet().stream().map(entry -> {
                    Object key = ((Map.Entry) entry).getKey();
                    Object value = entry.getValue();

                    if (gt.val().equals(key)) {
                        String str = (String) value;
                        java.util.Date date = DateUtils.getDate(str);
                        return Criteria.where(k).gt(date);
                    }
                    if (lt.val().equals(key)) {
                        String str = (String) value;
                        java.util.Date date = DateUtils.getDate(str);
                        return Criteria.where(k).lt(date);
                    }
                    if (gte.val().equals(key)) {
                        String str = (String) value;
                        java.util.Date date = DateUtils.getDate(str);
                        return Criteria.where(k).gte(date);
                    }
                    if (lte.val().equals(key)) {
                        String str = (String) value;
                        java.util.Date date = DateUtils.getDate(str);
                        return Criteria.where(k).lte(date);
                    }
                    return null;
                }).collect(Collectors.toList());
                return new Criteria().andOperator(collect.toArray(new Criteria[]{}));
            } else {
                return Criteria.where(k).ne("").ne(null);
            }
        }
    },
    Integer {
        @Override
        public Criteria format(java.lang.String k, Object v) {
            return getCriteria(k, v);
        }
    };
    private static Criteria getCriteria(String k, Object v) {
        if (v instanceof String) {
            return Criteria.where(k).is(v);
        }
        if (v instanceof Map) {
            Map<String, Object> valueMap = (Map<String, Object>) v;
            if (valueMap.size() == 0) {
                return Criteria.where(k).ne("").ne(null);
            }
            List<Criteria> collect = valueMap.entrySet().stream()
                    .map(entry -> {
                        Object key = ((Map.Entry) entry).getKey();
                        Object value = entry.getValue();

                        if (gt.val().equals(key)) {
                            return Criteria.where(k).gt(value);
                        }
                        if (lt.val().equals(key)) {
                            return Criteria.where(k).lt(value);
                        }
                        if (gte.val().equals(key)) {
                            return Criteria.where(k).gte(value);
                        }
                        if (lte.val().equals(key)) {
                            return Criteria.where(k).lte(value);
                        }
                        return null;
                    }).collect(Collectors.toList());
            return new Criteria().andOperator(collect.toArray(new Criteria[]{}));
        }
        return Criteria.where(k).ne("").ne(null);
    }

    public static List<String> dataTypes() {
        return Arrays.stream(values()).map(java.lang.Enum::name).collect(Collectors.toList());
    }

    public static OntologyPropertyDataType get(String dataType) {
        return Arrays.stream(values()).filter(it -> it.name().equals(dataType)).findFirst().orElse(null);
    }

    /**
     * 格式化查询语句
     *
     * @param k 待查询的字段
     * @param v 待查询字段的查询条件
     *          如 {"updatedDate": { "$gte" : "2022-12-03 00:00:00"}}
     *          updatedDate 是 k
     *          { "$gte" : "2022-12-03 00:00:00"} 是 v
     * @return 封装完成的mongo查询语句
     */
    public abstract Criteria format(String k, Object v);


}
