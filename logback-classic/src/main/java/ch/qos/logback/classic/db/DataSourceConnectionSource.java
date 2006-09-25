/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.classic.db;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 *  The DataSourceConnectionSource is an implementation of {@link ConnectionSource}
 *  that obtains the Connection in the recommended JDBC manner based on
 *  a {@link javax.sql.DataSource DataSource}.
 *  <p>
 *
 *  @author Ray DeCampo
 *  @author Ceki G&uuml;lc&uuml;
 */
public class DataSourceConnectionSource extends ConnectionSourceBase {

  private DataSource dataSource;

  @Override
  public void start() {
    //LogLog.debug("**********DataSourceConnectionSource.activateOptions called");
    if (dataSource == null) {
      addWarn("WARNING: No data source specified");
    } else {
      Connection connection = null;
      try {
        connection = getConnection();
      } catch(SQLException se) {
        addWarn("Could not get a connection to discover the dialect to use.", se);
      }
      if(connection != null) {
        discoverConnnectionProperties();
      } 
      if(!supportsGetGeneratedKeys() && getSQLDialectCode() == ConnectionSource.UNKNOWN_DIALECT) {
        addWarn("Connection does not support GetGeneratedKey method and could not discover the dialect.");
      }
    }
    super.start();
  }

  /**
   * @see org.apache.log4j.db.ConnectionSource#getConnection()
   */
  public Connection getConnection() throws SQLException {
    if (dataSource == null) {
      addError("WARNING: No data source specified");
      return null;
    }

    if (getUser() == null) {
      return dataSource.getConnection();
    } else {
      return dataSource.getConnection(getUser(), getPassword());
    }
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
}