package com.example.smart_elderly_care.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("STAFF")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Staff extends Member {
    @Column
    private Long welfareCenterId;

    @OneToMany
    @JoinColumn(name = "welfareCenterId", referencedColumnName = "welfareCenterId")
    private List<User> managedUsers;
}
