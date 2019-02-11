package si.nib.mbp.akvarij.poc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

public final class DaoDbConnection {

    final String DATASOURCE_CONTEXT = "java:comp/env/jdbc/AKVARIJ";

    static final Logger logger = Logger.getLogger(DaoDbConnection.class.getName());

    protected Connection connection = null;

    public DaoDbConnection() {
    }

    public Connection geConnection() {
        Connection result = null;
        try {
            Context initialContext = new InitialContext();
            DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
            if (datasource != null) {
                result = datasource.getConnection();
                result.setAutoCommit(false);
            } else {
                logger.log(Level.SEVERE, "Failed to lookup datasource.");
            }
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "Cannot get connection: ", ex);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Cannot get connection: {0}", ex);
        }
        return result;
    }
}
