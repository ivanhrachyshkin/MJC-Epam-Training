package com.epam.esm.dao.config;

import com.github.javafaker.Faker;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.time.Clock;
import java.util.Properties;
import java.util.Random;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
public class DaoConfig {

    @Bean
    public DataSource dataSource(final DataSourceProperties dataSourceProperties)
            throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(dataSourceProperties.getDriver());
        dataSource.setJdbcUrl(dataSourceProperties.getUrl());
        dataSource.setUser(dataSourceProperties.getUser());
        dataSource.setPassword(dataSourceProperties.getPassword());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final DataSourceProperties dataSourceProperties) {

        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();

        final Properties props = new Properties();
        props.put("hibernate.dialect", dataSourceProperties.getDialect());
        props.put("hibernate.show_sql", dataSourceProperties.getShowSql());

        entityManagerFactoryBean.setJpaProperties(props);
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.epam.esm.model");
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            final LocalContainerEntityManagerFactoryBean entityManagerFactory,
            final DataSource dataSource) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
