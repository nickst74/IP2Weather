package internet.applications.restservice.ContentNegotiation;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// overriding the context negotiation configurer of the Model-View-Controller
// to let the user decide between XML and JSON (default) formatted response
// through mediaType variavle in http get request method
@SuppressWarnings( "deprecation" )
@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

	    //set path extension to false
	    configurer.favorPathExtension(false).
        //request parameter ("format" by default) should be used to determine the requested media type
        favorParameter(true).
        //the favour parameter is set to "mediaType" instead of default "format"
        parameterName("mediaType").
        //ignore the accept headers
        ignoreAcceptHeader(true).
        //dont use Java Activation Framework since we are manually specifying the mediatypes required below
        useJaf(false).
        defaultContentType(MediaType.APPLICATION_JSON).
        mediaType("xml", MediaType.APPLICATION_XML).
        mediaType("json", MediaType.APPLICATION_JSON);
  }
}