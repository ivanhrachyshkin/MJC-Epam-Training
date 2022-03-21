package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto extends RepresentationModel<TagDto> {

    private Integer id;
    private String name;
    private Boolean active;

    public TagDto(final Integer id) {
        this.id = id;
    }
}
