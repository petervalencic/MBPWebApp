package si.nib.mbp.akvarij.poc.dao;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoDbConnection {

    final String JAVA_DB_CONTEXT = "java:comp/env";
    final String JNDI_NAME = "jdbc/AKVARIJ";

    static final Logger logger = Logger.getLogger(DaoDbConnection.class.getName());

    protected Connection connection = null;

    protected DaoDbConnection() {
    }

    protected boolean connect() {
        try {
            /*
            InitialContext initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup(JAVA_DB_CONTEXT);
            DataSource datasource = (DataSource) envContext.lookup(JNDI_NAME);
             */
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup(JNDI_NAME);
            connection = ds.getConnection();
           
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed establishing DB connection", e);
        }

        return true;
    }
}
