/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence.annotations.test;

import com.anaptecs.jeaf.spi.persistence.annotations.ContainerManagedConnections;
import com.anaptecs.jeaf.spi.persistence.annotations.PersistenceConfig;
import com.anaptecs.jeaf.spi.persistence.annotations.PersistenceUnit;

@PersistenceConfig(
    persistenceUnitConfigClass = MyJEEPersistenceUnit.class,
    containerManagedConnectionDefinition = MyJEEPersistenceUnit.class)

@PersistenceUnit(
    name = "MyJEEPersistenceUnit",
    managedClassesDefinition = EntityMappings.class,
    mappingFilesDefinition = EntityMappings.class)

@ContainerManagedConnections(jtaDataSource = "x.y.z", excludeUnlistedClasses = true)

public interface MyJEEPersistenceUnit {

}
