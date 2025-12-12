/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.spi.persistence;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anaptecs.jeaf.xfun.api.checks.Check;

/**
 * Class is the base class of all cache implementations that store all instances of a persistent object class and make
 * them accessible through their cache key. For the content of the cache a time to life (TTL) can be defined. The TTL
 * defines the period within which all objects will be read from the database again.
 * 
 * The cache must only be accessed from service methods that provide an transaction context.
 * 
 * Subclasses have to ensure that both cache key and the cached object are immutable if required.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.2
 */
public abstract class PersistentObjectCache<KEY, CACHED_OBJECT, PO extends PersistentObject> {
  /**
   * Constant for conversion from seconds to milliseconds.
   */
  private static final int MILLIS = 1000;

  /**
   * Attribute contains the time to life of the cached objects in seconds.
   */
  private final Long cacheTTL;

  /**
   * System timestamp indicating when the cached objects have to be reloaded again. If the attribute has the value null
   * then the data will never be refreshed.
   */
  private Long nextRefresh;

  /**
   * Map contains all cached objects.
   */
  private Map<KEY, CACHED_OBJECT> cachedObjects = new HashMap<KEY, CACHED_OBJECT>();

  /**
   * Initialize new cache instance
   * 
   * @param pCacheTTL Time to life of all objects inside the cache. After the TTL expired all objects of the cache will
   * be read again from the database. The parameter must not be null. The TTL is defined in seconds.
   */
  public PersistentObjectCache( Long pCacheTTL ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pCacheTTL, "pCacheTTL");

    cacheTTL = pCacheTTL;

    // In order to avoid problems when creating a component, all properties of the component will be loaded when they
    // are accessed the first time. Thus the attribute nextRefresh is set to now.
    nextRefresh = System.currentTimeMillis();
  }

  /**
   * Method returns the cache key of the passed business object.
   * 
   * @param pBusinessObject Business object whose cache key should be returned. The parameter is never null.
   * @return Key Object that should be used as cache key. The method must not return null. Its in the responsibility of
   * the subclass to ensure that cache keys are immutable.
   */
  protected abstract KEY getCacheKey( PO pBusinessObject );

  /**
   * Method returns the cached object that belongs to the passed business object.
   * 
   * @param pBusinessObject Business objects whose cacheable representation should be returned. The parameter is never
   * null.
   * @return CachedObject Cacheable object. The method must not return null. Its in the responsibility of the subclass
   * to ensure that the cached object is immutable if required.
   */
  protected abstract CACHED_OBJECT getCachedObject( PO pBusinessObject );

  /**
   * Method returns the cached object with the passed key.
   * 
   * @param pCacheKey Key of the cached object that should be returned. The parameter must not be null-
   * @return CachedObject Cached object with the passed key. The method returns null if no object with the passed key
   * exists.
   */
  public final synchronized CACHED_OBJECT getCachedObject( KEY pCacheKey ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pCacheKey, "pCacheKey");

    // Refresh content if required.
    this.refreshContentIfExpired();
    return cachedObjects.get(pCacheKey);
  }

  public final synchronized void refreshCache( ) {
    this.loadContent();
  }

  /**
   * Method returns the cache keys of all objects inside of the cache.
   * 
   * @return {@link List} List with the cache keys of all objects. The method never returns null.
   */
  public final synchronized List<KEY> getAllKeys( ) {
    // Refresh content if required.
    this.refreshContentIfExpired();

    // Return all cache keys.
    Set<KEY> lKeySet = cachedObjects.keySet();
    return new ArrayList<KEY>(lKeySet);
  }

  /**
   * Method returns all objects that are inside the cache.
   * 
   * @return {@link List} List with all cached objects. The method never returns null.
   */
  public final synchronized List<CACHED_OBJECT> getAllCachedObjects( ) {
    // Refresh content if required.
    this.refreshContentIfExpired();

    // Return all cached objects.
    return new ArrayList<CACHED_OBJECT>(cachedObjects.values());
  }

  /**
   * Method reloads all properties if the property values are expired according to the defined refresh interval.
   */
  private void refreshContentIfExpired( ) {
    // Determine current timestamp
    final long lNow = System.currentTimeMillis();

    // If a refresh timestamp is defined and it is in the past then we have to refresh.
    if (nextRefresh != null && lNow > nextRefresh) {
      this.loadContent();
    }
  }

  /**
   * Method loads all business objects of the cache type from the database.
   */
  private void loadContent( ) {
    // Get persistence provider and load all business objects.
    PersistenceServiceProvider lPersistenceServiceProvider = PersistentObject.getPersistenceServiceProvider();

    @SuppressWarnings("unchecked")
    Class<? extends PersistentObject> lClass =
        (Class<? extends PersistentObject>) ((ParameterizedType) this.getClass().getGenericSuperclass())
            .getActualTypeArguments()[2];
    List<? extends PersistentObject> lAllBusinessObjects = lPersistenceServiceProvider.findAll(lClass);

    // Cache all business objects.
    for (PersistentObject lNextBusinessObject : lAllBusinessObjects) {
      @SuppressWarnings("unchecked")
      PO lNextBO = (PO) lNextBusinessObject;
      KEY lCacheKey = this.getCacheKey(lNextBO);
      CACHED_OBJECT lCachedObject = this.getCachedObject(lNextBO);
      cachedObjects.put(lCacheKey, lCachedObject);
    }

    // Determine the next refresh interval for the property values.
    if (cacheTTL != null) {
      nextRefresh = System.currentTimeMillis() + (cacheTTL * MILLIS);
    }
    else {
      nextRefresh = null;
    }
  }
}
