package core.repository.dao;

import core.entity.AtlasUser;
import core.entity.ProjectMember;
import core.repository.AtlasUserDAO;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class AtlasUserDAOImpl implements AtlasUserDAO {

    private final DatabaseClient client;


    @Override
    public Mono<Integer> create(AtlasUser user) {
        return client
                .execute("insert into user_profile(sub_user_id, nickname, name, picture, email, email_verified, updated_at) " +
                        "values (:sub_user_id, :nickname, :name, :picture, :email, :email_verified, :updated_at)")
                .bind("sub_user_id", user.getSub())
                .bind("nickname", user.getNickname())
                .bind("name", user.getName())
                .bind("picture", user.getPicture())
                .bind("email", user.getEmail())
                .bind("email_verified", user.getEmailVerified())
                .bind("updated_at", user.getUpdatedAt())
                .fetch().rowsUpdated();
    }

}
