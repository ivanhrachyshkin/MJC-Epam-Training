package com.epam.esm.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateTagDaoImpl implements GiftCertificateTagDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GiftCertificateTagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void createGiftCertificateTag(final int giftCertificateId, final int tagId) {
        final String CREATE_QUERY = "INSERT" +
                " INTO gift_certificate_tag (gift_certificate_id, tag_id)" +
                " VALUES (:gift_certificate_id, :tag_id)" +
                " ON CONFLICT DO NOTHING";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("gift_certificate_id", giftCertificateId)
                .addValue("tag_id", tagId);
        namedParameterJdbcTemplate.update(CREATE_QUERY, namedParameters);
    }

    @Override
    public void deleteGiftCertificateTagByCertificateId(final int giftCertificateId) {
        final String DELETE_QUERY = "DELETE" +
                " FROM gift_certificate_tag" +
                " WHERE gift_certificate_id = :gift_certificate_id";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("gift_certificate_id", giftCertificateId);
        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }

    @Override
    public void deleteGiftCertificateTagByTagId(final int tagId) {
        final String DELETE_QUERY = "DELETE" +
                " FROM gift_certificate_tag" +
                " WHERE tag_id = :tag_id" +
                " ON CONFLICT DO NOTHIG";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("tag_id", tagId);
        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }
}
