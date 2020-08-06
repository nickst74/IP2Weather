package internet.applications.restservice.models;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.client.RestTemplate;

import internet.applications.restservice.http_controller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
// class to represent objects matching ip to location (city and geographic coordinates)
@Document(collection = "ip2location")
public class ip2location {
    @Id
    private String ip;
    private String latitude;
    private String longitude;
    private String city;
    @CreatedDate
    private Date lastModifieDate;
    private Integer validityPeriod = 5; //valid for 5 hours

    // fetches new data from freegeoip.app
    public void construct_ip2location(String ip) throws Exception{
        String url = String.format("https://freegeoip.app/json/%s", ip);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        JSONObject obj = new JSONObject(response);
        this.ip = obj.get("ip").toString();
        this.latitude = obj.get("latitude").toString();
        this.longitude = obj.get("longitude").toString();
        this.city = obj.get("city").toString();
        this.lastModifieDate = new Date();
    }

    public Boolean valid_city(){
        if(this.city == null || this.city.isEmpty()){
            return false;
        }
        return true;
    }

    public Boolean recent(){
        Date threshhold = http_controller.addHoursToJavaUtilDate(this.lastModifieDate, this.validityPeriod);
        return threshhold.after(new Date());
    }

    public void setCity(String city){
        this.city = city;
    }

}