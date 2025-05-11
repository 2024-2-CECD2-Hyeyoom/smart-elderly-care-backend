package com.example.smart_elderly_care.domain.entity.analysis_data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("OUTING")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OutingEvent extends EventData {

    private LocalDateTime outingStartTime;
    private LocalDateTime outingEndTime;

    private Long outingDurationMinutes;

    @PrePersist
    @PreUpdate
    private void calculateOutingDuration() {
        if (outingStartTime != null && outingEndTime != null) {
            this.outingDurationMinutes = Duration.between(outingStartTime, outingEndTime).toMinutes();
        }
    }
}
