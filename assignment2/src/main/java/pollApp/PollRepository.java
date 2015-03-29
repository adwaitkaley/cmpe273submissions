package pollApp;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Adwait on 3/28/2015.
 */
public interface PollRepository extends MongoRepository<Poll,String> {

    public Poll findById(String id);

}
