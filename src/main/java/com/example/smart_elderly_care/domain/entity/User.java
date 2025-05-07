package com.example.smart_elderly_care.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("USER")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends Member {

    @Column(unique = true)
    private String elderlyId; // 노인 고유 코드

    @Column
    private LocalDate birthDate;

    @Column
    private Long welfareCenterId;

    @ElementCollection
    @CollectionTable(name = "member_underlying_diseases", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "underlying_disease")
    private List<String> underlyingDiseases;

    // 유저 상태 추가
    @Column
    private int careStatus;
}
