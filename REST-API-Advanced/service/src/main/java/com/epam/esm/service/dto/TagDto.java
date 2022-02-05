package com.epam.esm.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {

    private Integer id;
    private String name;
}
