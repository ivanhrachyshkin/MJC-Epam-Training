package com.epam.esm.service.dto;

import com.epam.esm.service.Trimmable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDto extends RepresentationModel<TagDto> implements Trimmable {

    private Integer id;
    private String name;
    private Boolean active;

    public TagDto(final Integer id) {
        this.id = id;
    }

    public TagDto(Integer id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
