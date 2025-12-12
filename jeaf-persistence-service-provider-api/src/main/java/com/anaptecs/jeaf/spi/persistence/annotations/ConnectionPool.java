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

/**
 * Annotation can be used to define a connection pool for application managed connections of a persistence unit.
 * 
 * @author JEAF Development Team
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConnectionPool {
  /**
   * Minimum size of the connection pool. The attribute must be a valid positive integer (>=0). The attribute supports
   * replacement of place holders by system properties.
   */
  String minSize() default "0";

  /**
   * Maximum size of the connection pool. The attribute must be a valid positive integer (>0). The attribute supports
   * replacement of place holders by system properties.
   */
  String maxSize() default "1";

  /**
   * Step size with which the poll will be incremented. The attribute must be a valid positive integer (>0). The
   * attribute supports replacement of place holders by system properties.
   */
  String incrementSize() default "1";

  /**
   * Size of the prepared statement cache of the persistence unit. The attribute must be a valid positive integer (>=0).
   * The attribute supports replacement of place holders by system properties.
   */
  String preparedStatementCacheSize() default "0";

  /**
   * Period after which a connection is tested before it is used when taken from the pool. The attribute must be a valid
   * positive integer (>0). The attribute supports replacement of place holders by system properties.
   */
  String idleTestPeriod() default "100";

  /**
   * The timeout property specifies the number of seconds an unused connection will be kept before being discarded. By
   * default, connections will never expire from the pool. The attribute must be a valid positive integer (>=0). The
   * attribute supports replacement of place holders by system properties.
   */
  String timeout() default "";
}
