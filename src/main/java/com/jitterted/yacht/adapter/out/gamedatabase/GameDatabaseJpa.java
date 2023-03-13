package com.jitterted.yacht.adapter.out.gamedatabase;

import org.springframework.data.repository.CrudRepository;

public interface GameDatabaseJpa extends CrudRepository<GameRow, Long> {
}
