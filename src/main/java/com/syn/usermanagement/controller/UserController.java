package com.syn.usermanagement.controller;

import com.syn.usermanagement.entity.User;
import com.syn.usermanagement.entity.WeatherResponse;
import com.syn.usermanagement.service.MessageProducer;
import com.syn.usermanagement.service.UserService;
import com.syn.usermanagement.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://35.182.51.105:4200")
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;
    private final MessageProducer messageProducer;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser( @RequestBody User user) {
        User createdUser = userService.createUser(user);
//        messageProducer.sendObject(createdUser, "creation");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
             @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getUserByWeatherAPI() {
        String  url = "http://api.weatherstack.com/current?access_key=f4ae8bcd7c6773390ff253ec1e5f6606&query=Delhi";
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(""), WeatherResponse.class
            );
            return ResponseEntity.ok(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle 4xx and 5xx errors
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(null); // or parse e.getResponseBodyAsString()
        } catch (Exception e) {
            // Handle other errors (network issues, etc.)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}