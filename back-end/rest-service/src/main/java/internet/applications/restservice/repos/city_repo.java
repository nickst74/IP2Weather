package internet.applications.restservice.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import internet.applications.restservice.models.city_weather;

// an interface that extends the MongoRepository and plugs in
// the types of value and id for city_weather objects
public interface city_repo extends MongoRepository<city_weather, String> {
    
}