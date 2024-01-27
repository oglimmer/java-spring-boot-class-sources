package com.oglimmer.kniffel.db.repository;

import com.oglimmer.kniffel.db.entity.KniffelGame;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KniffelGameRepository extends CrudRepository<KniffelGame, Long> {

    Optional<KniffelGame> findByGameId(String gameId);

}
