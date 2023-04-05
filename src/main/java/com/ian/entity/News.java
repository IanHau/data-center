package com.ian.entity;

import lombok.Data;

/**
 * @author ianhau
 */
@Data
public class News {
    public static final String COLLECTION_NAME = "news";
    private String link;
    private String title;
    private String content;
    private String date;
    private boolean show;
}
