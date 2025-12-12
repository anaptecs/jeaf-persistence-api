/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Driver;

import javax.persistence.spi.PersistenceUnitTransactionType;

/**
 * Annotation can be used to define the application managed connections of a persistence unit.
 * 
 * @author JEAF Development Team
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationManagedConnections {
  /**
   * Transaction type of the persistence unit.
   */
  PersistenceUnitTransactionType transactionType() default PersistenceUnitTransactionType.RESOURCE_LOCAL;

  /**
   * JDBC Driver that should be used.
   */
  Class<? extends Driver> jdbcDriver();

  /**
   * JPA JDBC dialect that should be used.
   */
  String dialect() default "";

  /**
   * JDBC connection URL string to access database. The attribute supports replacement of place holders by system
   * properties.
   */
  String connectionURL();

  /**
   * User name for database access. The attribute supports replacement of place holders by system properties.
   */
  String username() default "";

  /**
   * Password for database access. The attribute supports replacement of place holders by system properties.
   */
  String password() default "";

  /**
   * Connection pool definition for the persistence unit. If no connection pool is defined then a default variant will
   * be used.
   */
  ConnectionPool connectionPool() default @ConnectionPool;

  /**
   * List of properties that belong to the persistence unit. These properties are provided to the persistence provider
   * during its initialization. The attribute supports replacement of place holders by system properties.
   */
  Property[] properties() default {};
}
