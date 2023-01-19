package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

public interface PlayerService {

    Page getPlayersList(Specification<Player> specification, Pageable pageable);
    Integer getPlayersCount(Specification<Player> specification);
    ResponseEntity<?> create(Player player);
    ResponseEntity<?> getPlayerById(String id);
    ResponseEntity<?> updateById(String id, Player player);
    ResponseEntity<?> deleteById(String id);
}
