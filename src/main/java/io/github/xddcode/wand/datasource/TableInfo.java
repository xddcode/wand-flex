package io.github.xddcode.wand.datasource;

import lombok.Data;

/**
 * 表信息
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 11:08
 */
@Data
public class TableInfo {
    private String tableName;
    private String tableDDL;
}
