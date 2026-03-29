package com.unnamed.matchmaking.cs16_matchmaking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "player")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nickname", length = 16, nullable = false)
    private String nickname;

    @Column(name = "rank", length = 20, nullable = false)
    private Integer rank;

    @Column(name = "kills", length = 200, nullable = false)
    private Integer kills;

    @Column(name = "deaths", length = 200, nullable = false)
    private Integer deaths;

    @Column(name = "country", length = 32, nullable = false)
    private String country;

    @Column(name = "last_conection")
    private Instant lastConection;
}
