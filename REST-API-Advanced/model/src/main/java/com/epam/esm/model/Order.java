package com.epam.esm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "order", schema = "public")
public class Order implements Serializable {

    @EmbeddedId
    private OrderPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("giftCertificateId")
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    @Column
    private Float price;
    @Column
    private LocalDateTime date;

    public Order() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(giftCertificate, order.giftCertificate) && Objects.equals(price, order.price) && Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, giftCertificate, price, date);
    }
}
