package com.neighboursnack.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 320, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @JdbcTypeCode(SqlTypes.JSON)
    private UserData userData;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(length = 150)
    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 10)
    private String phoneNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Address> addresses;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserData implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private UUID secretKey;

        private boolean secretKeyStatus = false;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Address implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private boolean isDefault;

        private String landmark;
        private String deliveryInstructions;
    }

}
