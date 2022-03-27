package com.epam.esm.service.dto;

import com.epam.esm.service.Trimmable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> implements Trimmable {

    private Integer id;
    private String name;
    private String description;
    private Float price;
    private Integer duration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdateDate;
    private Boolean active;
    private Set<TagDto> dtoTags;

    public GiftCertificateDto(final Integer id) {
        this.id = id;
    }

    public GiftCertificateDto(Integer id,
                              String name,
                              String description,
                              Float price,
                              Integer duration,
                              Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.active = active;
    }

    public GiftCertificateDto(String name,
                              String description,
                              Float price,
                              Integer duration,
                              Boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.active = active;
    }

    public GiftCertificateDto(Integer id,
                              String name,
                              String description,
                              Float price, Integer duration,
                              Boolean active,
                              Set<TagDto> dtoTags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.active = active;
        this.dtoTags = dtoTags;
    }

    public GiftCertificateDto(String name,
                              String description,
                              Float price, Integer duration,
                              Set<TagDto> dtoTags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.dtoTags = dtoTags;
    }
}
