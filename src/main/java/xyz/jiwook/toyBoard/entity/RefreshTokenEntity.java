package xyz.jiwook.toyBoard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    private String token;
    private String ipAddress;
    private String username;
    private LocalDateTime createdAt;

    public RefreshTokenEntity(String token, String ipAddress, String username) {
        this.token = token;
        this.ipAddress = ipAddress;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }
}
