package com.example.ecommers1.service;

import com.example.ecommers1.model.User;
import com.example.ecommers1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }


    public User register(User user) {
        return repo.save(user);
    }

    // ✅ Login
    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ✅ Update existing user
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = repo.findById(id);
        if (optionalUser.isPresent()) {
            User existing = optionalUser.get();
            existing.setName(updatedUser.getName());
            existing.setEmail(updatedUser.getEmail());
            existing.setPassword(updatedUser.getPassword());
            existing.setRole(updatedUser.getRole());
            return repo.save(existing); // save updated values
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    // ✅ Delete user
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
