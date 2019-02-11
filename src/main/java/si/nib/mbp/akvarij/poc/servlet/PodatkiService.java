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
import si.nib.mbp.akvarij.poc.pojo.PodatkiJson;

@Path("/podatki")
public class PodatkiService {
    private static final DaoDbConnection dao = new DaoDbConnection();

    static final Logger logger = Logger.getLogger(PodatkiService.class.getName());
    @GET
    @Path("/tempslajson")
    @Produces(MediaType.APPLICATION_JSON)

    public String getPodatkiJson(@QueryParam("datumOd") String datumOd, @QueryParam("datumDo") String datumDo) {
        logger.log(Level.INFO, "Podatki service DatumOd; {0}", datumOd);

        ArrayList<PodatkiJson> podatki = new ArrayList<PodatkiJson>();

        try {
            Connection connection = dao.getConnection();
            PreparedStatement stmt = null;
            ResultSet rs = null;
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
            rs.close(); rs = null;
            stmt.close(); stmt = null;
            connection.close(); connection = null;
        } catch (SQLException ex) {
            Logger.getLogger(PodatkiService.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(podatki);

    }
}
