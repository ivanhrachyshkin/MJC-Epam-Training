package com.epam.esm.dao;

import com.epam.esm.dao.extractor.TagRowMapper;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagDaoImpl implements TagDao {

    private static final String CREATE_QUERY = "INSERT INTO tag (name) VALUES (:name)";
    private static final String READ_QUERY = "SELECT id, name FROM tag";
    private static final String READ_ONE_QUERY = "SELECT id, name FROM tag WHERE id = :id";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT id, name FROM tag WHERE name = :name";
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TagRowMapper tagRowMapper;

    public TagDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate, final TagRowMapper tagRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Tag create(final Tag tag) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", tag.getName());
        namedParameterJdbcTemplate.update(CREATE_QUERY, namedParameters, keyHolder);
        tag.setId((Integer) keyHolder.getKeys().get("id"));
        return tag;
    }

    @Override
    public List<Tag> readAll() {
        return namedParameterJdbcTemplate.query(READ_QUERY, tagRowMapper);
    }

    @Override
    public Tag readOne(final int id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        final List<Tag> tags =
                namedParameterJdbcTemplate.query(READ_ONE_QUERY, namedParameters, tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public Tag readOneByName(final String name) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name);
        final List<Tag> tags =
                namedParameterJdbcTemplate.query(READ_ONE_BY_NAME_QUERY, namedParameters, tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public void deleteById(final int id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }
}
