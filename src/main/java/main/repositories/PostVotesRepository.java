package main.repositories;

import main.model.PostVotes;
import org.springframework.data.repository.CrudRepository;

public interface PostVotesRepository extends CrudRepository<PostVotes, Integer> {

}
