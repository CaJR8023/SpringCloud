package com.cajr.springcloud.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author CAJR
 * @create 2019/9/4 17:32
 */
@Configuration
public class CommonConfig {
    //偏好表表名
    public static final String PREF_TABLE="newslogs";
    //用户id列名
    public static final String PREF_TABLE_USERID="user_id";
    //新闻id列名
    public static final String PREF_TABLE_NEWSID="news_id";
    //偏好值列名
    public static final String PREF_TABLE_PREFVALUE="prefer_degree";
    //用户浏览时间列名
    public static final String PREF_TABLE_TIME="view_time";

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource(){
        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setPoolName("MyhikariCP");
        jdbcConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        jdbcConfig.setJdbcUrl(dataSourceProperties.getUrl());
        jdbcConfig.setUsername(dataSourceProperties.getUsername());
        jdbcConfig.setPassword(dataSourceProperties.getPassword());
        jdbcConfig.setMaximumPoolSize(10);
        jdbcConfig.setMaxLifetime(1800000);
        jdbcConfig.setConnectionTimeout(30000);
        jdbcConfig.setIdleTimeout(600000);
        return new HikariDataSource(jdbcConfig);
    }

    @Bean
    public MySQLBooleanPrefJDBCDataModel mySQLBooleanPrefJDBCDataModel(){
        return new MySQLBooleanPrefJDBCDataModel(dataSource(), PREF_TABLE, PREF_TABLE_USERID,
                PREF_TABLE_NEWSID,PREF_TABLE_TIME);
    }8
}
