package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Long create(User u);
    boolean update(User u);
    boolean deleteById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
}
