package core.dao.impl;

import core.dao.AtlasUserDAO;
import core.entity.AtlasUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AtlasUserDAOImpl implements AtlasUserDAO {

    private final DatabaseClient client;

    @Override
    public Mono<Integer> create(AtlasUser user) {
        return client
                .sql("insert into user_profile(sub_user_id, email, email_verified, family_name, given_name, name, local, picture) " +
                        "values (:sub_user_id, :email, :email_verified, :family_name, :given_name, :name, :local, :picture)")
                .bind("sub_user_id", user.getSub())
                .bind("email", user.getEmail())
                .bind("email_verified", user.getEmailVerified())
                .bind("family_name", user.getFamilyName())
                .bind("given_name", user.getGivenName())
                .bind("name", user.getName())
                .bind("local", user.getLocal())
                .bind("picture", setPicture())
                .fetch().rowsUpdated();
    }

    private String setPicture() {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0: return "assets/images/user/alien.svg";
            case 1: return "assets/images/user/alien_1.svg";
            default: return "assets/images/user/alien_2.svg";
        }
    }

    @Override
    public Mono<Integer> update(AtlasUser user) {
        return client
                .sql("update user_profile set family_name = :family_name, given_name = :given_name, name = :name, local = :local, picture = :picture where sub_user_id = :sub_user_id")
                .bind("family_name", user.getFamilyName())
                .bind("given_name", user.getGivenName())
                .bind("name", user.getName())
                .bind("local", user.getLocal())
                .bind("picture", user.getUserPicture())
                .fetch().rowsUpdated();
    }

    @Override
    public Mono<Integer> updateEmailVerification(boolean emailVerification, String sub) {
        return client
                .sql("update user_profile set email_verified = :email_verified where sub_user_id = :sub_user_id")
                .bind("email_verified", emailVerification)
                .bind("sub_user_id", sub)
                .fetch().rowsUpdated();
    }

}
