package com.oglimmer.kniffel.db.repository;

import com.oglimmer.kniffel.db.entity.KniffelPlayer;
import org.springframework.data.repository.CrudRepository;

public interface KniffelPlayerRepository extends CrudRepository<KniffelPlayer, Long> {
    
}
