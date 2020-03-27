package pl.mkapiczy.usermanagerservice;

import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;

public class TestConstants {

    public static final UserDto USER_DTO = UserDto.builder()
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

    public static final UserDto USER_CREATION_DTO = UserDto.builder()
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

    public static final UserDto USER_UPDATE_DTO = UserDto.builder()
            .name("updatedName")
            .email("updatedemail@email.com")
            .password("updatedpassword")
            .address(UserDto.Address.builder()
                             .street("updatedstreet")
                             .zipCode("updatedzip")
                             .city("updatedcity")
                             .country("updatedcountry")
                             .build())
            .build();
}
