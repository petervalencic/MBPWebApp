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
import si.nib.mbp.akvarij.poc.dao.DaoDbConnection;
import si.nib.mbp.akvarij.poc.pojo.PodatkiJson;

@Path("/podatki")
public class PodatkiService {

    private final DaoDbConnection dao;

    static final Logger logger = Logger.getLogger(PodatkiService.class.getName());

    public PodatkiService() {
        this.dao = new DaoDbConnection();
    }

    @GET
    @Path("/tempslajson")
    @Produces(MediaType.APPLICATION_JSON)

    public String getPodatkiJson(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);
        logger.log(Level.INFO, "Podatki service DatumDo; {0}", datumDo);

        ArrayList<PodatkiJson> podatki = new ArrayList<PodatkiJson>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dao.getDBConnection();

            String sql;
            if (StringUtils.isNullOrEmpty(datumOd) || StringUtils.isNullOrEmpty(datumDo)) {
                sql = "select temp,sal,dat_vno from met_meritve order by dat_vno desc limit 30";
            } else {
                sql = "SELECT * FROM met_meritve WHERE  dat_vno BETWEEN '" + datumOd + "' AND '" + datumDo + "' order by dat_vno asc";

            }
            stmt = connection.prepareCall(sql);
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PodatkiJson pod = new PodatkiJson();
                pod.setDatum(new Date(rs.getTimestamp("dat_vno").getTime()));
                pod.setTemperatura(rs.getDouble("temp"));
                pod.setSlanost(rs.getDouble("sal"));
                podatki.add(pod);
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(podatki);
    }

}
