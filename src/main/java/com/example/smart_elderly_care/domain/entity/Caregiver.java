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

    @ElementCollection
    @CollectionTable(name = "caregiver_elderly_ids", joinColumns = @JoinColumn(name = "caregiver_id"))
    @Column(name = "elderly_id")
    private List<String> elderlyIds;
}
