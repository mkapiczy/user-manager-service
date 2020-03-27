package pl.mkapiczy.usermanagerservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Address address;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Address {
        private String street;
        private String zipCode;
        private String city;
        private String country;
    }
}
