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
 * Annotation can be used to define container managed connections for a persistence unit.
 * 
 * @author JEAF Development Team
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ContainerManagedConnections {
  /**
   * Lookup name of the JTA datasource that should be used in container-managed environments. Either
   * {@link #jtaDataSource()} or {@link #nonJTADataSource()} has to be defined but non both or none.
   */
  String jtaDataSource() default "";

  /**
   * Lookup name of the nonJTA datasource that should be used in container-managed environments. Either
   * {@link #jtaDataSource()} or {@link #nonJTADataSource()} has to be defined but non both or none.
   */
  String nonJTADataSource() default "";

  /**
   * Attribute defines if classes that are not explicitly mentioned should be excluded from the persistence unit or not.
   * Class can be defined using {@link PersistenceUnit#managedClassesDefinition},
   * {@link PersistenceUnit#mappingFilesDefinition} or {@link PersistenceUnit#jarFiles}.
   */
  boolean excludeUnlistedClasses();

  /**
   * List of properties that belong to the persistence unit. These properties are provided to the persistence provider
   * during its initialization. The attribute supports replacement of place holders by system properties.
   */
  Property[] properties() default {};
}
