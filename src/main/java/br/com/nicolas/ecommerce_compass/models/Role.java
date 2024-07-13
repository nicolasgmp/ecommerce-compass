package br.com.nicolas.ecommerce_compass.models;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        Long id;

        Values(Long id) {
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public static boolean exists(String name) {
            return Arrays.stream(Values.values())
                    .anyMatch(value -> value.name().equalsIgnoreCase(name));
        }
    }
}
