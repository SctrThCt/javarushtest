package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service("playerService")
@Repository
@Transactional
public class PlayerServiceImpl implements PlayerService{

    private PlayersRepository playersRepository;
    @Autowired
    public void setPlayersRepository(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    @Override
    public Page getPlayersList(Specification<Player> specification, Pageable pageable) {
        return playersRepository.findAll(specification,pageable);
    }

    @Override
    public Integer getPlayersCount(Specification<Player> specification) {
        return Math.toIntExact(playersRepository.count(specification));
    }

    @Override
    public ResponseEntity<?> create(Player player) {
        if (!HelpService.validatePlayer(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        player.setLevel(HelpService.getLevel(player.getExperience()));
        player.setUntilNextLevel(HelpService.getExpUntilNextLevel(player.getExperience()));

        Player newPlayer = playersRepository.save(player);

        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPlayerById(String id) {

        Long identifier;
        if ((identifier = HelpService.validateId(id))==null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional player = playersRepository.findById(identifier);

        if (!player.isPresent())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player.get(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateById(String id, Player player) {

        Long identifier;

        if ((identifier = HelpService.validateId(id))==null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional playerOld = playersRepository.findById(identifier);

        if (!playerOld.isPresent())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player playerUpdated = (Player) playerOld.get();

        if (player.getName()!=null) playerUpdated.setName(player.getName());
        if (player.getTitle()!=null) playerUpdated.setTitle(player.getTitle());
        if (player.getRace()!=null) playerUpdated.setRace(player.getRace());
        if (player.getProfession()!=null) playerUpdated.setProfession(player.getProfession());
        if (player.getBirthday()!=null) playerUpdated.setBirthday(player.getBirthday());
        if (player.getBanned()!=null) playerUpdated.setBanned(player.getBanned());
        if (player.getExperience()!=null) {
            playerUpdated.setExperience(player.getExperience());
            playerUpdated.setLevel(HelpService.getLevel(playerUpdated.getExperience()));
            playerUpdated.setUntilNextLevel(HelpService.getExpUntilNextLevel(playerUpdated.getExperience()));
        }

        if (!HelpService.validatePlayer(playerUpdated))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            playersRepository.save(playerUpdated);
        }
        return new ResponseEntity<>(playerUpdated, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteById(String id) {

        Long identifier;
        if ((identifier = HelpService.validateId(id))==null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional player = playersRepository.findById(identifier);

        if (!player.isPresent())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        playersRepository.deleteById(identifier);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
