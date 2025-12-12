/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface is intended to be used in order to execute JDBC / Connection specific work inside an transaction.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.3
 * 
 * @see PersistenceServiceProvider#executeJDBCWorker(JDBCWorker)
 */
public interface JDBCWorker {
  /**
   * Method execute JDBC / Connection specific work on the passed connection. As connection the persistence provider
   * will always pass the connection of the current transaction.
   * 
   * @param pConnection Connection on which the work should be executed. The parameter is never null.
   * @throws SQLException if any exception occurs during the work on the connection.
   * 
   * @see PersistenceServiceProvider#executeJDBCWorker(JDBCWorker)
   */
  public void execute( Connection pConnection ) throws SQLException;
}
