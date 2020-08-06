package internet.applications.restservice.models;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

@Getter

@XmlRootElement
public class final_data {

    private String ip;        //ip given by user
    private String lat;       //latitude
    private String lon;       //longitude
    private String city;
    private String weather;   //weather description
    private String icon;      //corresponding icon to weather
    private String temp;      //temperature in kelvin
    private String pressure;  //barometric pressure
    private String humidity;
    private String windSpeed; //wind speed in meters per second
    private String windDeg;   //wind direction, degrees (meteorological)

    // just a sum-up of all data to return to user
    public final_data(ip2location ip, city_weather cw){
        this.ip = ip.getIp();
        this.lat = ip.getLatitude();
        this.lon = ip.getLongitude();
        this.city = ip.getCity();
        this.weather = cw.getWeather();
        this.icon = cw.getIcon();
        this.temp = cw.getTemp();
        this.pressure = cw.getPressure();
        this.humidity = cw.getHumidity();
        this.windSpeed = cw.getWind_speed();
        this.windDeg = cw.getWind_deg();
    }
}