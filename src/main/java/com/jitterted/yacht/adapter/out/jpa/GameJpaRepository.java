package com.jitterted.yacht.adapter.out.jpa;

import org.springframework.data.repository.CrudRepository;

public interface GameJpaRepository extends CrudRepository<GameDbo, Long> {
}
