package pl.sportovo.domain.athlete.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PublicAthlete {
    private UUID id;
    private String username;
    private String bio;
}
