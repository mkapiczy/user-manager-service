package pl.mkapiczy.usermanagerservice.mapper;

import org.springframework.stereotype.Component;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;
import pl.mkapiczy.usermanagerservice.domain.entity.AddressEntity;
import pl.mkapiczy.usermanagerservice.domain.entity.UserEntity;

@Component
public class UserMapper {

    public UserEntity map(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .address(AddressEntity.builder()
                                 .city(userDto.getAddress().getCity())
                                 .country(userDto.getAddress().getCountry())
                                 .street(userDto.getAddress().getStreet())
                                 .zipCode(userDto.getAddress().getZipCode())
                                 .build())
                .build();
    }

    public UserDto map(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .address(UserDto.Address.builder()
                                 .city(userEntity.getAddress().getCity())
                                 .country(userEntity.getAddress().getCountry())
                                 .street(userEntity.getAddress().getStreet())
                                 .zipCode(userEntity.getAddress().getZipCode())
                                 .build())
                .build();
    }
}
