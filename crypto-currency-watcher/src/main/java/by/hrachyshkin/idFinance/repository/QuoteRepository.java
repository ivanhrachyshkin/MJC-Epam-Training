package by.hrachyshkin.idFinance.repository;

import by.hrachyshkin.idFinance.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    @Query(value = "select * from quotes" +
            " where date = (select max(date)" +
            " from (select * from quotes where currency_id = ?1) as foo)",
            nativeQuery = true)
    Optional<Quote> findTopQuoteByCurrencyId(final int id);

    Optional<Quote> findById(final long id);
}
