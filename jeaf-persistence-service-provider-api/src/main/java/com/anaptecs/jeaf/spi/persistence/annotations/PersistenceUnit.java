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

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceProvider;

/**
 * Annotation can be used to define a persistence unit. This annotation belongs to a persistence configuration and
 * contains all parts that usually do not depend on a specific runtime environment.
 * 
 * @author JEAF Development Team
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PersistenceUnit {
  /**
   * Name of the persistence unit.
   */
  String name();

  /**
   * Reference to class that holds the managed classes of the persistence unit by using annotation
   * {@link ManagedClasses}.
   * 
   * The attribute is optional. However at least one of the following fields must be defined:
   * {@link #managedClassesDefinition}, {@link #mappingFilesDefinition} or {@link #jarFiles}
   */
  Class<?> managedClassesDefinition() default Object.class;

  /**
   * Reference to class that holds the mapping files of the persistence unit by using annotation {@link MappingFiles}.
   * 
   * The attribute is optional. However at least one of the following fields must be defined:
   * {@link #managedClassesDefinition}, {@link #mappingFilesDefinition} or {@link #jarFiles}
   */
  Class<?>[] mappingFilesDefinition() default Object.class;

  /**
   * List of JAR files that contain the classes that belong to this persistence unit. The attribute is optional. However
   * at least one of the following fields must be defined: {@link #managedClassesDefinition},
   * {@link #mappingFilesDefinition} or {@link #jarFiles}
   * 
   * The attribute supports replacement of place holders by system properties.
   */
  String[] jarFiles() default {};

  /**
   * List of properties that belong to the persistence unit. These properties are provided to the persistence provider
   * during its initialization. The attribute supports replacement of place holders by system properties.
   */
  Property[] properties() default {};

  /**
   * Shared cache mode of the persistence unit.
   */
  SharedCacheMode sharedCacheMode() default SharedCacheMode.UNSPECIFIED;

  /**
   * Validation mode of the persistence unit.
   */
  ValidationMode validationMode() default ValidationMode.AUTO;

  /**
   * Attribute defines of SQL statements that belong to this persistence unit should be trace. The attribute supports
   * replacement of place holders by system properties.
   */
  String showSQL() default "true";

  /**
   * Attribute defines if SQL statements should be formatted when they are traced. The attribute supports replacement of
   * place holders by system properties.
   */
  String formatSQL() default "true";

  /**
   * Implementation of the persistence provider that should be used. The attribute is optional.
   */
  Class<? extends PersistenceProvider> persistenceProvider() default PersistenceProvider.class;

}
