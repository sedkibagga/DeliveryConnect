package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.Livraison;
import com.FB_APP.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Integer> {
    List<Livraison> findByUser(User user);
}
