package com.unnamed.matchmaking.cs16_matchmaking.lobby.entity;

import com.unnamed.matchmaking.cs16_matchmaking.auditable.Auditable;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lobby")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Lobby extends Auditable implements Serializable {

    public Lobby(UUID id, String name, Match matchLobby, List<Player> listLobbyPlayer){
        this.id = id;
        this.name = name;
        this.matchLobby = matchLobby;
        typeMatchEvent = TypeMatch.DEFAULT;
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
    private TypeMatch typeMatchEvent;

    @OneToMany(mappedBy = "lobby", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> listLobbyPlayer;

}
