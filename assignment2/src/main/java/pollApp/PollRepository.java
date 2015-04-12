package pollApp;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Adwait on 3/28/2015.
 */
public interface PollRepository extends MongoRepository<Poll,String> {

    public Poll findById(String id);
    public List<Poll> findAll();

}
