package pl.mkapiczy.usermanagerservice.mapper;

import org.junit.jupiter.api.Test;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;
import pl.mkapiczy.usermanagerservice.domain.entity.AddressEntity;
import pl.mkapiczy.usermanagerservice.domain.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    public void shouldMapDtoToEntity() {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1l)
                .name("name")
                .email("email@email.com")
                .password("password")
                .address(UserDto.Address.builder()
                                 .street("street")
                                 .zipCode("zip")
                                 .city("city")
                                 .country("country")
                                 .build())
                .build();

        // When
        UserEntity mappedEntity = new UserMapper().map(userDto);

        // Then
        assertEquals(userDto.getId(), mappedEntity.getId());
        assertEquals(userDto.getName(), mappedEntity.getName());
        assertEquals(userDto.getEmail(), mappedEntity.getEmail());
        assertEquals(userDto.getPassword(), mappedEntity.getPassword());
        assertEquals(userDto.getAddress().getCity(), mappedEntity.getAddress().getCity());
        assertEquals(userDto.getAddress().getCountry(), mappedEntity.getAddress().getCountry());
        assertEquals(userDto.getAddress().getZipCode(), mappedEntity.getAddress().getZipCode());
        assertEquals(userDto.getAddress().getStreet(), mappedEntity.getAddress().getStreet());
    }

    @Test
    public void shouldMapEntityToDto() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1l)
                .name("name")
                .email("email@email.com")
                .password("password")
                .address(AddressEntity.builder()
                                 .street("street")
                                 .zipCode("zip")
                                 .city("city")
                                 .country("country")
                                 .build())
                .build();

        // When
        UserDto mappedDto = new UserMapper().map(userEntity);

        // Then
        assertEquals(userEntity.getId(), mappedDto.getId());
        assertEquals(userEntity.getName(), mappedDto.getName());
        assertEquals(userEntity.getEmail(), mappedDto.getEmail());
        assertEquals(userEntity.getPassword(), mappedDto.getPassword());
        assertEquals(userEntity.getAddress().getCity(), mappedDto.getAddress().getCity());
        assertEquals(userEntity.getAddress().getCountry(), mappedDto.getAddress().getCountry());
        assertEquals(userEntity.getAddress().getZipCode(), mappedDto.getAddress().getZipCode());
        assertEquals(userEntity.getAddress().getStreet(), mappedDto.getAddress().getStreet());
    }

}