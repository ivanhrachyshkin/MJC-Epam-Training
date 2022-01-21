package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.time.Clock;

@SuppressWarnings("SpringPropertySource")
@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@PropertySource(value = {"classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
public class ApplicationConfig {

    @Bean
    public DataSource dataSource(@Value("${driver}") final String driver,
                                 @Value("${url}") final String url,
                                 @Value("${user}") final String userName,
                                 @Value("${password}") final String password)
            throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
