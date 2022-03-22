package com.epam.esm.service.dto;

import com.epam.esm.service.Trimmable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto> implements Trimmable {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Set<OrderDto> dtoOrders;
    private List<RoleDto> dtoRoles;

    public UserDto(final Integer id) {
        this.id = id;
    }

    public UserDto(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
