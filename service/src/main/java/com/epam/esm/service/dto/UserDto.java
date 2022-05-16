package com.epam.esm.service.dto;

import com.epam.esm.service.Trimmable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends RepresentationModel<UserDto> implements Trimmable {

    private Integer id;
    private String username;
    private String firstName;
    private String email;
    private String password;
    private String address;
    private Set<OrderDto> dtoOrders;
    private List<RoleDto> dtoRoles;

    public UserDto(final Integer id) {
        this.id = id;
    }
}
