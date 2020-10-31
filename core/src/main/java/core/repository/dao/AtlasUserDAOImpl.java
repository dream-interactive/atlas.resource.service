package core.repository.dao;

import core.entity.AtlasUser;
import core.repository.AtlasUserDAO;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class AtlasUserDAOImpl implements AtlasUserDAO {

    private final DatabaseClient client;


    @Override
    public Mono<Integer> create(AtlasUser user) {
        Objects.requireNonNull(user, "user");
        return client
                .execute("insert into user_profile(sub_user_id, nickname, name, picture, email, email_verified, last_modify) " +
                        "values (:sub_user_id, :nickname, :name, :picture, :email, :email_verified, :last_modify)")
                .bind("sub_user_id", user.getSub())
                .bind("nickname", user.getNickname())
                .bind("name", user.getName())
                .bind("picture", user.getPicture())
                .bind("email", user.getEmail())
                .bind("email_verified", user.getEmailVerified())
                .bind("last_modify", user.getUpdatedAt())
                .fetch().rowsUpdated();
    }

    @Override
    public Mono<Integer> update(AtlasUser user) {
        Objects.requireNonNull(user, "user");
        return client
                .execute("update user_profile set name = :name, nickname = :nickname, picture = :picture, email_verified = :email_verified")
                .bind("name", user.getName())
                .bind("nickname", user.getNickname())
                .bind("picture", user.getPicture())
                .bind("email_verified", user.getEmailVerified())
                .fetch().rowsUpdated();
    }

}
