package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.mapper.UserMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.user.User;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import com.oliq04.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
    void getUsers_CorrectData_PageOfUsers() {
        //given
        int pageNumber = 0;
        int pageSize = 3;
        Pageable page = PageRequest.of(0, 3);
        User firstUser = User.builder()
                .id(1L)
                .username("User")
                .password("123456")
                .email("email@wp.pl")
                .patient(null)
                .doctor(null)
                .build();
        User secondUser = User.builder()
                .id(2L)
                .username("User")
                .password("123456")
                .email("emailtwo@wp.pl")
                .patient(null)
                .doctor(null)
                .build();
        List<User> users = new ArrayList<>(List.of(firstUser, secondUser));
        PageImpl<User> userPage = new PageImpl<>(users, page, users.size());
        List<UserDto> userDtos = userPage.stream()
                .map(userMapper::toDto)
                .toList();
        when(userRepository.findAll(page)).thenReturn(userPage);
        //when
        PageableDto<UserDto> result = userService.getUsers(pageNumber, pageSize);
        //then
        Assertions.assertAll(
                () -> assertEquals(pageSize, result.getPageSize()),
                () -> assertEquals(0, result.getCurrentPage()),
                () -> assertEquals(users.size(), result.getTotal()),
                () -> assertEquals(userDtos, result.getContent())
        );
    }

    @Test
    void addUser_CorrectData_AddedUser() {
        //given
        UserCommand userCommand = UserCommand.builder()
                .username("User")
                .password("12345678")
                .email("email@wp.pl")
                .build();
        User user = userMapper.toEntityFromCommand(userCommand);
        when(userRepository.save(user)).thenReturn(user);
        //when
        UserDto result = userService.addUser(userCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals("User", result.getUsername()),
                () -> assertEquals("email@wp.pl", result.getEmail())
        );
    }

    @Test
    void getUserByEmail_CorrectData_SpecificUser() {
        //given
        String email = "email@wp.pl";
        User user = User.builder()
                .id(1L)
                .username("User")
                .password("123456")
                .email("email@wp.pl")
                .patient(null)
                .doctor(null)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        //when
        UserDto result = userService.getUserByEmail(email);
        //then
        Assertions.assertAll(
                () -> assertEquals("User", result.getUsername()),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(email, result.getEmail())
        );
    }

    @Test
    void editUser_CorrectData_EditedUser() {
        //given
        String email = "email@wp.pl";
        UserCommand userCommand = UserCommand.builder()
                .username("newName")
                .password("123456")
                .email("newEmail@wp.pl")
                .build();
        User user = User.builder()
                .id(1L)
                .username("User")
                .password("123456")
                .email("email@wp.pl")
                .patient(null)
                .doctor(null)
                .build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //when
        UserDto result = userService.editUser(email, userCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals("newEmail@wp.pl", result.getEmail()),
                () -> assertEquals("newName", result.getUsername()),
                () -> assertEquals(1L, result.getId())
        );
    }

    @Test
    void deleteUserByEmail_CorrectData_UserRemoved() {
        //given
        User user = User.builder()
                .id(3L)
                .username("User")
                .password("12345")
                .email("email").
                build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));
        //when
        userService.deleteUserByEmail("email");
        //then
        verify(userRepository).delete(user);
    }
}
