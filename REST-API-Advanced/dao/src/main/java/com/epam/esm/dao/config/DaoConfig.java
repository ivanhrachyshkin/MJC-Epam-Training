package com.epam.esm.dao.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.time.Clock;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "classpath:application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})
public class DaoConfig {

    @Value("${driver}")
    String driver;
    @Value("${url}")
    String url;
    @Value("${user}")
    String userName;
    @Value("${password}")
    String password;
    @Value("${dialect}")
    String hibernateDialect;
    @Value("${showSql}")
    String showSql;

    @Bean
    public DataSource dataSource()
            throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SessionFactory getSessionFactory() throws PropertyVetoException, IOException {
        final Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", showSql);
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.epam.esm");
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }


    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
