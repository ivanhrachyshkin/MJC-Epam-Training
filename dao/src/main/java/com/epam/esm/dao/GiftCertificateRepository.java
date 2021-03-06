package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCertificateRepository
        extends JpaRepository<GiftCertificate, Integer>, JpaSpecificationExecutor<GiftCertificate> {

    Optional<GiftCertificate> findByName(String name);

    Optional<GiftCertificate> findByIdAndActive(Integer id, Boolean active);
}
