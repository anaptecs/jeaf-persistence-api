/**
 * Copyright 2004 - 2016 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence;

/**
 * Enum defines the possible state a persistent object may have.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.3
 */
public enum EntityState {
  /**
   * Object was created, read or updated within the current transaction
   */
  Active,

  /**
   * Object was deleted within the current transaction.
   */
  Deleted;
}
