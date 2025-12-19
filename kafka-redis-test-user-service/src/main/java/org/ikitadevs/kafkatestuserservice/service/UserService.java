package org.ikitadevs.kafkatestuserservice.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.kafkatestuserservice.dto.request.UserCreateDto;
import org.ikitadevs.kafkatestuserservice.dto.request.UserUpdateDto;
import org.ikitadevs.kafkatestuserservice.dto.response.PaginationResponseDto;
import org.ikitadevs.kafkatestuserservice.dto.response.UserDto;
import org.ikitadevs.kafkatestuserservice.mapper.UserMapper;
import org.ikitadevs.kafkatestuserservice.models.Event;
import org.ikitadevs.kafkatestuserservice.models.User;
import org.ikitadevs.kafkatestuserservice.models.enums.EventState;
import org.ikitadevs.kafkatestuserservice.repository.EventRepository;
import org.ikitadevs.kafkatestuserservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserServiceCaching userServiceCaching;
    private final KafkaProducerService kafkaProducerService;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public PaginationResponseDto<List<UserDto>> getAllUsersWithPagination(
      String sortDirection,
      String sortBy,
      int pageSize,
      int page
    ) {

        if(pageSize > 20) pageSize = 20;
        if(!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            sortDirection = "ASC";
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        return PaginationResponseDto.<List<UserDto>>builder()
                .content(userDtos)
                .page(page)
                .size(pageSize)
                .sortDirection(sortDirection)
                .sortBy(sortBy)
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .build();
    }
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        if (userServiceCaching.getById(id) != null) {
            log.info(String.valueOf(userServiceCaching.getById(id)));
            return userServiceCaching.getById(id);
        }
        log.info(String.valueOf(userServiceCaching.getById(id)));
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with this id does not exists!"));
    }


    @Transactional
    public UserDto createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByEmail(userCreateDto.getEmail())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists!");
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setName(userCreateDto.getName());
        user.setPassword(userCreateDto.getPassword());
        user.setLastname(userCreateDto.getLastname());
        userRepository.save(user);
        userServiceCaching.put(user);
        Event event = Event.builder()
                .userEmail(user.getEmail())
                .eventState(EventState.PROCESSING)
                .addedAt(Instant.now()).build();
        eventRepository.save(event);
        CompletableFuture.runAsync(() -> {
            kafkaProducerService.sendEvent(event, "user.email.send");
        });
        return userMapper.toDto(user);
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        userServiceCaching.delete(id);
    }

    @Transactional
    public UserDto updateUserById(Long id, UserUpdateDto dto) {
        User user = getUserById(id);
        if(!dto.getOldPassword().equals(user.getPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials!");
        userMapper.updateUserFromDto(dto, user);
        userServiceCaching.put(user);
        return userMapper.toDto(user);
    }
}
