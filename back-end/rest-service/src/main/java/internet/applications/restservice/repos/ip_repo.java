package internet.applications.restservice.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import internet.applications.restservice.models.ip2location;

// an interface that extends the MongoRepository and plugs in
// the types of value and id for ip2location objects
public interface ip_repo extends MongoRepository<ip2location, String> {
    
}