package com.oliq04.medicalclinic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliq04.medicalclinic.model.PageableDto;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.model.user.UserCommand;
import com.oliq04.medicalclinic.model.user.UserDto;
import com.oliq04.medicalclinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_CorrectData_UsersPageReturned() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 1);
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("User")
                .email("user@wp.pl")
                .build();

        List<UserDto> userDtoList = new ArrayList<>(List.of(userDto));
        PageImpl<UserDto> userDtoPage = new PageImpl<>(userDtoList, pageRequest, 1);
        PageableDto<UserDto> pageOfUsers = PageableDto.toPageable(userDtoList, userDtoPage);

        PageableDto<UserDto> expectedPage = PageableDto.<UserDto>builder()
                .pageSize(1)
                .currentPage(0)
                .total(1)
                .totalPages(1)
                .content(userDtoList)
                .build();
        when(userService.getUsers(0, 1)).thenReturn(pageOfUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageOfUsers)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void getUserByEmail_CorrectData_UserReturned() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("User")
                .email("user@wp.pl")
                .build();
        when(userService.getUserByEmail(any())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{email}", "email@wp.pl")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("User"))
                .andExpect(jsonPath("$.email").value("user@wp.pl"));
    }

    @Test
    void createUser_CorrectData_CreatedUserReturned() throws Exception {
        UserCommand userCommand = UserCommand.builder()
                .username("Username")
                .password("dsadasdsda")
                .email("email@wp.pl")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("Username")
                .email("email@wp.pl")
                .build();
        when(userService.addUser(any())).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Username"))
                .andExpect(jsonPath("$.email").value("email@wp.pl"));
    }

    @Test
    void editUser_CorrectData_EditedUserReturned() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("Username")
                .email("email@wp.pl")
                .build();
        UserCommand userCommand = UserCommand.builder()
                .username("Username")
                .password("dsadasdsda")
                .email("email@wp.pl")
                .build();
        when(userService.editUser(any(), any())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{email}", "email@wp.pl")
                        .content(objectMapper.writeValueAsString(userCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Username"))
                .andExpect(jsonPath("$.email").value("email@wp.pl"));
    }

    @Test
    void deleteUserByEmail_CorrectData_UserDeletedNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{email}", "email@wp.pl"))
                .andExpect(status().isNoContent());
        verify(userService).deleteUserByEmail("email@wp.pl");
    }
}
