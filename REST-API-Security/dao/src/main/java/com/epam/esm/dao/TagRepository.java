package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    String READ_ONE_MOST_USED =
           " with my_table as" +
            " (SELECT tags.id, tags.name, tags.active, COUNT(*) as count" +
                    " FROM (select user_id, sum(price)" +
                    " from orders" +
                    " group by user_id" +
                    " having sum(price) >= ALL(select sum(price) from orders group by user_id)" +
                    "     ) as maxcost" +
                    " JOIN orders ON orders.user_id = maxcost.user_id" +
                    " JOIN order_gifts ON order_gifts.order_id = orders.id" +
                    " JOIN gift_certificate_tags" +
                    " ON gift_certificate_tags.gift_certificate_id = order_gifts.gift_certificate_id" +
                    " JOIN tags ON tags.id = gift_certificate_tags.tag_id" +
                    " GROUP BY tags.id, tags.name)" +
                    "" +
                    " select id, name, active, count" +
                    " from my_table" +
                    " where count >= ALL (select count from my_table)";


    Page<Tag> findAllByActive(Boolean active, Pageable pageable);

    Optional<Tag> findByIdAndActive(int id, Boolean active);

    Optional<Tag> findByName(String name);

    @Query(value = READ_ONE_MOST_USED, nativeQuery = true)
    Page<Tag> readMostUsed(Pageable pageable);
}
