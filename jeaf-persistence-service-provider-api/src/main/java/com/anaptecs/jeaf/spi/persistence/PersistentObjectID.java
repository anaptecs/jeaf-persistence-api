/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.spi.persistence;

import com.anaptecs.jeaf.core.api.DomainObjectID;
import com.anaptecs.jeaf.core.api.ServiceObjectID;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.types.Base36;

/**
 * Class is used to have a special type for ids of persistent object which are always persistent.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class PersistentObjectID extends AbstractObjectID<PersistentObjectID> {
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize object. Therefore the id value has to be passed to the object.
   * 
   * @param pObjectID ID value that is used by this object. The parameter must not be null.
   * @param pVersionLabel Version label of the service object. The parameter may be null.
   */
  public PersistentObjectID( String pObjectID, Integer pVersionLabel ) {
    super(pObjectID, pVersionLabel);
  }

  /**
   * Initialize with the passed object id.
   * 
   * @param pAbstractObjectID Abstract object id that is used to create a new one. The parameter must not be null.
   */
  public PersistentObjectID( AbstractObjectID<?> pAbstractObjectID ) {
    super(pAbstractObjectID);
  }

  /**
   * Method returns whether the represented object id has a delimiter or not.
   * 
   * @return boolean Method returns true if the object id has a delimiter and otherwise false.
   */
  @Override
  public boolean hasDelimiter( ) {
    return true;
  }

  /**
   * Method returns the delimiter in case that the object id contains delimiters.
   * 
   * @return char Delimiter of the object id or null if the object id does not contain a delimiter.
   */
  @Override
  public char getDelimiter( ) {
    return '#';
  }

  /**
   * Method returns a unversioned variant of this object id. In opposite to an version object id an unversioned one can
   * be stored as it does not contain any version information about an object.
   * 
   * @return {@link PersistentObjectID} Unversioned variant of this object ID. The method never returns null.
   */
  @Override
  public PersistentObjectID getUnversionedObjectID( ) {
    PersistentObjectID lUnversionedObjectID;
    if (this.isVersioned() == true) {
      lUnversionedObjectID = new PersistentObjectID(this.getObjectID(), null);
    }
    else {
      lUnversionedObjectID = this;
    }
    return lUnversionedObjectID;
  }

  /**
   * Method creates a new service object id object that corresponds to this persistent object id. This conversion is
   * basically needed because persistent objects and their id objects are only for service internal use and must not be
   * passed to clients of a service.
   * 
   * @return {@link ServiceObjectID} ID object to that this object was transformed. The method never returns null.
   */
  public ServiceObjectID transformToServiceObjectID( ) {
    // Create new ServiceObjectID object.
    // TODO Transform persistent object id to service object id in a way that security issues are considered. Primary
    // keys should not be sent to the client as this may be a security leak.
    return new ServiceObjectID(this.getObjectID().replace('#', '-'), this.getVersionLabel());
  }

  /**
   * Method creates a new domain object id object that corresponds to this persistent object id. This conversion is
   * basically needed because persistent objects and their id objects are only for service internal use and must not be
   * passed to clients of a service.
   * 
   * @return {@link DomainObjectID} ID object to that this object was transformed. The method never returns null.
   */
  public DomainObjectID transformToDomainObjectID( ) {
    // Create new ServiceObjectID object.
    // TODO Transform persistent object id to service object id in a way that security issues are considered. Primary
    // keys should not be sent to the client as this may be a security leak.
    return new DomainObjectID(this.getObjectID().replace('#', '-'), this.getVersionLabel());
  }

  /**
   * Method creates a persistent object id from the passed object id.
   * 
   * @param pObjectID Object id that should be transformed. The parameter must not be null.
   * @return {@link PersistentObjectID} Persistent object id that was created from the passed object id. The method
   * never returns null. If the passed object id is an persistent object id the the object will be returned.
   */
  public static PersistentObjectID create( AbstractObjectID<?> pObjectID ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pObjectID, "pObjectID");

    PersistentObjectID lPersistentObjectID;
    if (pObjectID instanceof PersistentObjectID) {
      lPersistentObjectID = (PersistentObjectID) pObjectID;
    }
    else {
      lPersistentObjectID =
          new PersistentObjectID(pObjectID.getObjectID().replace('-', '#'), pObjectID.getVersionLabel());
    }
    return lPersistentObjectID;
  }

  /**
   * Method transforms the passed service object id to a persistent object id.
   * 
   * @param pServiceObjectID Service object id that should be transformed. The parameter must not be null.
   * @return {@link PersistentObjectID} Persistent object id that was created from the passed service object id. The
   * method never returns null.
   */
  public static PersistentObjectID createFromServiceObjectID( ServiceObjectID pServiceObjectID ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pServiceObjectID, "pServiceObjectID");

    return new PersistentObjectID(pServiceObjectID.getObjectID().replace('-', '#'), pServiceObjectID.getVersionLabel());
  }

  /**
   * Method transforms the passed domain object id to a persistent object id.
   * 
   * @param pDomainObjectID Service object id that should be transformed. The parameter must not be null.
   * @return {@link PersistentObjectID} Persistent object id that was created from the passed domain object id. The
   * method never returns null.
   */
  public static PersistentObjectID createFromDomainObjectID( DomainObjectID pDomainObjectID ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pDomainObjectID, "pDomainObjectID");

    return new PersistentObjectID(pDomainObjectID.getObjectID().replace('-', '#'), pDomainObjectID.getVersionLabel());
  }

  /**
   * Method returns the class object of the persistent object to which this persistent object id belongs to.
   * 
   * @return {@link Class} Class object of the persistent object to which this id belongs. The method never returns
   * null.
   */
  public Class<? extends PersistentObject> getPersistentObjectClass( ) {
    // The class id are the last three digits of the objects id which in total if 15 characters long. Thus the
    // characters from index 12 - 14 are the class id.
    String lClassIDString = this.getObjectID().substring(12);

    // Convert class id to integer and return class object.
    Base36 lBase36 = new Base36(lClassIDString);
    return ClassID.getClass(lBase36.toInteger());
  }
}
