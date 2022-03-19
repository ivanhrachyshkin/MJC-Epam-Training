package com.epam.esm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Audited
@NoArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "boolean default true")
    private Boolean active;
    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> giftCertificates = new HashSet<>();

    public Tag(final Integer id) {
        this.id = id;
    }
}
