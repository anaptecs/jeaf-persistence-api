/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.spi.persistence.util;

/*
 * TLS: Modified class to use JEAF's tracing.
 */

/*
 * Added additional null checks when closing the ResultSet and Statements.
 * 
 * Thanks to pihug12 and Grzegorz Oledzki at stackoverflow.com
 * http://stackoverflow.com/questions/5332149/jdbc-scriptrunner-java-lang-nullpointerexception?tab=active#tab-top
 */
/*
 * Modified: Use logWriter in print(Object), JavaDoc comments, correct Typo.
 */
/*
 * Modified by Pantelis Sopasakis <chvng@mail.ntua.gr> to take care of DELIMITER statements. This way you can execute
 * scripts that contain some TRIGGER creation code. New version using REGEXPs! Latest modification: Cater for a
 * NullPointerException while parsing. Date: Feb 16, 2011, 11:48 EET
 */
/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class from the iBATIS Apache project. Only
 * removed dependency on Resource class and a constructor
 */
/*
 * Copyright 2004 Clinton Begin Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Tool to run database scripts. This version of the script can be found at https://gist.github.com/gists/831762/
 */
public class ScriptRunner {

  private static final String DEFAULT_DELIMITER = ";";

  private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";

  private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

  private final Connection connection;

  private final boolean stopOnError;

  private final boolean autoCommit;

  private String delimiter = DEFAULT_DELIMITER;

  private boolean fullLineDelimiter = false;

  /**
   * Default constructor.
   * 
   * @param connection
   * @param autoCommit
   * @param stopOnError
   */
  public ScriptRunner( Connection connection, boolean autoCommit, boolean stopOnError ) {
    this.connection = connection;
    this.autoCommit = autoCommit;
    this.stopOnError = stopOnError;
  }

  /**
   * @param delimiter
   * @param fullLineDelimiter
   */
  public void setDelimiter( String delimiter, boolean fullLineDelimiter ) {
    this.delimiter = delimiter;
    this.fullLineDelimiter = fullLineDelimiter;
  }

  /**
   * Runs an SQL script (read in using the Reader parameter).
   * 
   * @param reader - the source of the script
   * @throws SQLException if any SQL errors occur
   * @throws IOException if there is an error reading from the Reader
   */
  public void runScript( Reader reader ) throws IOException, SQLException {
    try {
      boolean originalAutoCommit = connection.getAutoCommit();
      try {
        if (originalAutoCommit != autoCommit) {
          connection.setAutoCommit(autoCommit);
        }
        this.runScript(connection, reader);
      }
      finally {
        connection.setAutoCommit(originalAutoCommit);
      }
    }
    catch (IOException e) {
      throw e;
    }
    catch (SQLException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RuntimeException("Error running script.  Cause: " + e, e);
    }
  }

  /**
   * Runs an SQL script (read in using the Reader parameter) using the connection passed in.
   * 
   * @param pConnection - the connection to use for the script
   * @param pReader - the source of the script
   * @throws SQLException if any SQL errors occur
   * @throws IOException if there is an error reading from the Reader
   */
  private void runScript( Connection pConnection, Reader pReader ) throws IOException, SQLException {
    StringBuffer lCommand = null;
    Statement lStatement = null;
    try {
      LineNumberReader lLineReader = new LineNumberReader(pReader);
      String lLine = null;
      while ((lLine = lLineReader.readLine()) != null) {
        if (lCommand == null) {
          lCommand = new StringBuffer();
        }
        String lTrimmedLine = lLine.trim();
        if (lTrimmedLine.startsWith("--")) {
          this.println(lTrimmedLine);
        }
        else if (lTrimmedLine.length() < 1 || lTrimmedLine.startsWith("//")) {
          // Do nothing
        }
        else if (lTrimmedLine.length() < 1 || lTrimmedLine.startsWith("--")) {
          // Do nothing
        }
        else if (!fullLineDelimiter && lTrimmedLine.endsWith(this.getDelimiter())
            || fullLineDelimiter && lTrimmedLine.equals(this.getDelimiter())) {

          Pattern lPattern = Pattern.compile(DELIMITER_LINE_REGEX);
          Matcher lMatcher = lPattern.matcher(lTrimmedLine);
          if (lMatcher.matches()) {
            setDelimiter(lTrimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(), fullLineDelimiter);
            lLine = lLineReader.readLine();
            if (lLine == null) {
              break;
            }
            lTrimmedLine = lLine.trim();
          }

          lCommand.append(lLine.substring(0, lLine.lastIndexOf(this.getDelimiter())));
          lCommand.append(" ");
          lStatement = pConnection.createStatement();

          this.println(lCommand);

          boolean lHasResults = false;
          if (stopOnError) {
            lHasResults = lStatement.execute(lCommand.toString());
          }
          else {
            try {
              lStatement.execute(lCommand.toString());
            }
            catch (SQLException e) {
              e.fillInStackTrace();
              this.printlnError("Error executing: " + lCommand);
              this.printlnError(e);
            }
          }

          if (autoCommit && !pConnection.getAutoCommit()) {
            pConnection.commit();
          }

          ResultSet lResultSet = lStatement.getResultSet();
          if (lHasResults && lResultSet != null) {
            ResultSetMetaData md = lResultSet.getMetaData();
            int cols = md.getColumnCount();
            StringBuffer lBuffer = new StringBuffer();
            for (int i = 0; i < cols; i++) {
              String name = md.getColumnLabel(i);
              lBuffer.append(name);
              lBuffer.append(" ");
            }
            this.println(lBuffer.toString());
            lBuffer = new StringBuffer();
            while (lResultSet.next()) {
              for (int i = 1; i <= cols; i++) {
                String value = lResultSet.getString(i);
                lBuffer.append(value);
                lBuffer.append(" ");
              }
              this.println(lBuffer.toString());
            }
          }

          lCommand = null;
          try {
            if (lResultSet != null) {
              lResultSet.close();
            }
          }
          catch (Exception e) {
            XFun.getTrace().error(e.getMessage(), e);
          }
          try {
            lStatement.close();
          }
          catch (Exception e) {
            XFun.getTrace().error(e.getMessage(), e);
          }
        }
        else {
          Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
          Matcher matcher = pattern.matcher(lTrimmedLine);
          if (matcher.matches()) {
            setDelimiter(lTrimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(), fullLineDelimiter);
            lLine = lLineReader.readLine();
            if (lLine == null) {
              break;
            }
            lTrimmedLine = lLine.trim();
          }
          lCommand.append(lLine);
          lCommand.append(" ");
        }
      }
      if (!autoCommit) {
        pConnection.commit();
      }
    }
    catch (SQLException e) {
      e.fillInStackTrace();
      this.printlnError("Error executing: " + lCommand);
      this.printlnError(e);
      throw e;
    }
    catch (IOException e) {
      e.fillInStackTrace();
      this.printlnError("Error executing: " + lCommand);
      this.printlnError(e);
      throw e;
    }
    finally {
      if (lStatement != null) {
        try {
          lStatement.close();
        }
        catch (SQLException e) {
          this.printlnError(e);
        }
      }
      pConnection.rollback();
    }
  }

  private String getDelimiter( ) {
    return delimiter;
  }

  private void println( Object o ) {
    XFun.getTrace().info(o.toString());
  }

  private void printlnError( Object o ) {
    XFun.getTrace().error(o.toString());
  }
}
