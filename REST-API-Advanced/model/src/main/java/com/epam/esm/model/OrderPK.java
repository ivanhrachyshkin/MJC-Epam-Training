package com.epam.esm.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderPK implements Serializable {

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "gift_certificate_id")
    private Integer giftCertificateId;

    public OrderPK() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGiftCertificateId() {
        return giftCertificateId;
    }

    public void setGiftCertificateId(Integer giftCertificateId) {
        this.giftCertificateId = giftCertificateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPK orderPK = (OrderPK) o;
        return Objects.equals(userId, orderPK.userId) && Objects.equals(giftCertificateId, orderPK.giftCertificateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, giftCertificateId);
    }
}
