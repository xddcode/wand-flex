package io.github.xddcode.wand.datasource;

import io.github.xddcode.wand.exception.DataSourceException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 10:26
 */
public class DatasourceManager {

    private static final String yamlFilePath = "application.yml";

    public static DataSource getDataSource() {
        DataSourceConfig config = loadDataSourceConfig(yamlFilePath);
        String url = config.getUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        String driverClassName = config.getDriverClassName();
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    /**
     * 获取所有表名
     *
     * @return
     */
    public static List<String> getTables() {
        DataSource dataSource = getDataSource();
        return getTables(dataSource);
    }

    public static List<TableInfo> getTableInfos() {
        DataSource dataSource = getDataSource();
        List<String> tables = getTables(dataSource);
        List<TableInfo> tableInfos = new ArrayList<>();
        for (String table : tables) {
            TableInfo tableInfo = new TableInfo();
            String ddl = getTableDDL(dataSource, table);
            tableInfo.setTableName(table);
            tableInfo.setTableDDL(ddl);
            tableInfos.add(tableInfo);
        }
        return tableInfos;
    }

    /**
     * 获取所有表名
     *
     * @param dataSource
     * @return
     */
    public static List<String> getTables(DataSource dataSource) {
        List<String> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to retrieve table names", e);
        }
        return tables;
    }

    public static String getTableDDL(String tableName) {
        DataSource dataSource = getDataSource();
        return getTableDDL(dataSource, tableName);
    }

    public static String getTableDDL(DataSource dataSource, String tableName) {
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

    @SuppressWarnings("unchecked")
    public static DataSourceConfig loadDataSourceConfig(String yamlFilePath) {
        Yaml yaml = new Yaml(new Constructor(Map.class));
        InputStream inputStream = DatasourceManager.class
                .getClassLoader()
                .getResourceAsStream(yamlFilePath);

        if (inputStream == null) {
            throw new DataSourceException("Cannot find " + yamlFilePath);
        }
        Map<String, Object> obj = yaml.load(inputStream);
        Map<String, Object> dataSourceProps = (Map<String, Object>) ((Map<String, Object>) obj.get("spring")).get("datasource");

        DataSourceConfig config = new DataSourceConfig();
        config.setUrl((String) dataSourceProps.get("url"));
        config.setUsername((String) dataSourceProps.get("username"));
        config.setPassword((String) dataSourceProps.get("password"));
        config.setDriverClassName((String) dataSourceProps.get("driver-class-name"));
        return config;
    }
}
