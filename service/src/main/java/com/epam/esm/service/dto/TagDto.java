package com.epam.esm.service.dto;

import com.epam.esm.service.Trimmable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDto extends RepresentationModel<TagDto> implements Trimmable {

    private Integer id;
    private String name;
    private Boolean active;

    public TagDto(Integer id) {
        this.id = id;
    }
}
