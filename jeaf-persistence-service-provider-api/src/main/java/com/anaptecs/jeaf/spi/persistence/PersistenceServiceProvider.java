/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.spi.persistence;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.anaptecs.jeaf.core.api.ServiceProvider;
import com.anaptecs.jeaf.xfun.api.common.AbstractObjectID;
import com.anaptecs.jeaf.xfun.api.common.Identifiable;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;

/**
 * This interface defines all methods that have to provided by a persistence provider implementation to integrate a
 * persistence framework into JEAF. In order to have a proper interaction with persistence frameworks the caller has to
 * take care about the following conditions:
 * <ul>
 * <li>All methods of this class have to be called from the context of a running transaction. This context will be used
 * for all database accesses.</li>
 * <li>Created persistent object are members of the current transaction will be inserted to the database when the
 * running transaction will be committed.</li>
 * <li>Any changes made on any persistent object will also be part of the current transaction.</li>
 * </ul>
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public interface PersistenceServiceProvider extends ServiceProvider {
  /**
   * Name of the base path under which all X-Fun configuration files are located.
   */
  static final String PERSISTENCE_BASE_PATH = "META-INF/JEAF/Persistence";

  /**
   * Method creates a new persistent instance of the passed class. Before the object will be returned to the caller the
   * implementing class has to assign an object id to the created persistent object. All objects created by this method
   * have to be written to the database when the current transaction will be committed.
   * 
   * @param pPersistentObjectType Type of the persistent object that should be created. The parameter must not be null.
   * @param <T> Type
   * @return PersistentObject Created persistent object. The method must not return null and an object id has to be
   * assigned to the object.
   */
  <T extends PersistentObject> T createPersistentObject( Class<T> pPersistentObjectType );

  /**
   * Method reads the persistent object with the passed object id from the database. If the object has already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pObjectID ID of the object that should be read from the database. The parameter must not be null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @return {@link PersistentObject} Persistent object with the passed id and type. The method may return null in the
   * case that there exists not persistent object with passed id.
   */
  <T extends PersistentObject> T getPersistentObject( AbstractObjectID<?> pObjectID, Class<T> pPersistentObjectType );

  /**
   * Method reads the persistent object with the passed object id from the database. If the object has already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pObjectID ID of the object that should be read from the database. The parameter must not be null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that the result
   * set is empty. If the parameter is true then empty results are valid and no exception will be thrown.
   * @return {@link PersistentObject} Persistent object with the passed id and type. The method may return null in the
   * case that there exists not persistent object with passed id and parameter pAllowEmptyResult is true.
   */
  <T extends PersistentObject> T getPersistentObject( AbstractObjectID<?> pObjectID, Class<T> pPersistentObjectType,
      boolean pAllowEmptyResult );

  /**
   * Method reads the persistent object with the id of the passed object from the database. If the object has already
   * been loaded within the current transaction the already loaded object has to be returned. The object must not be
   * reloaded from the database in this case.
   * 
   * @param pIdentifiableObject Object that should be read from the database. The parameter must not be null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @return {@link PersistentObject} Persistent object with the passed id and type. The method may return null in the
   * case that there exists not persistent object with passed id.
   */
  <T extends PersistentObject> T getPersistentObject( Identifiable<? extends AbstractObjectID<?>> pIdentifiableObject,
      Class<T> pPersistentObjectType );

  /**
   * Method reads the persistent object with the id of the passed object from the database. If the object has already
   * been loaded within the current transaction the already loaded object has to be returned. The object must not be
   * reloaded from the database in this case.
   * 
   * @param pIdentifiableObject Service object that should be read from the database. The parameter must not be null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that the result
   * set is empty. If the parameter is true then empty results are valid and no exception will be thrown.
   * @return {@link PersistentObject} Persistent object with the passed id and type. The method may return null in the
   * case that there exists no persistent object with passed id and parameter pAllowEmptyResult is true.
   */
  <T extends PersistentObject> T getPersistentObject( Identifiable<? extends AbstractObjectID<?>> pIdentifiableObject,
      Class<T> pPersistentObjectType, boolean pAllowEmptyResult );

  /**
   * Method reads all persistent objects with the passed objects from the database. If the objects have already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pIdentifiableObjects List with all objects that should be read from the database. The parameter must not be
   * null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @return {@link List} List with all persistent objects with the passed id and type. The method returns an empty list
   * if none of the objects was found.
   */
  <T extends PersistentObject> List<T> getPersistentObjects(
      Collection<? extends Identifiable<? extends AbstractObjectID<?>>> pIdentifiableObjects,
      Class<T> pPersistentObjectType );

  /**
   * Method reads all persistent objects with the passed objects from the database. If the objects have already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pIdentifiableObjects List with all objects that should be read from the database. The parameter must not be
   * null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that that at
   * least one persistent object could not be found by its id. If the parameter is true then empty results are valid and
   * no exception will be thrown.
   * @return {@link List} List with all persistent objects with the passed id and type. The method returns an empty list
   * if none of the objects was found.
   */
  <T extends PersistentObject> List<T> getPersistentObjects(
      Collection<? extends Identifiable<? extends AbstractObjectID<?>>> pIdentifiableObjects,
      Class<T> pPersistentObjectType, boolean pAllowEmptyResult );

  /**
   * Method reads all persistent objects with the passed objects from the database. If the objects have already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pIdentifiableObjects List with all objects that should be read from the database. The parameter must not be
   * null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that that at
   * least one persistent object could not be found by its id. If the parameter is true then empty results are valid and
   * no exception will be thrown.
   * @param pUseUnversionedObjectID Parameter defines if the method should check if the version label of the persistent
   * object is still the same. This means that the method will ensure that the object was not updated meanwhile.
   * @return {@link List} List with all persistent objects with the passed id and type. The method returns an empty list
   * if none of the objects was found.
   */
  <T extends PersistentObject> List<T> getPersistentObjects(
      Collection<? extends Identifiable<? extends AbstractObjectID<?>>> pIdentifiableObjects,
      Class<T> pPersistentObjectType, boolean pAllowEmptyResult, boolean pUseUnversionedObjectID );

  /**
   * Method reads all business objects with the passed object ids from the database. If the objects have already been
   * loaded within the current transaction the already loaded object has to be returned. The object must not be reloaded
   * from the database in this case.
   * 
   * @param pObjectIDs List with all IDs of the objects that should be read from the database. The parameter must not be
   * null.
   * @param pPersistentObjectType Type of business object, to which the passed id belongs. The parameter must not be
   * null.
   * @param <T> Type
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that that at
   * least one business object could not be found by its id. If the parameter is true then empty results are valid and
   * no exception will be thrown.
   * @param pUseUnversionedObjectID Parameter defines if the method should check if the version label of the persistent
   * object is still the same. This means that the method will ensure that the object was not updated meanwhile.
   * @return {@link List} List with all business objects with the passed id and type. The method returns an empty list
   * if none of the objects was found.
   */
  public <T extends PersistentObject> List<T> getPersistentObjectsByIDs(
      Collection<? extends AbstractObjectID<?>> pObjectIDs, Class<T> pPersistentObjectType, boolean pAllowEmptyResult,
      boolean pUseUnversionedObjectID );

  /**
   * Method executes the passed query. This is just a convenience method for the fact that JPA API has no method for a
   * direct query which supports generics. This method may return empty results.
   * 
   * @param pQuery Query that should be executed. The parameter must not be null.
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @return T Result of the query. The method never returns null. In the case of an empty result set the method returns
   * an empty list.
   */
  <T extends PersistentObject> List<T> executeQuery( Query pQuery, Class<T> pResultType );

  /**
   * Method executes the passed query. This is just a convenience method for the fact that JPA API has no method for a
   * direct query which supports generics.
   * 
   * @param pQuery Query that should be executed. The parameter must not be null.
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that the result
   * set is empty. If the parameter is true then empty results are valid and no exception will be thrown.
   * @return T Result of the query. The method never returns null. In the case of an empty result set the method returns
   * an empty list.
   */
  <T extends PersistentObject> List<T> executeQuery( Query pQuery, Class<T> pResultType, boolean pAllowEmptyResult );

  /**
   * Method executes the passed query for a single result query. This is just a convenience method for the fact that JPA
   * <code>getSingleResult()</code> throws an exception of the result set of the query is empty.
   * 
   * @param <T> Type
   * @param pQuery Query that should be executed. The parameter must not be null.
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that the result
   * set is empty. If the parameter is true then empty results are valid and no exception will be thrown.
   * @return T Result of the query. The method may return null in the case that empty result sets are valid and no
   * object was found.
   */
  <T extends PersistentObject> T executeSingleResultQuery( Query pQuery, Class<T> pResultType,
      boolean pAllowEmptyResult );

  /**
   * Method executes the passed update query. This is just a convenience method for the fact that JPA
   * <code>executeUpdate()</code>.
   * 
   * @param pQuery Query that should be executed. The parameter must not be null.
   * @return int Number of objects that were updated.
   */
  int executeUpdateQuery( Query pQuery );

  /**
   * Method executes the passed SQL statement for a single result query. This is just a convenience method for the fact
   * that JPA API has no method for a direct query.
   * 
   * @param <T> Type
   * @param pSQLStatement SQL Statement that should be executed. The parameter must not be null.
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @param pAllowEmptyResult Parameter defines whether the method should throw an exception in the case that the result
   * set is empty. If the parameter is true then empty results are valid and no exception will be thrown.
   * @return T Result of the query. The method may return null in the case that empty result sets are valid and no
   * object was found.
   */
  <T extends PersistentObject> T executeNativeSingleResultQuery( String pSQLStatement, Class<T> pResultType,
      boolean pAllowEmptyResult );

  /**
   * Method executes the passed SQL query. This is just a convenience method for the fact that JPA API has no method for
   * a direct query.
   * 
   * @param <T> Type
   * @param pSQLStatement SQL Statement that should be executed. The parameter must not be null.
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @return T Result of the query. The method never returns null. In the case of an empty result set the method returns
   * an empty list.
   */
  <T extends PersistentObject> List<T> executeNativeQuery( String pSQLStatement, Class<T> pResultType );

  /**
   * Method returns all persistent objects of the passed type that can be found in the database.
   * 
   * @param <T> Type
   * @param pResultType Class object describing the expected result type of the query. The parameter must not be null.
   * @return T Result of the query. The method never returns null. In the case of an empty result set the method returns
   * an empty list.
   */
  <T extends PersistentObject> List<T> findAll( Class<T> pResultType );

  /**
   * Method deletes the passed persistent object within the current transaction from the database.
   * 
   * @param pPersistentObject Persistent Object that should be deleted within the currently running transaction. The
   * parameter must not be null.
   */
  void delete( PersistentObject pPersistentObject );

  /**
   * Method removes the passed persistent object within the current transaction from the database.
   * 
   * @param pObjectID ID, as service object id, of the object that be removed from the database. The parameter must not
   * be null.
   * @param pPersistentObjectType Type of persistent object, to which the passed id belongs. The parameter must not be
   * null.
   */
  void remove( AbstractObjectID<?> pObjectID, Class<? extends PersistentObject> pPersistentObjectType );

  /**
   * Method uses the passed native SQL statement to create a JPA Query object that can be used to execute a query on the
   * database. Therefore the current transaction context will be used.
   * 
   * @param pNativeSQLStatement Native SQL statement that should be send to the database. The parameter must not be
   * null. The passed select statement has to contain the names of all rows that should be read from the database. For
   * any rows of an table where there is now projection the attribute on the returned persistent object will not be set.
   * @return {@link Query} Query object that can be used to execute the passed query. The method never returns null.
   */
  public Query createNativeQuery( String pNativeSQLStatement );

  /**
   * Method uses the passed native SQL statement to create a JPA Query object that can be used to execute a query on the
   * database. Therefore the current transaction context will be used.
   * 
   * @param pNativeSQLStatement Native SQL statement that should be send to the database. The parameter must not be
   * null. The passed select statement has to contain the names of all rows that should be read from the database. For
   * any rows of an table where there is now projection the attribute on the returned persistent object will not be set.
   * @param pResultClass Class Class object of the expected resulting objects. The parameter must not be null.
   * @return {@link Query} Query object that can be used to execute the passed query. The method never returns null.
   */
  Query createNativeQuery( String pNativeSQLStatement, Class<? extends PersistentObject> pResultClass );

  /**
   * Method uses the passed JPA-QL statement to create a JPA Query object that can be used to execute a query on the
   * database. Therefore the current transaction context will be used.
   * 
   * @param pJPAQLStatement JPA-QL statement that should be send to the database. The parameter must not be null and the
   * passed select statement has to be a valid JPA-QL statement.
   * @return {@link Query} Query object that can be used to execute the passed query. The method never returns null.
   */
  Query createJPAQLQuery( String pJPAQLStatement );

  /**
   * Method flushes the current persistence context to the database. This means that all newly created persistent
   * objects as well as updates and deletes will be sent to the database.
   */
  void flush( );

  /**
   * This method is highly dependent the used persistence framework. Some persistence frameworks use so called proxies
   * that are created at runtime to support lazy loading n:1 associations as there are no collection interfaces that can
   * hide a lazy loading supporting implementation. However there may occur situation where this behavior leads to
   * problems as operations such as "instanceof" won't work.
   * 
   * Further information about the problem can be found here:
   * http://pwu-developer.blogspot.com/2010/11/hibernate-lazy-proxied-objects-and.html
   * 
   * JEAF's solves this problem as for all get method of non multi valued associations a potential proxy will be
   * unwrapped by default. Thus this method should only be called directly in rare circumstances.
   * 
   * @param <T> Type
   * @param pProxyBO Potential proxy of a persistent object that should be unwrapped. The parameter may be null.
   * @return T Unwrapped persistent object if required for the used persistence framework. If null was passed to the
   * method null will also be returned.
   */
  <T extends PersistentObject> T unproxy( T pProxyBO );

  /**
   * Method can be called in order to execute JDBC / connection specific calls that require the connection of the
   * current transaction.
   * 
   * @param pJDBCWorker Worker object that contains the work that should be executed. The parameter must not be null.
   * @throws SystemException method throws a system exception if the JDBC worker causes a SQLExceptrion during his work.
   */
  void executeJDBCWorker( JDBCWorker pJDBCWorker );

  /**
   * Method creates a new entity manager for the persistence unit with the passed name. It's in the responsibility of
   * the caller to close the entity manager again. Same applies to transaction management.
   * 
   * @param pPersistenceUnitName Name of the persistence unit for which an new entity manager should be created.
   * @return {@link EntityManager} Created entity manager. The method never returns null.
   */
  EntityManager createEntityManager( String pPersistenceUnitName );

  // TODO Add method to refresh and lock an persistent object.

}
