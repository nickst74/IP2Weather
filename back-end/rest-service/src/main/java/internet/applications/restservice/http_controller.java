package internet.applications.restservice;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import internet.applications.restservice.error_hander.IPException;
import internet.applications.restservice.models.city_weather;
import internet.applications.restservice.models.final_data;
import internet.applications.restservice.models.ip2location;
import internet.applications.restservice.repos.city_repo;
import internet.applications.restservice.repos.ip_repo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Create rest controller to handle incoming HTTP requests
@RestController
// Enable CORS (client and server will be on the same host machine)
@CrossOrigin(origins = "*")
public class http_controller {

    // a simple regex for ip4 validation (ipv6 not implemented)
    private static final String IPV4_REGEX =
					"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

    @Autowired
    private ip_repo repo1;
    @Autowired
    private city_repo repo2;

    // just a static method that returns the date after adding <hours> hours
    public static Date addHoursToJavaUtilDate(Date date, int hours) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return calendar.getTime();
    }

    //use the regex expression to check ip parameter format
    public Boolean ip_check(String ip){
        if(ip == ""){
            return false;
        }
        Matcher match = IPv4_PATTERN.matcher(ip);
        return match.matches();
    }

    // method for our endpoint (only one implemented)
    @GetMapping(value = "/ip2weather", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public final_data response(@RequestParam(value = "ip", defaultValue = "") String val) throws Exception{
        //ip variable required
        if(val.isEmpty()){
            throw new IPException("IP parameter required");
        }
        ///////////Implement ip parameter checking?????????
        if(!ip_check(val)){
            throw new IPException(String.format("Invalid Ip format: %s", val));
        }
        // Search db for given ip
        Optional<ip2location> iploc = repo1.findById(val);
        ip2location ip;
        // if not present in db or is outdated then create new else fetch it
        if (!(iploc.isPresent() && iploc.get().recent())){
            ip = new ip2location(); //not inserted to db???????????
            ip.construct_ip2location(val);
        } else{
            ip = iploc.get();
        }

        // search db for weather in specific location
        Optional<city_weather> weather = repo2.findById(ip.getCity());
        city_weather cw;
        // if not present in db or is outdated then create new else fetch it
        if(!(weather.isPresent() && weather.get().recent())){
            cw = new city_weather(); //not inserted to db???????????
            cw.construct_city_weather(ip);
        } else{
            cw = weather.get();
        }
        // check if matched with global ip
        // meaning that web services weren't able to recognise it
        if(cw.getCity().equals(new String("Globe"))){
            throw new IPException("IP not recognised, try another one");
        }
        // in case the two fetched data refer to different city names
        // also check if ip.city is empty (sometimes returns empty string)
        if(ip.getCity() != cw.getCity()){
            if(ip.valid_city()){
                cw.setCity(ip.getCity());
            } else{
                ip.setCity(cw.getCity());
            }
        }
        // store to cache (db) if not already there
        if(!iploc.isPresent() || !iploc.get().recent()){
            repo1.save(ip);
        }
        if(!weather.isPresent() || !weather.get().recent()){
            repo2.save(cw);
        }
        // return the requested data
        return new final_data(ip, cw);
    }

}
