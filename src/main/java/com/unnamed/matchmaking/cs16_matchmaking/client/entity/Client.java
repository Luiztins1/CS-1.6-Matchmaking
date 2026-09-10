package com.unnamed.matchmaking.cs16_matchmaking.client.entity;

import com.unnamed.matchmaking.cs16_matchmaking.auditable.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_login", nullable = false)
    private String clientLogin;

    @Column(name = "client_password", nullable = false)
    private String clientPassword;

    @Column(name = "redirect_uri", nullable = false)
    private String redirectUri;

    @Column(name = "scope", nullable = false)
    private String scope;

}
