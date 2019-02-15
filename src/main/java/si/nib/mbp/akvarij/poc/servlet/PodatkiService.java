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
import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import si.nib.mbp.akvarij.poc.dao.DaoDbConnection;
import si.nib.mbp.akvarij.poc.pojo.PodatkiDTO;

@Path("/podatki")
public class PodatkiService {

    static final Logger logger = Logger.getLogger(PodatkiService.class.getName());

    @Context
    ServletContext servletContext;

    @GET
    @Path("/getTCS")
    @Produces(MediaType.APPLICATION_XML)
    public String getTCS() {
        logger.log(Level.INFO, "getTCS :: start");
        String retXml = (String) servletContext.getAttribute("ARDUINO_DATA");
        logger.log(Level.INFO, "getTCS :: end");
        return retXml;
    }

    @GET
    @Path("/tempslajson")
    @Produces(MediaType.APPLICATION_JSON)

    public String getPodatkiJson(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);
        logger.log(Level.INFO, "Podatki service DatumDo; {0}", datumDo);

        ArrayList<PodatkiDTO> podatki = new ArrayList<PodatkiDTO>();
        DaoDbConnection dao = new DaoDbConnection();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dao.geConnection();
            String sql;
            if (StringUtils.isNullOrEmpty(datumOd) || StringUtils.isNullOrEmpty(datumDo)) {
                sql = "select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30";
            } else {
                sql = "SELECT * FROM met_meritve WHERE  dat_vno BETWEEN '" + datumOd + "' AND '" + datumDo + "' order by dat_vno asc";

            }
            stmt = connection.prepareCall(sql);
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PodatkiDTO pod = new PodatkiDTO();
                pod.setDatum(new Date(rs.getTimestamp("dat_vno").getTime()));
                pod.setTemperatura(rs.getDouble("temp"));
                pod.setSlanost(rs.getDouble("sal"));
                podatki.add(pod);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            connection.close();
            connection = null;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                stmt = null;
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
                connection = null;
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(podatki);
    }

}
