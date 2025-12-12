/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anaptecs.jeaf.spi.persistence.PersistenceServiceProvider;

/**
 * Annotation can be used to define the persistence configuration for JEAF's Persistence Service Provider. The usage of
 * this annotation is an alternative to persistence.xml which is the JPA standard mechanism. However the standard
 * mechanism is not very flexible and cases configuration redundancy if a persistence unit should be used in multiple
 * environments e.g. JUnit tests and inside an applications server.
 * 
 * Using this annotation and the connected ones eliminates this redundancy ;-)
 * 
 * @author JEAF Development Team
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PersistenceConfig {
  /**
   * Name of the resource that contains the name of the classes with the @PersistenceConfig annotation.
   */
  String PERSISTENCE_CONFIG_RESOURCE_NAME = "PersistenceConfig";

  /**
   * Path under which the configuration file is stored.
   */
  String PERSISTENCE_CONFIG_PATH =
      PersistenceServiceProvider.PERSISTENCE_BASE_PATH + '/' + PERSISTENCE_CONFIG_RESOURCE_NAME;

  /**
   * Reference to class that holds the {@link PersistenceUnit} annotation.
   */
  Class<?> persistenceUnitConfigClass();

  /**
   * Reference to class that holds the {@link ApplicationManagedConnections} annotation.
   */
  Class<?> applicationManagedConnectionDefinition() default Object.class;

  /**
   * Reference to class that holds the {@link ContainerManagedConnections} annotation.
   * 
   * @return
   */
  Class<?> containerManagedConnectionDefinition() default Object.class;
}
