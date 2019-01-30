package si.nib.mbp.akvarij.poc.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.jdbc.StringUtils;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.sql.*;

import si.nib.mbp.akvarij.poc.dao.DaoDbConnection;
import si.nib.mbp.akvarij.poc.pojo.PodatkiVO;
import si.nib.mbp.akvarij.poc.pojo.PodatkiJson;

@Path("/podatki")
public class PodatkiService extends DaoDbConnection {
    static final Logger logger = Logger.getLogger(PodatkiService.class.getName());

    @GET
    @Path("/tempsla")
    @Produces({MediaType.APPLICATION_JSON})

    public ArrayList<PodatkiJson> getPodatki(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);
        logger.log(Level.INFO, "Podatki service DatumDo: {0}", datumDo);
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30";

        ArrayList<PodatkiJson> podatki = new ArrayList<PodatkiJson>();

        try {
            con = this.getConnection();
            st = con.createStatement();
            if (StringUtils.isNullOrEmpty(datumOd) & StringUtils.isNullOrEmpty(datumDo)) {

                sql = "select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30";
                pstmt = con.prepareCall(sql);
            } else {
                sql = "SELECT * FROM met_meritve WHERE  dat_vno BETWEEN ? AND ?' order by dat_vno asc";
                pstmt = con.prepareCall(sql);
                pstmt.setString(1, datumOd);
                pstmt.setString(2, datumDo);

            }

            rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                PodatkiJson pod = new PodatkiJson();
                pod.setDatum(new Date(rs.getTimestamp("dat_vno").getTime()));
                pod.setTemperatura(rs.getDouble("temp"));
                pod.setSlanost(rs.getDouble("sal"));
                podatki.add(pod);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return podatki;

    }

    @GET
    @Path("/tempslajson")
    @Produces(MediaType.APPLICATION_JSON)

    public String getPodatkiJson(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);
        logger.log(Level.INFO, "Podatki service DatumDo: {0}", datumDo);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql;

        ArrayList<PodatkiJson> podatki = new ArrayList<PodatkiJson>();

        try {
            con = this.getConnection();
            st = con.createStatement();
            if (StringUtils.isNullOrEmpty(datumOd) & StringUtils.isNullOrEmpty(datumDo)) {
                sql = "select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30";
            } else {
                sql = "SELECT * FROM met_meritve WHERE  dat_vno BETWEEN '" + datumOd + "' AND '" + datumDo + "' order by dat_vno asc";

            }
            pstmt = con.prepareCall(sql);

            rs = pstmt.executeQuery(sql);

            while (rs.next()) {
                PodatkiJson pod = new PodatkiJson();
                pod.setDatum(new Date(rs.getTimestamp("dat_vno").getTime()));
                pod.setTemperatura(rs.getDouble("temp"));
                pod.setSlanost(rs.getDouble("sal"));
                podatki.add(pod);
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(podatki);

    }

    @GET
    @Path("/tempslaxml")
    @Produces(MediaType.APPLICATION_XML)

    public ArrayList<PodatkiVO> getPodatkiXML(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);
        logger.log(Level.INFO, "Podatki service DatumDo: {0}", datumDo);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        ArrayList<PodatkiVO> podatki = new ArrayList<PodatkiVO>();

        if (StringUtils.isNullOrEmpty(datumOd) & StringUtils.isNullOrEmpty(datumDo)) {
            System.out.println("oba sta null");
            try {
                con = this.getConnection();
                st = con.createStatement();
                rs = st.executeQuery("select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30");
                while (rs.next()) {
                    PodatkiVO pod = new PodatkiVO();
                    pod.setDatum(new Date(rs.getTimestamp("dat_vno").getTime()));
                    pod.setTemperatura(rs.getDouble("temp"));
                    pod.setSlanost(rs.getDouble("sal"));
                    podatki.add(pod);
                }
                rs.close();
                st.close();

            } catch (SQLException ex) {
                Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return podatki;

    }

    private Connection getConnection() throws NamingException, SQLException {
        if (!connect()) {
            logger.log(Level.WARNING, "Cannot get connection");
        }
        return connection;
    }

}
