package com.jwlee.bookshow;

import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
* H2ServerConfiguration , DB JVM에 SET
* @author jungwoolee
* @since 2019-07-22
**/
@Configuration
@Profile("dev")
public class H2ServerConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource") // application properties의 설정값을 Set한다.
    public DataSource dataSource() throws SQLException {
        Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }
}