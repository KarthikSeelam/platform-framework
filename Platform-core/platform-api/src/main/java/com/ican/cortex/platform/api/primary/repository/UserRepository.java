package com.ican.cortex.platform.api.primary.repository;


import com.ican.cortex.platform.api.primary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom query methods here if needed
    User findByEmail(String email);
}
