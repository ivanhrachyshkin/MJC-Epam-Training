package com.epam.esm.dao;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new Tag(rs.getInt("id"), rs.getString("name"));
    }
}
