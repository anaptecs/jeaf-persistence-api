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
 * Annotation can be used to define properties for a persistence unit.
 * 
 * @author JEAF Development Team
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Property {
  /**
   * Name of the property. This must be a real string.
   * 
   * The attribute supports replacement of place holders by system properties.
   */
  String name();

  /**
   * Value of the property.
   * 
   * The attribute supports replacement of place holders by system properties.
   */
  String value();
}
