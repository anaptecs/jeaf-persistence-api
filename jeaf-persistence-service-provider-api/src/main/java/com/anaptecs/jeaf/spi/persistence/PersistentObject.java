/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.spi.persistence;

import javax.persistence.Entity;
import javax.persistence.PostUpdate;

import com.anaptecs.jeaf.core.api.JEAF;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.common.Identifiable;

/**
 * This class is the technical base class for all persistent objects. It contains more or less all technical overhead
 * that is required to make persistent objects persistent.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
@Entity
public abstract class PersistentObject implements Identifiable<PersistentObjectID> {

  /**
   * Name of the row where the Class ID of the object is stored.
   */
  public static final String CLASS_ID = "CLASS_ID";

  /**
   * Name of the row where the OID / primary key of the object is stored.
   */
  public static final String OID = "OID";

  /**
   * Name of the object ID attribute.
   */
  public static final String OBJECT_ID = "objectID";

  /**
   * The object id contains the technical key of an persistent objects. Since persistent objects in JEAF are always
   * persistent the object id is the equivalent to the databases primary key. The object id must never be null and has
   * to be assigned to persistent objects when they are created new via the persistence provider.
   * 
   * In order to provide access to the object id to persistence frameworks this class offers the methods
   * <code>getObjectID()</code> and <code>setObjectID(...)</code>.
   * 
   * @see #getObjectID()
   * @see #setObjectID(String)
   */
  private String objectID;

  /**
   * Attribute contains the version label that is used in order to implement optimistic locking. The version label is
   * maintained by the used persistence framework.
   */
  private int versionLabel;

  /**
   * Attribute defines the state of the entity with a transaction.
   */
  private EntityState entityState;

  /**
   * Attribute contains the persistent object id of this object. This is just another representation of the object id
   * value. Unlike the <code>objectID</code> attribute which is intended to be used by persistence frameworks the
   * reference to the persistent object id is intended to be used in "regular" code that deals with persistent objects.
   */
  private PersistentObjectID persistentObjectID;

  /**
   * Default constructor of this class. Since most persistence frameworks use Java's reflection API at least to create
   * objects all persistent objects have to provide a parameterless default constructor with package visibility.
   */
  protected PersistentObject( ) {
    entityState = EntityState.Active;
  }

  /**
   * Method returns the persistent object id of this class. The persistent object id can be used to identify a
   * persistent object. The returned object can also be used as key within maps etc.
   * 
   * @return PersistentObjectID Object to identify this persistent object. The method never returns null.
   */
  @Override
  public final PersistentObjectID getID( ) {
    // Since persistence frameworks like Hibernate provide transaction isolation calls of this method should not occur
    // from two threads. However even if it will happen no problems will occur since the created object has a proper
    // implemented equals method. So that in this case only one persistent object too much is created.
    if (persistentObjectID == null) {
      // As persistence frameworks generate subclasses at runtime to control access on fields the access methods for the
      // fields of the class have to be used.
      persistentObjectID = new PersistentObjectID(this.getObjectID(), this.getVersionLabel());
    }

    // Return persistent object id.
    return persistentObjectID;
  }

  /**
   * Method returns the unversioned persistent object id of this class. The persistent object id can be used to identify
   * a persistent object. The returned object can also be used as key within maps etc.
   * 
   * @return PersistentObjectID Object to identify this persistent object. The method never returns null.
   */
  @Override
  public PersistentObjectID getUnversionedID( ) {
    return this.getID().getUnversionedObjectID();
  }

  /**
   * Method returns the persistent object id of this class. The persistent object id can be used to identify a
   * persistent object. The returned object can also be used as key within maps etc.
   * 
   * @return PersistentObjectID Object to identify this persistent object. The method never returns null.
   * 
   * @deprecated Please use {@link #getID()} instead
   */
  @Deprecated
  public final PersistentObjectID getPersistentObjectID( ) {
    return this.getID();
  }

  /**
   * Method returns the object id of this object. This method is intended to be called by persistence frameworks
   * whenever they require the object id of an object.
   * 
   * The method has protected visibility since Hibernate works with dynamic proxies for persistent objects in order to
   * provide lazy loading. As soon as this method has not the visibility protected or more the lazy loading mechanism of
   * Hibernate will not work anymore for the object id. For the same reason it must not be final.
   * 
   * @return String Id of the persistent object. This method can only return null in circumstances of a wrong use of
   * JEAFs persistence capabilities. In order to avoid strange effect due to a not set object id, the method throws an
   * assertions when this method will be call and the object id is null.
   */
  protected String getObjectID( ) {
    // Ensure that an object id has already been set.
    Assert.assertNotNull(objectID, "persistentObject.objectID");

    // Return object id.
    return objectID;
  }

  /**
   * Method sets the object id of this object. This method is intended to be called by persistence frameworks in order
   * to set the objects object id when it is read from the database.
   * 
   * @param pObjectID Id of the object. The parameter must not be null.
   */
  private void setObjectID( String pObjectID ) {
    // Check parameter for null.
    Assert.assertNotNull(pObjectID, "pObjectID");

    // Assign object id and create new persistent object id.
    objectID = pObjectID;
  }

  /**
   * This method is intended to be called by the persistence provider when a new persistent object and before the object
   * will be returned to any persistence provider client.
   * 
   * @param pObjectID Object id of this object. The parameter must not be null and the method must only be called if no
   * object id was assigned before.
   */
  public final void assignObjectID( String pObjectID ) {
    // Ensure that no object id is set.
    Assert.assertNull(objectID, "pObjectID");

    // Assign passed object id.
    this.setObjectID(pObjectID);
  }

  /**
   * Method returns the version label of this object.
   * 
   * @return int Version label of this object.
   */
  public int getVersionLabel( ) {
    return versionLabel;
  }

  /**
   * Method sets the version label of this persistent object. This method is only intended to be used by persistence
   * frameworks
   * 
   * @param pVersionLabel Current version label of this object.
   */
  public void setVersionLabel( int pVersionLabel ) {
    versionLabel = pVersionLabel;
  }

  /**
   * Method returns the class id of this persistent object class.
   * 
   * @return {@link ClassID} Class ID of this persistent object. The method must not return null.
   */
  public abstract ClassID getClassID( );

  /**
   * Method compares the passed object with this object as defined by the Java Language Specification. To persistent
   * objects which are persistent this means that they either have to be the same instance or at least must have the
   * same object id (= primary key on the database).
   * 
   * This method must not be called before an object id has been assigned to this object.
   * 
   * @param pObject Object that should be compared to this instance. The parameter may be null. In this case the method
   * will return false.
   * @return boolean The method return <code>true</code> if the passed object is equal to this instance as defined above
   * and <code>false</code> in all other cases.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals( Object pObject ) {
    // Ensure that an object id has already been set.
    Assert.assertNotNull(this.getObjectID(), "persistentObject.objectID");

    boolean lIsEqual;
    // Passed object is null, so this method has to return false.
    if (pObject == null) {
      lIsEqual = false;
    }
    // Passed object is the same instance.
    else if (this == pObject) {
      lIsEqual = true;
    }
    // Passed object is not the same instance, so check the object id, if they are instances of the same class.
    else {
      if (pObject instanceof PersistentObject) {
        PersistentObject lPersistentObject = (PersistentObject) pObject;
        lIsEqual = objectID.equals(lPersistentObject.getObjectID());
      }
      // Objects are not instance of the same class.
      else {
        lIsEqual = false;
      }
    }
    // Return result of comparison.
    return lIsEqual;
  }

  /**
   * Method returns the hash code of this object.
   * 
   * @return int Hash code of this object.
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode( ) {
    // Ensure that an object id has already been set.
    Assert.assertNotNull(objectID, "PersistentObject.objectID");

    // Return hash code of object id.
    return this.getObjectID().hashCode();
  }

  /**
   * Method removes this object from the database. According to the defined delete rules associated object may also be
   * deleted. The object will be deleted from the database within the context of the current transaction.
   */
  public void delete( ) {
    PersistentObject.getPersistenceServiceProvider().delete(this);
  }

  public final void deleted( ) {
    entityState = EntityState.Deleted;
  }

  /**
   * Method returns the current state of the persistent object.
   * 
   * @return {@link EntityState} State of this persistent object. The method never returns null.
   */
  public final EntityState getEntityState( ) {
    return entityState;
  }

  /**
   * Method returns a reference to the current persistence service provider.
   * 
   * @return {@link PersistenceServiceProvider} Reference to persistence service provider implementation. The method
   * never returns null.
   */
  public static PersistenceServiceProvider getPersistenceServiceProvider( ) {
    return JEAF.getServiceProvider(PersistenceServiceProvider.class);
  }

  /**
   * This method is highly dependent the used persistence framework. Some persistence frameworks use so called proxies
   * that are created at runtime to support lazy loading n:1 associations as there are no collection interfaces that can
   * hide a lazy loading supporting implementation. However there may occur situation where this behavior leads to
   * problems as operations such as "instanceof" won't work.
   * 
   * Further information about the problem can be found here:
   * http://pwu-developer.blogspot.com/2010/11/hibernate-lazy-proxied-objects-and.html
   * 
   * JEAF solves this problem as for all get method of non multi valued associations a potential proxy will be unwrapped
   * by default. Thus this method should only be called directly in rare circumstances.
   * 
   * @param <T> Type
   * @param pProxyPO Potential proxy of a persistent object that should be unwrapped. The parameter may be null.
   * @return T Unwrapped persistent object if required for the used persistence framework. If null was passed to the
   * method null will also be returned.
   * 
   * @see PersistenceServiceProvider#unproxy(PersistentObject)
   */
  public final <T extends PersistentObject> T unproxy( T pProxyPO ) {
    // May be existing proxies can only be unwrapped if JEAF already is initialized. In rare circumstances (e.g. JAAS
    // Login Modules) this can not be ensured. In this cases the passed object will be returned.
    T lPersistentObject;
    if (JEAF.isCoreInitialized() == true) {
      PersistenceServiceProvider lPersistenceServiceProvider = PersistentObject.getPersistenceServiceProvider();
      lPersistentObject = lPersistenceServiceProvider.unproxy(pProxyPO);
    }
    else {
      lPersistentObject = pProxyPO;
    }
    return lPersistentObject;
  }

  /**
   * Method is intended to be called by Hibernate as soon as the object was updated either by flushing it or by
   * committing it to the database. An update of an object causes that its version label is incremented. This means that
   * the cached persistence object id becomes invalid.
   */
  @PostUpdate
  void updateObject( ) {
    // As the persistent object id became invalid by the update we set it to null. The next time it will e accesses it
    // will be created again.
    persistentObjectID = null;
  }
}
