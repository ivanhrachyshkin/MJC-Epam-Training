package com.epam.esm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gift_certificate_id", unique = true)
    private GiftCertificate giftCertificate;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false)
    private LocalDateTime date;

    public Order(final Integer id) {
        this.id = id;
    }
}
