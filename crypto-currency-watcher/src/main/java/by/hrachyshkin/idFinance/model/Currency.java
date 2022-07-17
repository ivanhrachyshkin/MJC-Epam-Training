package by.hrachyshkin.idFinance.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
@Data
public class Currency {

    @Id
    private Integer id;
    @Column(unique = true)
    private String symbol;
}
