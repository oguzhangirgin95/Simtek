package modules.firstlevel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import models.firstlevel.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByUsernameAndPassword(String username, String password);
}
