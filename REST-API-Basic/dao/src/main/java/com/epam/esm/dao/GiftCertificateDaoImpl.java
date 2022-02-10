package com.epam.esm.dao;


import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private static final String CREATE_QUERY = "INSERT INTO" +
            " gift_certificate (name, description, price, duration, create_date, last_update_date)" +
            " VALUES (:name, :description, :price, :duration, :create_date, :last_update_date)";

    private static final String READ_QUERY = "SELECT" +
            " gift_certificate.id," +
            " gift_certificate.name," +
            " gift_certificate.description," +
            " gift_certificate.price," +
            " gift_certificate.duration," +
            " gift_certificate.create_date," +
            " gift_certificate.last_update_date," +
            " tag.id as tag_id," +
            " tag.name as tag_name" +
            " FROM gift_certificate" +
            " JOIN gift_certificate_tag ON gift_certificate_tag.gift_certificate_id = gift_certificate.id" +
            " JOIN tag ON gift_certificate_tag.tag_id = tag.id";

    private static final String READ_ONE_QUERY = "SELECT" +
            " gift_certificate.id," +
            " gift_certificate.name," +
            " gift_certificate.description," +
            " gift_certificate.price," +
            " gift_certificate.duration," +
            " gift_certificate.create_date," +
            " gift_certificate.last_update_date," +
            " tag.id as tag_id," +
            " tag.name as tag_name" +
            " FROM gift_certificate" +
            " JOIN gift_certificate_tag ON gift_certificate_tag.gift_certificate_id = gift_certificate.id" +
            " JOIN tag ON gift_certificate_tag.tag_id = tag.id" +
            " WHERE gift_certificate.id = :id";

    private static final String READ_ONE_BY_NAME_QUERY = "SELECT" +
            " gift_certificate.id," +
            " gift_certificate.name," +
            " gift_certificate.description," +
            " gift_certificate.price," +
            " gift_certificate.duration," +
            " gift_certificate.create_date," +
            " gift_certificate.last_update_date," +
            " tag.id as tag_id," +
            " tag.name as tag_name" +
            " FROM gift_certificate" +
            " JOIN gift_certificate_tag ON gift_certificate_tag.gift_certificate_id = gift_certificate.id" +
            " JOIN tag ON gift_certificate_tag.tag_id = tag.id" +
            " WHERE gift_certificate.name = :name";

    private static final String UPDATE_QUERY = "UPDATE gift_certificate SET %s WHERE id = :id";
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final GiftCertificatesResultSetExtractor giftCertificatesResultSetExtractor;
    private final Clock clock;

    public GiftCertificateDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  final GiftCertificatesResultSetExtractor giftCertificatesResultSetExtractor,
                                  final Clock clock) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.giftCertificatesResultSetExtractor = giftCertificatesResultSetExtractor;
        this.clock = clock;
    }

    @Override
    public GiftCertificate create(final GiftCertificate giftCertificate) {
        final LocalDateTime createDate = LocalDateTime.now(clock);
        final LocalDateTime lastUpdateDate = LocalDateTime.now(clock);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", giftCertificate.getName())
                .addValue("description", giftCertificate.getDescription())
                .addValue("price", giftCertificate.getPrice())
                .addValue("duration", giftCertificate.getDuration())
                .addValue("create_date", Timestamp.valueOf(createDate))
                .addValue("last_update_date", Timestamp.valueOf(lastUpdateDate));
        namedParameterJdbcTemplate.update(CREATE_QUERY, namedParameters, keyHolder);
        giftCertificate.setId(((Number) Objects.requireNonNull(keyHolder.getKeys()).get("id")).intValue());
        giftCertificate.setCreateDate(createDate);
        giftCertificate.setLastUpdateDate(lastUpdateDate);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> readAll(final String tag,
                                         final String name,
                                         final String description,
                                         final String dateSort,
                                         final String nameSort) {
        final Map<String, Object> columnToValue = new HashMap<>();
        final Set<String> whereCriteria = new HashSet<>();
        if (tag != null) {
            columnToValue.put("tag", tag);
            whereCriteria.add("gift_certificate.id IN (" +
                    " SELECT gift_certificate_tag.gift_certificate_id" +
                    " FROM gift_certificate_tag" +
                    " JOIN tag ON gift_certificate_tag.tag_id = tag.id" +
                    " WHERE tag.name = :tag)");
        }
        if (name != null) {
            columnToValue.put("name", "%" + name + "%");
            whereCriteria.add("gift_certificate.name LIKE :name");
        }
        if (description != null) {
            columnToValue.put("description", "%" + description + "%");
            whereCriteria.add("gift_certificate.description LIKE :description");
        }
        String where = "";
        if (!whereCriteria.isEmpty()) {
            where = " WHERE " + String.join(" AND ", whereCriteria);
        }


        final Set<String> sortCriteria = new HashSet<>();
        if (dateSort != null) {
            sortCriteria.add(" create_date " + dateSort.toUpperCase());
        }
        if (nameSort != null) {
            sortCriteria.add(" name " + nameSort.toUpperCase());
        }
        String sort = "";
        if (!sortCriteria.isEmpty()) {
            sort = " ORDER BY " + String.join(", ", sortCriteria);
        }

        final MapSqlParameterSource namedParameters = new MapSqlParameterSource(columnToValue);
        final String query = READ_QUERY + where + sort;

        return namedParameterJdbcTemplate.query(query, namedParameters, giftCertificatesResultSetExtractor);
    }

    @Override
    public Optional<GiftCertificate> readOne(final int id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        final List<GiftCertificate> giftCertificates = namedParameterJdbcTemplate
                .query(READ_ONE_QUERY, namedParameters, giftCertificatesResultSetExtractor);
        return giftCertificates.isEmpty() ? Optional.empty() : Optional.of(giftCertificates.get(0));
    }

    @Override
    public Optional<GiftCertificate> readOneByName(final String name) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name);
        final List<GiftCertificate> giftCertificates = namedParameterJdbcTemplate
                .query(READ_ONE_BY_NAME_QUERY, namedParameters, giftCertificatesResultSetExtractor);
        return giftCertificates.isEmpty() ? Optional.empty() : Optional.of(giftCertificates.get(0));
    }

    @Override
    public void update(final GiftCertificate giftCertificate) {
        final Map<String, Object> columnToPlaceholder = new HashMap<>();
        final Map<String, Object> columnToValue = new HashMap<>();

        columnToValue.put("id", giftCertificate.getId());

        if (giftCertificate.getName() != null) {
            columnToPlaceholder.put("name", ":name");
            columnToValue.put("name", giftCertificate.getName());
        }

        if (giftCertificate.getDescription() != null) {
            columnToPlaceholder.put("description", ":description");
            columnToValue.put("description", giftCertificate.getDescription());
        }

        if (giftCertificate.getPrice() != null) {
            columnToPlaceholder.put("price", ":price");
            columnToValue.put("price", giftCertificate.getPrice());
        }

        if (giftCertificate.getDuration() != null) {
            columnToPlaceholder.put("duration", ":duration");
            columnToValue.put("duration", giftCertificate.getDuration());
        }

        columnToPlaceholder.put("last_update_date", ":last_update_date");
        columnToValue.put("last_update_date", Timestamp.valueOf(LocalDateTime.now(clock)));

        final MapSqlParameterSource namedParameters = new MapSqlParameterSource(columnToValue);
        final String query = String.format(UPDATE_QUERY, columnToPlaceholder
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ")));

        namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public void deleteById(final int id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }
}