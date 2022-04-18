package com.epam.esm.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Entity
@Audited
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @Enumerated(EnumType.STRING)
    private Roles roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public enum Roles {
        ROLE_USER, ROLE_ADMIN
    }
}
