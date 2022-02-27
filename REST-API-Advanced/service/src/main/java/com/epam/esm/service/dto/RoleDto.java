package com.epam.esm.service.dto;

import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto extends RepresentationModel<RoleDto> {

    private Integer id;
    private String name;
    private List<User> users;

    public RoleDto(String name) {
        this.name = name;
    }
}
