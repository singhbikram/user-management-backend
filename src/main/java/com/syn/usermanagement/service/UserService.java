package com.syn.usermanagement.service;

import com.syn.usermanagement.entity.User;
import com.syn.usermanagement.exception.ResourceNotFoundException;
import com.syn.usermanagement.exception.EmailAlreadyExistsException;
import com.syn.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + userDetails.getEmail());
        }

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Upload photo for a user
     */
    public User uploadUserPhoto(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Delete old photo if exists
        if (user.getPhotoUrl() != null) {
            s3Service.deleteFile(user.getPhotoUrl());
        }

        // Upload new photo to S3
        String photoUrl = s3Service.uploadFile(file, userId);

        // Update user with new photo URL
        user.setPhotoUrl(photoUrl);
        return userRepository.save(user);
    }

    /**
     * Delete user photo
     */
    public User deleteUserPhoto(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getPhotoUrl() != null) {
            s3Service.deleteFile(user.getPhotoUrl());
            user.setPhotoUrl(null);
            return userRepository.save(user);
        }

        return user;
    }

}