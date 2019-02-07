package si.nib.mbp.akvarij.poc.pojo;

import java.util.Date;
import java.util.logging.Logger;
import org.joda.time.DateTime;

public class FormaBean {

    static final Logger logger = Logger.getLogger(FormaBean.class.getName());

    Date datum = new Date();

    String datumOd;
    String datumDo;
    String id;

    public FormaBean() {

        datumOd = new DateTime(new Date()).minusDays(3).toString("yyyy-MM-dd HH:mm");
        datumDo = new DateTime(new Date()).toString("yyyy-MM-dd HH:mm");
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

}
