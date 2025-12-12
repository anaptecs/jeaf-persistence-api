package com.anaptecs.jeaf.spi.persistence;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;

/**
 * Class contains all generated constants for messages of JEAF's Persistence Service Provider.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
@MessageResource(path = "PersistenceServiceProviderMessages.xml")
public final class PersistenceServiceProviderMessages {
  /**
   * Constant for XML file that contains all messages that are defined within this class.
   */
  private static final String MESSAGE_RESOURCE = "PersistenceServiceProviderMessages.xml";

  /**
   * Exceptions with this error code are thrown if the available transaction context is incompatible with the one
   * required by the implementation.
   */
  public static final ErrorCode UNSUPPORTED_TX_CONTEXT_IMPL;

  /**
   * Messages with this message id are traced when the hibernate persistence service provider tries to obtain a lock for
   * getting the next session id.
   */
  public static final MessageID OBTAINING_LOCK_FOR_SESSION_ID;

  /**
   * Messages with this message id are traced when the hibernate persistence service provider created a new object id
   * generator.
   */
  public static final MessageID CREATED_OBJECT_ID_GENERATOR;

  /**
   * Exceptions with this error code are thrown if the lookup for an entity manager fails.
   */
  public static final ErrorCode LOOKUP_FOR_ENTITY_MANAGER_FAILED;

  /**
   * Exceptions with this error code are thrown if the persistence service provider implementation is used within an
   * unsupported environment.
   */
  public static final ErrorCode UNSUPPORTED_RUNTIME;

  /**
   * Exceptions with this error code are thrown if a query for a single result returned an empty result set that but one
   * object is required.
   */
  public static final ErrorCode EMPTY_RESULT_SET_NOT_ALLOWED;

  /**
   * Exceptions with this error code are thrown if a query for a single result returned more than one object.
   */
  public static final ErrorCode SINGLE_RESULT_QUERY_RETURNED_MANY_OBJECTS;

  /**
   * Messages with message id will be traced for every persistence unit whose configuration is read from its property
   * file.
   */
  public static final MessageID USING_JPA_CONFIGURATION_FROM_PROPERTIES;

  /**
   * Messages with message id will be traced for every persistence unit whose configuration is read from
   * persistence.xml.
   */
  public static final MessageID USING_JPA_CONFIGURATION_FROM_PERSISTENCE_XML;

  /**
   * Exceptions with this error code are thrown if an object that was queried by its OID could not be found.
   */
  public static final ErrorCode OBJECT_DOES_NOT_EXIST;

  /**
   * Messages with this id will be traced when JEAF tries to lookup an an entity manager.
   */
  public static final MessageID LOOKING_UP_ENTITY_MANAGER;

  /**
   * Exceptions with this error code are thrown if the class id value is already in use.
   */
  public static final ErrorCode CLASS_ID_VALUE_ALREADY_IN_USE;

  /**
   * Exceptions with this error code are thrown if the class is already assigned to a class id.
   */
  public static final ErrorCode CLASS_ALREADY_IN_USE;

  /**
   * Exceptions with this error code are thrown if an object optimistic lock conflict is detected when loading an object
   * from the database.
   */
  public static final ErrorCode OPTIMISTIC_LOCK_CONFLICT;

  /**
   * Exceptions with this error code are thrown if no entity manager JNDI name is defined in the UML model.
   */
  public static final ErrorCode ENTITY_MANAGER_JNDI_NOT_DEFINED;

  /**
   * Messages with the messages id are written when an explicit query returns.
   */
  public static final MessageID QUERY_COMPLETED;

  /**
   * Messages with the messages id are written when the current persistence contexted is flushed.
   */
  public static final MessageID FLUSHED_CONTEXT;

  /**
   * Messages with the messages id are written when an update query is executed.
   */
  public static final MessageID EXECUTED_UPDATE_QUERY;

  /**
   * Exceptions with the error code are thrown if a persistence unit configuration contains an invalid reference for
   * container manager connections.
   */
  public static final ErrorCode INVALID_REF_IN_PERSISTENCE_CONFIG;

  /**
   * Exceptions with the error code are thrown if JEAF Persistence configuration file points to a class that does not
   * have annotation @PersistenceConfig.
   */
  public static final ErrorCode CLASS_DOES_NOT_DECLARE_PERSISTENCE_CONFIG;

  /**
   * Exceptions with the error code are thrown if a persistence unit configuration contains an invalid reference for
   * container manager connections.
   */
  public static final ErrorCode INVALID_REF_IN_PERSISTENCE_UNIT;

  /**
   * Exceptions with the error code are thrown if a persistence unit configuration defines a JTA and a non-JTA
   * datasource.
   */
  public static final ErrorCode JTA_AND_NON_JTA_DATASOURCE_DEFINED;

  /**
   * Exceptions with the error code are thrown if a persistence unit configuration neither defines a JTA nor a non-JTA
   * datasource.
   */
  public static final ErrorCode NO_DATASOURCE_DEFINED;

  /**
   * Exceptions with the error code are thrown if a persistence configuration neither defines container nor application
   * managed connections.
   */
  public static final ErrorCode NO_CONNECTIONS_DEFINED;

  /**
   * Exceptions with the error code are thrown if a persistence unit configuration does not define persistent classes at
   * all.
   */
  public static final ErrorCode NO_PERSISTENT_CLASSES;

  /**
   * Exceptions with the error code are thrown if a application managed connections configuration does not have real
   * string as JDBC connection URL.
   */
  public static final ErrorCode JDBC_CONNECTION_URL_MISSING;

  /**
   * Exceptions with the error code are thrown if a application managed connections configuration does not have real
   * string as JDBC connection URL.
   */
  public static final ErrorCode DATASOURCE_LOOKUP_FAILED;

  /**
   * Exceptions with the error code are thrown if a a new entity manager could not be created.
   */
  public static final ErrorCode PERSISTENCE_UNIT_NOT_DEFINED;
  /**
   * Static initializer contains initialization for all generated constants.
   */
  static {
    MessageRepository lRepository = XFun.getMessageRepository();
    lRepository.loadResource(MESSAGE_RESOURCE);
    // Handle all info messages.
    OBTAINING_LOCK_FOR_SESSION_ID = lRepository.getMessageID(6001);
    CREATED_OBJECT_ID_GENERATOR = lRepository.getMessageID(6002);
    USING_JPA_CONFIGURATION_FROM_PROPERTIES = lRepository.getMessageID(6007);
    USING_JPA_CONFIGURATION_FROM_PERSISTENCE_XML = lRepository.getMessageID(6008);
    LOOKING_UP_ENTITY_MANAGER = lRepository.getMessageID(6010);
    QUERY_COMPLETED = lRepository.getMessageID(6015);
    FLUSHED_CONTEXT = lRepository.getMessageID(6016);
    EXECUTED_UPDATE_QUERY = lRepository.getMessageID(6017);
    // Handle all messages for errors.
    UNSUPPORTED_TX_CONTEXT_IMPL = lRepository.getErrorCode(6000);
    LOOKUP_FOR_ENTITY_MANAGER_FAILED = lRepository.getErrorCode(6003);
    UNSUPPORTED_RUNTIME = lRepository.getErrorCode(6004);
    EMPTY_RESULT_SET_NOT_ALLOWED = lRepository.getErrorCode(6005);
    SINGLE_RESULT_QUERY_RETURNED_MANY_OBJECTS = lRepository.getErrorCode(6006);
    OBJECT_DOES_NOT_EXIST = lRepository.getErrorCode(6009);
    CLASS_ID_VALUE_ALREADY_IN_USE = lRepository.getErrorCode(6011);
    CLASS_ALREADY_IN_USE = lRepository.getErrorCode(6012);
    OPTIMISTIC_LOCK_CONFLICT = lRepository.getErrorCode(6013);
    ENTITY_MANAGER_JNDI_NOT_DEFINED = lRepository.getErrorCode(6014);
    INVALID_REF_IN_PERSISTENCE_CONFIG = lRepository.getErrorCode(6018);
    CLASS_DOES_NOT_DECLARE_PERSISTENCE_CONFIG = lRepository.getErrorCode(6019);
    INVALID_REF_IN_PERSISTENCE_UNIT = lRepository.getErrorCode(6020);
    JTA_AND_NON_JTA_DATASOURCE_DEFINED = lRepository.getErrorCode(6021);
    NO_DATASOURCE_DEFINED = lRepository.getErrorCode(6022);
    NO_CONNECTIONS_DEFINED = lRepository.getErrorCode(6023);
    NO_PERSISTENT_CLASSES = lRepository.getErrorCode(6024);
    JDBC_CONNECTION_URL_MISSING = lRepository.getErrorCode(6025);
    DATASOURCE_LOOKUP_FAILED = lRepository.getErrorCode(6026);
    PERSISTENCE_UNIT_NOT_DEFINED = lRepository.getErrorCode(6027);
    // Handle all localized strings.
  }

  /**
   * Constructor is private to ensure that no instances of this class will be created.
   */
  private PersistenceServiceProviderMessages( ) {
    // Nothing to do.
  }
}