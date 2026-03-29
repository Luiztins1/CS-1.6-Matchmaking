package com.unnamed.matchmaking.cs16_matchmaking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "player")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nickname", length = 26, nullable = false)
    private String nickname;

    @Column(name = "rank", nullable = false)
    private Rank rank;

    @Column(name = "kills", nullable = false)
    private Integer kills;

    @Column(name = "deaths", nullable = false)
    private Integer deaths;

    @Column(name = "country", length = 32, nullable = false)
    private String country;

    @Column(name = "last_conection")
    private Instant lastConection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_match", referencedColumnName = "id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lobby", referencedColumnName = "id")
    private Lobby lobby;
}
