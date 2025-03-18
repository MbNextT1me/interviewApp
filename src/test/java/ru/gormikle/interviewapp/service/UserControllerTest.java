package ru.gormikle.interviewapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gormikle.interviewapp.controller.UserController;
import ru.gormikle.interviewapp.dto.CreateUserRequestDto;
import ru.gormikle.interviewapp.dto.UserDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void UserControllerTest_CreateUser_ReturnCreated() throws Exception {
        CreateUserRequestDto userRequestDto = new CreateUserRequestDto();
        userRequestDto.setName("Misha");
        userRequestDto.setDateOfBirth(LocalDate.of(2002, 4, 18));
        userRequestDto.setPassword("password!");
        userRequestDto.setPhone("79213456789");
        userRequestDto.setEmail("misha@gmail.com");
        userRequestDto.setInitialBalance(new BigDecimal(123));

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());

    }

    @Test
    public void getUserById_ReturnsUserDto() throws Exception {
        UserDto mockUser = new UserDto(
                1L,
                "Misha",
                LocalDate.of(2002, 4, 18),
                List.of("79213456789", "79111234567"),
                List.of("misha@gmail.com", "misha2@gmail.com")
        );

        Mockito.when(userService.getUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Misha"))
                .andExpect(jsonPath("$.dateOfBirth").value("2002-04-18"))
                .andExpect(jsonPath("$.phones[0]").value("79213456789"))
                .andExpect(jsonPath("$.phones[1]").value("79111234567"))
                .andExpect(jsonPath("$.emails[0]").value("misha@gmail.com"))
                .andExpect(jsonPath("$.emails[1]").value("misha2@gmail.com"));
    }
}
