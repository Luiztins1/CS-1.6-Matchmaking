package com.unnamed.matchmaking.cs16_matchmaking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lobby")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lobby implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "lobby", nullable = false)
    private String lobby;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> listLobby;
}
