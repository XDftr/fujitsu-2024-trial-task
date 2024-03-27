package ee.fujitsu.trialtask.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@JacksonXmlRootElement(localName = "station")
public class StationDto {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "wmocode")
    private Integer wmocode;

    @JacksonXmlProperty(localName = "phenomenon")
    private String phenomenon;

    @JacksonXmlProperty(localName = "airtemperature")
    private Double airTemperature;

    @JacksonXmlProperty(localName = "windspeed")
    private Double windSpeed;
}
