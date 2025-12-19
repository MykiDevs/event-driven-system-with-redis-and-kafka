package org.ikitadevs.kafkatestuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.kafkatestuserservice.dto.request.UserCreateDto;
import org.ikitadevs.kafkatestuserservice.dto.request.UserUpdateDto;
import org.ikitadevs.kafkatestuserservice.dto.response.PaginationResponseDto;
import org.ikitadevs.kafkatestuserservice.dto.response.UserDto;
import org.ikitadevs.kafkatestuserservice.mapper.UserMapper;
import org.ikitadevs.kafkatestuserservice.models.User;
import org.ikitadevs.kafkatestuserservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get all users using pagination")
    public ResponseEntity<PaginationResponseDto<List<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(userService.getAllUsersWithPagination(sortDirection, sortBy, pageSize, page));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.getUserById(id)));
    }

    @PostMapping
    @Operation(summary = "Create user by id")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto dto) {
        UserDto userDto = userService.createUser(dto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user by id")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUserById(id, userUpdateDto));
    }
}
