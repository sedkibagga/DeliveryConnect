package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    @NonNull
    Optional<User> findById(@NonNull Integer id);

    @NonNull
    Optional<User> findByName(String name);

    List<User> findByNameContaining(String name);

}
