package com.FB_APP.demo.repositories;

import com.FB_APP.demo.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    @Override
    void deleteById(Integer integer);
}
