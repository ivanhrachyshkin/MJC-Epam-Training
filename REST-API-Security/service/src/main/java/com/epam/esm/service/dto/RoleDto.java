package com.epam.esm.service.dto;

import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class RoleDto extends RepresentationModel<RoleDto> {

    private Integer id;
    private Roles roleName;
    private List<User> users;

    public enum Roles {
        ROLE_USER, ROLE_ADMIN;
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
