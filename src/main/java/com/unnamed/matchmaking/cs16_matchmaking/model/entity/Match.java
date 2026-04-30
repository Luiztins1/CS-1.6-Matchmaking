package com.unnamed.matchmaking.cs16_matchmaking.model.entity;

import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "match_name", length = 32,nullable = false)
    private String nameMatch;

    @Column(name = "game_map", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameMap map;

    @Column(name = "match_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchState matchState;

    @Column(name = "time_match_map", nullable = false)
    private Instant timeMatchMap;

    @OneToOne(mappedBy = "matchLobby", cascade = CascadeType.ALL)
    private Lobby lobbyMatch;

    @OneToMany(mappedBy = "match", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> listPlayer;

}
