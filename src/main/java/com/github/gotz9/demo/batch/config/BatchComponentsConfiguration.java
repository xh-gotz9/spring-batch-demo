package com.github.gotz9.demo.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class BatchComponentsConfiguration {

    static class MultiDataSourceConfiguration {

        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource")
        DataSourceProperties defaultDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean("MySQL-DataSource")
        @Primary
        DataSource mysqlDataSource(DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder()
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean("h2-datasource-properties")
        @ConfigurationProperties("app.datasource.h2")
        DataSourceProperties h2DatasourceProperties() {
            return new DataSourceProperties();
        }

        /**
         * create a datasource marked with {@link BatchDataSource}.
         * <p>
         * It will be used in Spring Batch components.
         */
        @Bean("H2-DataSource")
        @BatchDataSource
        DataSource h2DataSource(@Qualifier("h2-datasource-properties") DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder()
                    .type(HikariDataSource.class)
                    .build();
        }

    }

    /**
     * auto register jobs into jobRegistry
     */
    @Bean
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);

        return postProcessor;
    }

}
