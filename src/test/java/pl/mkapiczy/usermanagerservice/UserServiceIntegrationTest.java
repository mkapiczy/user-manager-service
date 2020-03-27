package pl.mkapiczy.usermanagerservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.mkapiczy.usermanagerservice.TestConstants.USER_CREATION_DTO;
import static pl.mkapiczy.usermanagerservice.TestConstants.USER_UPDATE_DTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCreateTwoUsersAndThenGetAllUsers() {
        var createUserResponse1 =
                restTemplate.exchange("/users", HttpMethod.POST, new HttpEntity<>(USER_CREATION_DTO), UserDto.class);
        assertThat(createUserResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var createUserResponse2 =
                restTemplate.exchange("/users", HttpMethod.POST, new HttpEntity<>(USER_CREATION_DTO), UserDto.class);
        assertThat(createUserResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var getUsersResponse = restTemplate.exchange("/users", HttpMethod.GET, new HttpEntity<>(null), UserDto[].class);
        assertThat(getUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<UserDto> users = List.of(getUsersResponse.getBody());
        assertEquals(2, users.size());
        assertEquals(USER_CREATION_DTO.getName(), users.get(0).getName());
        assertEquals(USER_CREATION_DTO.getName(), users.get(1).getName());
    }

    @Test
    public void shouldCreateUserAndThenUpdateThisUser() {
        var createUserResponse =
                restTemplate.exchange("/users", HttpMethod.POST, new HttpEntity<>(USER_CREATION_DTO),
                                      UserDto.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserDto createdUser = createUserResponse.getBody();

        var getUsersResponse =
                restTemplate.exchange("/users", HttpMethod.GET, new HttpEntity<>(null), UserDto[].class);
        assertThat(getUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<UserDto> users = List.of(getUsersResponse.getBody());
        Optional<UserDto> filteredUserOptional =
                users.stream().filter(u -> u.getId().equals(createdUser.getId())).findFirst();
        assertTrue(filteredUserOptional.isPresent());
        UserDto filteredUser = filteredUserOptional.get();
        assertEquals(createdUser.getId(), filteredUser.getId());
        assertEquals(createdUser.getName(), filteredUser.getName());
        assertEquals(createdUser.getPassword(), filteredUser.getPassword());
        assertEquals(createdUser.getEmail(), filteredUser.getEmail());
        assertEquals(createdUser.getAddress().getCountry(), filteredUser.getAddress().getCountry());
        assertEquals(createdUser.getAddress().getCity(), filteredUser.getAddress().getCity());
        assertEquals(createdUser.getAddress().getZipCode(), filteredUser.getAddress().getZipCode());
        assertEquals(createdUser.getAddress().getStreet(), filteredUser.getAddress().getStreet());

        var updateResponse = restTemplate
                .exchange("/users/{id}", HttpMethod.POST, new HttpEntity<>(USER_UPDATE_DTO), UserDto.class,
                          createdUser.getId());
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        var getUsersResponse2 =
                restTemplate.exchange("/users", HttpMethod.GET, new HttpEntity<>(null), UserDto[].class);
        assertThat(getUsersResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<UserDto> usersAfterUpdate = List.of(getUsersResponse2.getBody());
        Optional<UserDto> filteredUpdatedUserOptional =
                usersAfterUpdate.stream().filter(u -> u.getId().equals(createdUser.getId())).findFirst();
        assertTrue(filteredUpdatedUserOptional.isPresent());
        UserDto filteredUpdatedUser = filteredUpdatedUserOptional.get();
        assertEquals(createdUser.getId(), filteredUpdatedUser.getId());
        assertEquals(USER_UPDATE_DTO.getName(), filteredUpdatedUser.getName());
        assertEquals(USER_UPDATE_DTO.getEmail(), filteredUpdatedUser.getEmail());
        assertEquals(USER_UPDATE_DTO.getPassword(), filteredUpdatedUser.getPassword());
        assertEquals(USER_UPDATE_DTO.getAddress().getCountry(), filteredUpdatedUser.getAddress().getCountry());
        assertEquals(USER_UPDATE_DTO.getAddress().getCity(), filteredUpdatedUser.getAddress().getCity());
        assertEquals(USER_UPDATE_DTO.getAddress().getZipCode(), filteredUpdatedUser.getAddress().getZipCode());
        assertEquals(USER_UPDATE_DTO.getAddress().getStreet(), filteredUpdatedUser.getAddress().getStreet());

    }
}
