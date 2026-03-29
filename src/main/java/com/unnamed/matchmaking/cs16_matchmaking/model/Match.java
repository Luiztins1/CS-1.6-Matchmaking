package com.unnamed.matchmaking.cs16_matchmaking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
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

    @Column(name = "game_map")
    private GameMap map;

    @Column(name = "time_match_map")
    private LocalTime timeMatchMap;

    @OneToMany(mappedBy = "match")
    private List<Player> listPlayer;

}
