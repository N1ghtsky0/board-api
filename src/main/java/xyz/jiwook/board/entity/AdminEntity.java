package xyz.jiwook.board.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("admin")
public class AdminEntity extends BaseAccountEntity {
    public AdminEntity(String username, String password) {
        super(username, password);
    }
}
