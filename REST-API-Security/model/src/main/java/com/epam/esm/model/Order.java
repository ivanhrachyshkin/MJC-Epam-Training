package com.epam.esm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Audited
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime date;

    @ManyToMany
    @JoinTable(name = "order_gifts",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private Set<GiftCertificate> giftCertificates = new HashSet<>();

    public Order(final Integer id) {
        this.id = id;
    }
}
