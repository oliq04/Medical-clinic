package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.UserAlreadyExistsException;
import com.oliq04.medicalclinic.exceptions.UserNotFoundException;
import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import com.oliq04.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PageableDto<UserDto> getUsers(int pageNumber, int usersCount) {
        Pageable page = PageRequest.of(pageNumber, usersCount);
        Page<User> userPage = userRepository.findAll(page);
        List<UserDto> userDtos = userPage.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return PageableDto.toPageable(userDtos, userPage);
    }

    @Transactional
    public UserDto addUser(UserCommand user) {
        User userEntity = userMapper.toEntityFromCommand(user);
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public UserDto getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND)));
    }

    @Transactional
    public UserDto editUser(String email, UserCommand userCommand) {
        if (userRepository.existsByEmail(email) && !userCommand.getEmail().equals(email)) {
            throw new UserAlreadyExistsException("User with given email already exists", HttpStatus.CONFLICT);
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
        user.update(userCommand);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }
}
