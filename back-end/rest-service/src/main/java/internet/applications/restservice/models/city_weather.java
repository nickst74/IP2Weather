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
// class to represent objects matching city to weather conditions
@Document(collection = "city_weather")
public class city_weather {
    @Id
    private String city;
    private String weather, icon, temp, pressure, humidity, wind_speed, wind_deg;
    @CreatedDate
    private Date lastModifieDate;
    private Integer validityPeriod = 1; //valid for 1 hour

    // fetches new data from openweathermaps.org
    // passing ip2location object directly to constructor generated an error so this method to set the values is a workaround to that
    public void construct_city_weather(ip2location ip) throws Exception{
        String response;
        RestTemplate restTemplate = new RestTemplate();
        // try to retrive data from openweathermaps.org (hence the city might be invalid)
        // on invalid city retry with coordinates
        // in case of another thrown exception, global handler catches it
        try{
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=34bb621429743d65e44208cf9c39f3da", ip.getCity());
            response = restTemplate.getForObject(url, String.class);
        } catch(Exception ex){
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=34bb621429743d65e44208cf9c39f3da", ip.getLatitude(), ip.getLongitude());
            response = restTemplate.getForObject(url, String.class);
        }
        JSONObject obj = new JSONObject(response);
        this.city = obj.get("name").toString();
        this.weather = obj.getJSONArray("weather").getJSONObject(0).get("description").toString();
        this.icon = obj.getJSONArray("weather").getJSONObject(0).get("icon").toString();
        this.temp = obj.getJSONObject("main").get("temp").toString();
        this.pressure = obj.getJSONObject("main").get("pressure").toString();
        this.humidity = obj.getJSONObject("main").get("humidity").toString();
        this.wind_speed = obj.getJSONObject("wind").get("speed").toString();
        this.wind_deg = obj.getJSONObject("wind").get("deg").toString();
        this.lastModifieDate = new Date();
    }

    public Boolean recent(){
        Date threshhold = http_controller.addHoursToJavaUtilDate(this.lastModifieDate, this.validityPeriod);
        return threshhold.after(new Date());
    }

    public void setCity(String city){
        this.city = city;
    }

}   