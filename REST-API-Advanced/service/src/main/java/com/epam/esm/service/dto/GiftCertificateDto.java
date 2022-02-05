package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    private Integer id;
    private String name;
    private String description;
    private Float price;
    private Integer duration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdateDate;
    private Set<TagDto> tags;
}
