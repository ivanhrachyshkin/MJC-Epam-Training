package com.epam.esm.dao.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "db")
@Getter
@Setter
@NoArgsConstructor
public class DataSourceProperties {

   private String driver;
   private String url;
   private String user;
   private String password;
   private String dialect;
   private String showSql;
}
