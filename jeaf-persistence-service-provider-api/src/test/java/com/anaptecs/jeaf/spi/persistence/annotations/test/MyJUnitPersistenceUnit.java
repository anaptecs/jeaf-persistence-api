/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence.annotations.test;

import javax.persistence.spi.PersistenceUnitTransactionType;

import com.anaptecs.jeaf.spi.persistence.annotations.ApplicationManagedConnections;
import com.anaptecs.jeaf.spi.persistence.annotations.PersistenceConfig;
import com.anaptecs.jeaf.spi.persistence.annotations.PersistenceUnit;
import com.anaptecs.jeaf.spi.persistence.annotations.Property;
import com.mysql.cj.jdbc.Driver;

@PersistenceConfig(
    persistenceUnitConfigClass = MyJUnitPersistenceUnit.class,
    applicationManagedConnectionDefinition = MyJUnitPersistenceUnit.class)

@PersistenceUnit(
    name = "MyJUnitPersistenceUnit",
    managedClassesDefinition = EntityMappings.class,
    mappingFilesDefinition = EntityMappings.class,

    properties = { @Property(name = "hibernate.dialect", value = "org.hibernate.dialect.MySQL5Dialect"),
      @Property(name = "hibernate.connection.driver_class", value = "com.mysql.cj.jdbc.Driver") })

@ApplicationManagedConnections(
    transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL,
    jdbcDriver = Driver.class,
    connectionURL = "jdbc:mysql://localhost:3306/jeaf_test?useSSL=false&serverTimezone=CET")

public interface MyJUnitPersistenceUnit {
}
