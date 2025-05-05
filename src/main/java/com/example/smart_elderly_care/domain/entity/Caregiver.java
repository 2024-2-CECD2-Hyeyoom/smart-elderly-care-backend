package com.example.smart_elderly_care.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("CAREGIVER")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Caregiver extends Member {

    @ManyToMany
    @JoinTable(
            name = "caregiver_user",
            joinColumns = @JoinColumn(name = "caregiver_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> elderlyList;
}
