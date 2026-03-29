package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    public void deletePlayer(UUID uuid){
        playerRepository.deleteById(uuid);
    }

    public Optional<Player> updatePlayer(UUID uuid, Player player){
        return playerRepository.findById(uuid).map(existPlayer ->{
            existPlayer.setNickname(player.getNickname());
            existPlayer.setRank(player.getRank());
            existPlayer.setKills(player.getKills());
            existPlayer.setDeaths(player.getDeaths());
            existPlayer.setCountry(player.getCountry());
            existPlayer.setLastConection(player.getLastConection());
            return playerRepository.save(existPlayer);
        });
    }

    public Optional<Player> findByIdPlayer(UUID uuid){
        return playerRepository.findById(uuid);
    }
}
