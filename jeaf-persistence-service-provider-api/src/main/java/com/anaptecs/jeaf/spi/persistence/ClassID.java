/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence;

import java.util.HashMap;
import java.util.Map;

import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.types.Base36;

/**
 * A class id is a unique representation of the type of a business object. It is a base 36 encoded number with a max
 * length of 3 digits.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class ClassID extends Base36 {
  /**
   * Reference to class object of business objects whose type is represented by this class id. The reference is never
   * null.
   */
  private final Class<? extends PersistentObject> businessObjectType;

  /**
   * Default serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The length of the SessionId.
   */
  public static final int MAX_LENGTH = 3;

  /**
   * Map contains all created class ids with its id value as key.
   */
  private static final Map<Integer, ClassID> CLASS_IDS_BY_VALUE = new HashMap<Integer, ClassID>();

  /**
   * Map contains all created class ids with its class object as key.
   */
  private static final Map<Class<? extends PersistentObject>, ClassID> CLASS_IDS_BY_BO_CLASS =
      new HashMap<Class<? extends PersistentObject>, ClassID>();

  /**
   * Map contains the class objects of all business objects with their class id as key.
   */
  private static final Map<Integer, Class<? extends PersistentObject>> CLASSES_BY_ID =
      new HashMap<Integer, Class<? extends PersistentObject>>();

  /**
   * Constructor calls his parent an gets the SessionId with the length of 6 digits.
   * 
   * @param pIDValue Value of the class id as integer.
   * @param pBusinessObjectType Type of business object that is represented by this class id. The parameter must not be
   * null.
   */
  private ClassID( int pIDValue, Class<? extends PersistentObject> pBusinessObjectType ) {
    super(pIDValue, MAX_LENGTH);
    businessObjectType = pBusinessObjectType;
  }

  /**
   * Method creates a new instance of a class id with the passed values. The method ensures that neither the id value
   * nor the class object is used twice.
   * 
   * @param pIDValue Value of the class id as integer.
   * @param pBusinessObjectType Type of business object that is represented by this class id. The parameter must not be
   * null.
   * @return ClassID Created object. The method never returns null.
   */
  public static synchronized ClassID createClassID( int pIDValue,
      Class<? extends PersistentObject> pBusinessObjectType ) {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pBusinessObjectType, "pBusinessObjectType");

    // Ensure that neither class id value nor class are already used.
    // ClassID value is already in use.
    if (CLASS_IDS_BY_VALUE.containsKey(pIDValue) == true) {
      final String[] lParams =
          new String[] { Integer.toString(pIDValue), CLASS_IDS_BY_VALUE.get(pIDValue).businessObjectType.toString() };
      throw new JEAFSystemException(PersistenceServiceProviderMessages.CLASS_ID_VALUE_ALREADY_IN_USE, lParams);
    }
    // Class is already in use.
    if (CLASS_IDS_BY_BO_CLASS.containsKey(pBusinessObjectType)) {
      ClassID lClassID = CLASS_IDS_BY_BO_CLASS.get(pBusinessObjectType);
      String[] lParams =
          new String[] { pBusinessObjectType.toString(), lClassID.toString(), Integer.toString(lClassID.toInteger()) };
      throw new JEAFSystemException(PersistenceServiceProviderMessages.CLASS_ALREADY_IN_USE, lParams);
    }

    // Create new ClassID and register it within maps.
    final ClassID lClassID = new ClassID(pIDValue, pBusinessObjectType);
    CLASS_IDS_BY_VALUE.put(pIDValue, lClassID);
    CLASS_IDS_BY_BO_CLASS.put(pBusinessObjectType, lClassID);
    CLASSES_BY_ID.put(pIDValue, pBusinessObjectType);
    return lClassID;
  }

  /**
   * Method returns the class object of the business object that has the passed class id value.
   * 
   * @param pClassID Class id of the business object whose class object should be returned.
   * @return {@link Class} Class object that belongs to the passed class id. The method never returns null.
   */
  public static synchronized Class<? extends PersistentObject> getClass( int pClassID ) {
    Class<? extends PersistentObject> lClass = CLASSES_BY_ID.get(pClassID);
    Assert.assertNotNull(lClass, "ClassID.getClass(int)");
    return lClass;
  }

  /**
   * Method returns the type of the business object that is represented by this class id.
   * 
   * @return Class Class object of the represented business object.
   */
  public Class<? extends PersistentObject> getBusinessObjectType( ) {
    return businessObjectType;
  }
}
