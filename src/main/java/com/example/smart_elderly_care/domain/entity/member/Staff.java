package com.example.smart_elderly_care.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("STAFF")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Staff extends Member {
    @ManyToOne
    @JoinColumn(name = "welfare_center_id")
    private WelfareCenter welfareCenter;
}
