package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.Claims;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimsRepository extends JpaRepository<Claims, Integer> {
}
