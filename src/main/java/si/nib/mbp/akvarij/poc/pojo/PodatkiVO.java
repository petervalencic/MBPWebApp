
package si.nib.mbp.akvarij.poc.pojo;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "podatki")
public class PodatkiVO {

    Date datum;
    Double temperatura;
    Double slanost;

    @XmlElement
    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @XmlElement
    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    @XmlElement
    public Double getSlanost() {
        return slanost;
    }

    public void setSlanost(Double slanost) {
        this.slanost = slanost;
    }

}
