package com.system.personalfinance.resources;

import com.system.personalfinance.dtos.user.CreateUserRequest;
import com.system.personalfinance.dtos.user.UserResponse;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserResponse> devCreate(@RequestBody CreateUserRequest request) {
        User user = userService.create(request);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
        URI location = URI.create("/api/users/" + user.getId());

        return ResponseEntity.created(location).body(response);
    }
}
