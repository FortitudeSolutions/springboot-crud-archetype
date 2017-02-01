package com.example.service;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Defines database configuration for this service
 */
@SuppressWarnings({"ALL", "unused"})
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationEmf",
    transactionManagerRef = "applicationTm",
    basePackages = {"com.example.service.hello.data"}
)
@EnableTransactionManagement
public class JpaConfig {

  @Bean(name = "applicationDs")
  @ConfigurationProperties("datasource.default")
  public DataSource applicationDataSource() {
    return new ComboPooledDataSource();
  }


  @Bean(name = "applicationEmf")
  public LocalContainerEntityManagerFactoryBean applicationEmfFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("applicationDs") DataSource ds) {

    return builder
        .dataSource(ds)
        .packages("com.example.service.hello.data")
        .persistenceUnit("applicationPersistence")
        .build();
  }

  @Bean(name = "applicationTm")
  public PlatformTransactionManager applicationTransactionManager(
      @Qualifier("applicationEmf") EntityManagerFactory entityManager
  ) {
    return new JpaTransactionManager(entityManager);

  }
}
