package com.ian.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ianhau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountVO {
    public List<TypeNumber> typeNumbers;
    private List<ClassCount> categoryList;
    private List<SpeciesList> speciesList;
    private List<Distribution> distribution;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeNumber {
        private String title;
        private Long number;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpeciesList {
        private String species;
        private Long varietyNumber;
        private Long phenotypicNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassCount {
        private String classification;
        private Long categoryNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Distribution {
        private String selectArea;
        private String id;
        private Long value;
    }
}
