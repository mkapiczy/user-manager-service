package pl.mkapiczy.usermanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mkapiczy.usermanagerservice.domain.dto.UserDto;
import pl.mkapiczy.usermanagerservice.domain.entity.AddressEntity;
import pl.mkapiczy.usermanagerservice.domain.entity.UserEntity;
import pl.mkapiczy.usermanagerservice.mapper.UserMapper;
import pl.mkapiczy.usermanagerservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(u -> userMapper.map(u)).collect(Collectors.toList());
    }

    public UserDto saveUser(UserDto userDto) {
        return userMapper.map(userRepository.save(userMapper.map(userDto)));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        userEntity.setName(userDto.getName());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setAddress(AddressEntity.builder()
                                      .street(userDto.getAddress().getStreet())
                                      .city(userDto.getAddress().getCity())
                                      .country(userDto.getAddress().getCountry())
                                      .zipCode(userDto.getAddress().getZipCode()).build());
        return userMapper.map(userRepository.save(userEntity));
    }
}
