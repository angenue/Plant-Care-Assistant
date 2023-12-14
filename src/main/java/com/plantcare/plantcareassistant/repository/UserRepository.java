package com.plantcare.plantcareassistant.repository;

import com.plantcare.plantcareassistant.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
