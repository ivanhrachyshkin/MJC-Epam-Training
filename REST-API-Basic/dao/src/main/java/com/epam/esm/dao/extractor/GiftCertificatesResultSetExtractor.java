package com.epam.esm.dao.extractor;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class GiftCertificatesResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(final ResultSet resultSet) throws DataAccessException, SQLException {

        final Set<GiftCertificate> rawGiftCertificates = new LinkedHashSet<>();
        final Map<Integer, Set<Tag>> giftCertificateIdToTags = new HashMap<>();

        while (resultSet.next()) {

            final GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(resultSet.getInt("id"));
            giftCertificate.setName(resultSet.getString("name"));
            giftCertificate.setDescription(resultSet.getString("description"));
            giftCertificate.setPrice(resultSet.getFloat("price"));
            giftCertificate.setDuration(resultSet.getInt("duration"));
            giftCertificate.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
            giftCertificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime());
            rawGiftCertificates.add(giftCertificate);
            final Tag tag = new Tag(resultSet.getInt("tag_id"), resultSet.getString("tag_name"));

            if (!giftCertificateIdToTags.containsKey(giftCertificate.getId())) {
                giftCertificateIdToTags.put(giftCertificate.getId(), new HashSet<>());
            }
            final Set<Tag> tags = giftCertificateIdToTags.get(giftCertificate.getId());
            tags.add(tag);
        }

        final List<GiftCertificate> giftCertificates = new ArrayList<>();
        for (GiftCertificate giftCertificate : rawGiftCertificates) {
            final Set<Tag> tags = giftCertificateIdToTags.get(giftCertificate.getId());
            giftCertificate.setTags(tags);
            giftCertificates.add(giftCertificate);
        }

        return giftCertificates;
    }
}
