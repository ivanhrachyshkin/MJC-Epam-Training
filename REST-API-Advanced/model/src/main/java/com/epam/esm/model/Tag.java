package com.epam.esm.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> giftCertificates = new HashSet<>();

    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.giftCertificates = giftCertificates;
    }

    public Tag(Integer id, String name, Set<GiftCertificate> giftCertificates) {
        this.id = id;
        this.name = name;
        this.giftCertificates = giftCertificates;
    }

    public Tag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(giftCertificates, tag.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, giftCertificates);
    }
}
