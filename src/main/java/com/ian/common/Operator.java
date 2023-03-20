package com.ian.common;

/**
 * @author ianhau
 */
public enum Operator {

    /**
     * 操作符
     */
    gte("$gte", "大于等于"),
    lte("$lte", "小于等于"),
    lt("$lt", "小于"),
    gt("$gt", "大于"),
    eq("$eq", "等于"),
    in("$in", "in");

    private final String val;
    private final String desc;

    Operator(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public String val() {
        return val;
    }

    public String desc() {
        return desc;
    }
}
