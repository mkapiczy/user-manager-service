package pl.mkapiczy.usermanagerservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;
import pl.mkapiczy.usermanagerservice.domain.entity.AddressEntity;
import pl.mkapiczy.usermanagerservice.domain.entity.UserEntity;
import pl.mkapiczy.usermanagerservice.mapper.UserMapper;
import pl.mkapiczy.usermanagerservice.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    public void shouldGetAllUsers() {
        // Given
        UserEntity userEntity = mock(UserEntity.class);
        UserDto userDto = mock(UserDto.class);
        when(userMapper.map(any(UserEntity.class))).thenReturn(userDto);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        // When
        List<UserDto> allUsers = userService.getAll();

        // Then
        assertEquals(1, allUsers.size());
        assertEquals(allUsers.get(0), userDto);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        UserEntity userEntity = mock(UserEntity.class);
        UserDto userDto = mock(UserDto.class);
        when(userMapper.map(any(UserEntity.class))).thenReturn(userDto);
        when(userMapper.map(any(UserDto.class))).thenReturn(userEntity);
        when(userRepository.save(eq(userEntity))).thenReturn(userEntity);

        // When
        UserDto savedUser = userService.saveUser(userDto);

        // Then
        assertEquals(userDto, savedUser);
        verify(userRepository).save(eq(userEntity));
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        Long id = 1l;
        UserEntity dbEntity = UserEntity.builder()
                .id(id)
                .name("name1")
                .email("email1@email.com")
                .password("password1")
                .address(AddressEntity.builder()
                                 .street("street1")
                                 .zipCode("zip1")
                                 .city("city1")
                                 .country("country1")
                                 .build())
                .build();
        UserDto userDto = UserDto.builder()
                .name("name2")
                .email("email2@email.com")
                .password("password2")
                .address(UserDto.Address.builder()
                                 .street("street2")
                                 .zipCode("zip2")
                                 .city("city2")
                                 .country("country2")
                                 .build())
                .build();
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(dbEntity));
        when(userMapper.map(any(UserEntity.class))).thenReturn(userDto);
        when(userRepository.save(any(UserEntity.class))).thenReturn(dbEntity);

        // When
        UserDto savedUser = userService.updateUser(id, userDto);

        // Then
        assertEquals(userDto, savedUser);
        ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(entityCaptor.capture());
        UserEntity capturedEntity = entityCaptor.getValue();
        assertEquals(userDto.getName(), capturedEntity.getName());
        assertEquals(userDto.getEmail(), capturedEntity.getEmail());
        assertEquals(userDto.getPassword(), capturedEntity.getPassword());
        assertEquals(id, capturedEntity.getId());
        assertEquals(userDto.getAddress().getCity(), capturedEntity.getAddress().getCity());
        assertEquals(userDto.getAddress().getCountry(), capturedEntity.getAddress().getCountry());
        assertEquals(userDto.getAddress().getStreet(), capturedEntity.getAddress().getStreet());
        assertEquals(userDto.getAddress().getZipCode(), capturedEntity.getAddress().getZipCode());

    }
}