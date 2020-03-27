package pl.mkapiczy.usermanagerservice.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;
import pl.mkapiczy.usermanagerservice.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.mkapiczy.usermanagerservice.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetAllUsers() {
        // Given

        when(userService.getAll()).thenReturn(Collections.singletonList(USER_DTO));

        // When
        var response = restTemplate.exchange("/users", HttpMethod.GET, new HttpEntity<>(null), UserDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<UserDto> users = List.of(response.getBody());
        assertEquals(1, users.size());
        assertEquals(USER_DTO, users.get(0));
    }

    @Test
    public void shouldCreateUser() {
        // Given
        when(userService.saveUser(eq(USER_CREATION_DTO))).thenReturn(USER_DTO);

        // When
        var response =
                restTemplate.exchange("/users", HttpMethod.POST, new HttpEntity<>(USER_CREATION_DTO), UserDto.class);

        // Then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserDto userDto = response.getBody();
        assertEquals(USER_DTO, userDto);
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        Long id = 1l;
        when(userService.updateUser(eq(id), eq(USER_DTO))).thenReturn(USER_UPDATE_DTO);

        // When
        var response =
                restTemplate.exchange("/users/{id}", HttpMethod.POST, new HttpEntity<>(USER_DTO), UserDto.class, id);

        // Then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto responseUserDto = response.getBody();
        assertEquals(USER_UPDATE_DTO, responseUserDto);
    }
}