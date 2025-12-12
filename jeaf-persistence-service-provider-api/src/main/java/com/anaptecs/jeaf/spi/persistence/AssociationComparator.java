/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.Identifiable;

/**
 * Class is intended to help by the comparison of changes in associations objects. The class can be used by service
 * implementations to check which objects were added to an association and which were removed.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.2
 * 
 * @param <I>
 * @param <PO>
 */
public class AssociationComparator<I extends Identifiable<? extends AbstractObjectID<?>>, PO extends PersistentObject> {
  /**
   * Map contains all objects that should be used as basis for the comparison.
   */
  private final Map<AbstractObjectID<?>, I> identifiableObjectMap;

  /**
   * Map contains all persistent objects that should be used as basis for the comparison.
   */
  private final Map<AbstractObjectID<?>, PO> persistentObjectMap;

  /**
   * Collection contains all new objects.
   */
  private final Collection<I> newObjects = new LinkedList<I>();

  /**
   * Initialize object. Therefore the collection of identifiable and persistent objects has to be passed.
   * 
   * @param pObjects Collection of objects that should be compared. The parameter must not be null.
   * @param pPersistentObjects Collection of persistent objects that should be compared. The parameter must not be null.
   */
  public AssociationComparator( Collection<I> pObjects, Collection<PO> pPersistentObjects ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pObjects, "pObjects");
    Check.checkInvalidParameterNull(pPersistentObjects, "pPersistentObjects");

    // Build up map with all objects that are not new.
    identifiableObjectMap = new HashMap<AbstractObjectID<?>, I>(pObjects.size());
    for (I lNextObject : pObjects) {
      // Check id of object.
      AbstractObjectID<?> lObjectID = lNextObject.getID();

      // Object already exists, so add it to map.
      if (lObjectID != null) {
        identifiableObjectMap.put(lObjectID, lNextObject);
      }
      // Object does not have a object id and thus must be new.
      else {
        newObjects.add(lNextObject);
      }
    }

    // Build up map with all persistent objects.
    persistentObjectMap = new HashMap<AbstractObjectID<?>, PO>(pPersistentObjects.size());

    for (PO lNextPersistentObject : pPersistentObjects) {
      // Convert id of persistent object and add it to map.
      AbstractObjectID<?> lObjectID = lNextPersistentObject.getID();
      persistentObjectMap.put(lObjectID, lNextPersistentObject);
    }
  }

  /**
   * Method returns all new objects. New objects are detected as they have no objects id yet.
   * 
   * @return {@link Collection} Collection with all new objects. The method never returns null.
   */
  public Collection<I> getNewObjects( ) {
    return Collections.unmodifiableCollection(newObjects);
  }

  /**
   * Method returns all objects that were added to the collection of objects compared to the persistent objects.
   * Therefore the method checks the following criteria: If the list of persistent objects does not contain any object
   * with an id that matches to the id of a object. This does not include new objects
   * 
   * @return {@link Collection} Collection with all objects that were added compared to the collection of persistent
   * objects. The collection does not included new objects that were added.
   * 
   * @see #getNewObjects()
   */
  public Collection<I> getAddedObjects( ) {
    // Check for every object if there also is a persistent object.
    Collection<I> lAddedObjects = new LinkedList<I>();
    for (I lNextObject : identifiableObjectMap.values()) {
      // Check if next object was already part of the association. If the object id is not included in
      // the map of persistent objects than the object was added.
      if (persistentObjectMap.containsKey(lNextObject.getID()) == false) {
        lAddedObjects.add(lNextObject);
      }
    }
    return lAddedObjects;
  }

  /**
   * Method returns all added and new objects compared to the collection of persistent objects. For further details
   * please see {@link #getAddedObjects()} and {@link #getNewObjects()}
   * 
   * @return {@link Collection} Collection with all added and new objects. The method never returns null.
   */
  public Collection<I> getAddedAndNewObjects( ) {
    Collection<I> lAddedObjects = this.getAddedObjects();
    ArrayList<I> lServiceObjects = new ArrayList<I>(newObjects.size() + lAddedObjects.size());
    lServiceObjects.addAll(newObjects);
    lServiceObjects.addAll(lServiceObjects);
    return lServiceObjects;
  }

  /**
   * Method returns all service objects that were not added to the collection of service objects compared to the list of
   * persistent objects. This are the "already existing objects".
   * 
   * @return {@link Collection} Collection with all service objects that already were part of the collection before the
   * other objects were added. The method never returns null.
   */
  public Collection<I> getAlreadyExistingServiceObjects( ) {
    ArrayList<I> lExistingServiceObjects = new ArrayList<I>(persistentObjectMap.size());

    for (AbstractObjectID<?> lNextObjectID : persistentObjectMap.keySet()) {
      I lServiceObject = identifiableObjectMap.get(lNextObjectID);
      if (lServiceObject != null) {
        lExistingServiceObjects.add(lServiceObject);
      }
    }
    return lExistingServiceObjects;
  }

  /**
   * Method returns all persistent objects that should be removed. Therefore the method compares the collection of
   * service objects with the collection of persistent objects.
   * 
   * @return {@link Collection} Collection with all persistent objects that shpuld be removed in order to have the both
   * collection in sync again.
   */
  public Collection<PO> getPersistentObjectsToRemove( ) {
    // Check for every persistent object if there still is a service object.
    Collection<PO> lPersistentObjectsToRemove = new LinkedList<PO>();

    for (Entry<AbstractObjectID<?>, PO> lNextEntry : persistentObjectMap.entrySet()) {
      // Check if id of persistent object is still in map with all service objects.
      AbstractObjectID<?> lNextID = lNextEntry.getKey();
      if (identifiableObjectMap.containsKey(lNextID) == false) {
        PO lPersistentObject = lNextEntry.getValue();
        lPersistentObjectsToRemove.add(lPersistentObject);
      }
    }
    // Return all persistent objects that should be removed.
    return lPersistentObjectsToRemove;
  }
}
