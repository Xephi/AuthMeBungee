package fr.xephi.authme.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import fr.xephi.authme.Settings;
import fr.xephi.authme.database.MiniConnectionPoolManager.TimeoutException;

public class MySQL implements DataSource {

    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String tableName;
    private String columnName;
    private String columnLogged;
    private MiniConnectionPoolManager conPool;

    public MySQL() {
        this.host = Settings.getMySQLHost;
        this.port = Settings.getMySQLPort;
        this.username = Settings.getMySQLUsername;
        this.password = Settings.getMySQLPassword;
        this.database = Settings.getMySQLDatabase;
        this.tableName = Settings.getMySQLTablename;
        this.columnName = Settings.getMySQLColumnName;
        this.columnLogged = Settings.getMySQLColumnLogged;
        try {
            this.connect();
            this.setup();
        } catch (Exception e) {
        }
    }

    private synchronized void connect() throws ClassNotFoundException,
            SQLException, TimeoutException, NumberFormatException {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("[AuthMe] MySQL driver loaded");
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName(database);
        dataSource.setServerName(host);
        dataSource.setPort(Integer.parseInt(port));
        dataSource.setUser(username);
        dataSource.setPassword(password);
        conPool = new MiniConnectionPoolManager(dataSource, 10);
        System.out.println("[AuthMe] Connection pool ready");
    }

    private synchronized void setup() throws SQLException {
        Connection con = null;
        try {
            con = makeSureConnectionIsReady();
        } finally {
            close(con);
        }
    }

    @Override
    public synchronized boolean isAuthAvailable(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = makeSureConnectionIsReady();
            pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE LOWER(" + columnName + ")=LOWER(?);");
            pst.setString(1, user);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("[AuthMe] " + ex.getMessage());
            return false;
        } catch (TimeoutException ex) {
            System.out.println("[AuthMe] " + ex.getMessage());
            return false;
        } finally {
            close(rs);
            close(pst);
            close(con);
        }
    }

    @Override
    public synchronized void close() {
        try {
            conPool.dispose();
        } catch (SQLException ex) {
            System.out.println("[AuthMe] " + ex.getMessage());
        }
    }

    @Override
    public void reload() {
        try {
            reconnect(true);
        } catch (Exception e) {
            System.out.println("[AuthMe] " + e.getMessage());
        }
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                System.out.println("[AuthMe] " + ex.getMessage());
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                System.out.println("[AuthMe] " + ex.getMessage());
            }
        }
    }

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("[AuthMe] " + ex.getMessage());
            }
        }
    }

    private synchronized void reconnect(boolean reload)
            throws ClassNotFoundException, SQLException, TimeoutException {
        conPool.dispose();
        Class.forName("com.mysql.jdbc.Driver");
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName(database);
        dataSource.setServerName(host);
        dataSource.setPort(Integer.parseInt(port));
        dataSource.setUser(username);
        dataSource.setPassword(password);
        conPool = new MiniConnectionPoolManager(dataSource, 10);
        if (!reload)
            System.out.println("[AuthMe] ConnectionPool was unavailable... Reconnected!");
    }

    @Override
    public boolean isLogged(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = makeSureConnectionIsReady();
            pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE LOWER(" + columnName + ")=?;");
            pst.setString(1, user);
            rs = pst.executeQuery();
            if (rs.next())
                return (rs.getInt(columnLogged) == 1);
        } catch (SQLException ex) {
            System.out.println("[AuthMe] " + ex.getMessage());
            return false;
        } catch (TimeoutException ex) {
            System.out.println("[AuthMe] " + ex.getMessage());
            return false;
        } finally {
            close(rs);
            close(pst);
            close(con);
        }
        return false;
    }

    private synchronized Connection makeSureConnectionIsReady() {
        Connection con = null;
        try {
            con = conPool.getValidConnection();
        } catch (Exception te) {
            try {
                con = null;
                reconnect(false);
            } catch (Exception e) {
                System.out.println("[AuthMe] " + e.getMessage());
            }
        } catch (AssertionError ae) {
            if (!ae.getMessage().equalsIgnoreCase("AuthMeDatabaseError"))
                throw new AssertionError(ae.getMessage());
            try {
                con = null;
                reconnect(false);
            } catch (Exception e) {
                System.out.println("[AuthMe] " + e.getMessage());
            }
        }
        if (con == null)
            con = conPool.getValidConnection();
        return con;
    }
}
