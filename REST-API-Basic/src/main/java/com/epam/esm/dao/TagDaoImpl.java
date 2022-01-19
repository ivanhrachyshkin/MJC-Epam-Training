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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TagRowMapper tagRowMapper;

    public TagDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate, final TagRowMapper tagRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Integer create(final Tag tag) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String CREATE_QUERY = "INSERT INTO tag (name) VALUES (:name)";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", tag.getName());
        namedParameterJdbcTemplate.update(CREATE_QUERY, namedParameters, keyHolder);
        return (Integer) keyHolder.getKeys().get("id");
    }

    @Override
    public List<Tag> readAll() {
        final String READ_QUERY = "SELECT id, name FROM tag";
        return namedParameterJdbcTemplate.query(READ_QUERY, tagRowMapper);
    }

    @Override
    public Tag readOne(final int id) {

        final String READ_ONE_QUERY = "SELECT id, name FROM tag WHERE id = :id";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        final List<Tag> tags =
                namedParameterJdbcTemplate.query(READ_ONE_QUERY, namedParameters, tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public Tag readOneByName(final String name) {
        final String READ_ONE_QUERY = "SELECT id, name FROM tag WHERE name = :name";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name);
        final List<Tag> tags =
                namedParameterJdbcTemplate.query(READ_ONE_QUERY, namedParameters, tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public void deleteById(final int id) {
        final String DELETE_QUERY = "DELETE FROM tag WHERE id = :id";
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
    }
}
