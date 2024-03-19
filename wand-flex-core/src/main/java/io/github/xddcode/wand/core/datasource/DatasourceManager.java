package io.github.xddcode.wand.core.datasource;

import io.github.xddcode.wand.core.exception.DataSourceException;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源管理
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 10:26
 */
public class DatasourceManager {

    private final DataSource dataSource;

    public DatasourceManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<TableInfo> getTableInfos() {
        List<String> tables = getTables();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (String table : tables) {
            TableInfo tableInfo = new TableInfo();
            String ddl = getTableDDL(table);
            tableInfo.setTableName(table);
            tableInfo.setTableDDL(ddl);
            tableInfos.add(tableInfo);
        }
        return tableInfos;
    }

    /**
     * 获取所有表名
     *
     * @return
     */
    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.getMetaData().getTables(getDatabaseNameFromUrl(connection.getMetaData().getURL()), null, "%", new String[]{"TABLE"})) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to retrieve table names", e);
        } catch (URISyntaxException e) {
            throw new DataSourceException("Failed to retrieve database name from URL", e);
        }
        return tables;
    }

    public String getTableDDL(String tableName) {
        String query = "SHOW CREATE TABLE " + tableName;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("Create Table") != null ? rs.getString("Create Table") : rs.getString(2);
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to retrieve DDL for table: " + tableName, e);
        }
        return "";
    }

    private String getDatabaseNameFromUrl(String url) throws URISyntaxException {
        URI uri = new URI(url.substring(5)); // 移除"jdbc:"部分
        String path = uri.getPath();
        if (path.startsWith("/")) {
            path = path.substring(1); // 移除路径前面的"/"
        }
        return path.contains("/") ? path.substring(0, path.indexOf('/')) : path; // 截取第一个"/"前的部分作为数据库名
    }
}
