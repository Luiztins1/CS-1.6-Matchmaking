package com.unnamed.matchmaking.cs16_matchmaking.model.entity;

import com.unnamed.matchmaking.cs16_matchmaking.model.enums.TypeMatchEvent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lobby")
@Data
@NoArgsConstructor
public class Lobby implements Serializable {

    public Lobby(UUID id, String name, Match matchLobby, List<Player> listLobbyPlayer){
        this.id = id;
        this.name = name;
        this.matchLobby = matchLobby;
        typeMatchEvent = TypeMatchEvent.DEFAULT;
        this.listLobbyPlayer = listLobbyPlayer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "match_lobby_id", referencedColumnName = "id")
    private Match matchLobby;

    @Column(name = "type_match_event", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeMatchEvent typeMatchEvent;

    @OneToMany(mappedBy = "lobby", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> listLobbyPlayer;

}
