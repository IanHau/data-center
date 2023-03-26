package com.ian.entity;

import lombok.Data;

import java.util.List;

@Data
public class AreaEntry {
    private String code;
    private String name;
    private String shortName;
    private String nameCapitalPy;
    private String shortNameCapitalPy;
    private String nameFullPy;
    private String shortNameFullPy;
    private boolean municipality;
    private List<AreaEntry> children;

    public static AreaEntry cloneWithoutChildren(AreaEntry source) {
        AreaEntry result = new AreaEntry();

        result.code = source.code;
        result.name = source.name;
        result.shortName = source.shortName;
        result.nameCapitalPy = source.nameCapitalPy;
        result.shortNameCapitalPy = source.shortNameCapitalPy;
        result.nameFullPy = source.nameFullPy;
        result.shortNameFullPy = source.shortNameFullPy;
        result.municipality = source.municipality;

        return result;
    }
}
