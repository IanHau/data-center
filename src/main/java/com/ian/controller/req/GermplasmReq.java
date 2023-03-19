package com.ian.controller.req;

import cn.hutool.db.sql.Condition;
import lombok.Data;

/**
 * @author ianhau
 */
@Data
public class GermplasmReq {
    private String tableName;
    private Condition condition;
    private String ontologyOid;
}
