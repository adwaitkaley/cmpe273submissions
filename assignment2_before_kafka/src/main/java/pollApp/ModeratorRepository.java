package pollApp;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ModeratorRepository extends MongoRepository<Moderator,String> {

    public Moderator findById(int id);
    public List<Moderator> findAll();

}
