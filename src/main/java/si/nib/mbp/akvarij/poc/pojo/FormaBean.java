/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.nib.mbp.akvarij.poc.pojo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.joda.time.DateTime;
import si.nib.mbp.akvarij.poc.dao.DaoDbConnection;

/**
 *
 * @author Peter
 */
public class FormaBean extends DaoDbConnection {

    static final Logger logger = Logger.getLogger(FormaBean.class.getName());

    Date datum = new Date();
    
    String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";

    String datumOd;
    String datumDo;
    String id;

    public FormaBean() {
        
        datumOd = new DateTime(new Date()).minusDays(3).toString("yyyy-MM-dd'T'HH:mm");
        datumDo = new DateTime(new Date()).toString("yyyy-MM-dd'T'HH:mm");
    }

    public String getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(String datumOd) {
        this.datumOd = datumOd;
    }

    public String getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(String datumDo) {
        this.datumDo = datumDo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Date getMaxDate(Connection con) throws Exception {
        Date date = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(dat_vno) from met_meritve");
            while (rs.next()) {
                date = new Date(rs.getTimestamp(1).getTime());
            }
            rs.close();
            st.close();
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (st != null) {
                st.close();
            }
        }

        return date;
    }

    private Connection getConnection() {
        if (!connect()) {
            logger.log(Level.WARNING, "Cannot get connection");
        }
        return connection;
    }

}
