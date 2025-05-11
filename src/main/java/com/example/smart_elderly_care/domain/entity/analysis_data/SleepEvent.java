package com.example.smart_elderly_care.domain.entity.analysis_data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("SLEEP")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SleepEvent extends EventData {

    private LocalDateTime sleepStartTime;
    private LocalDateTime wakeUpTime;

    private Long sleepDurationMinutes;

    @PrePersist
    @PreUpdate
    private void calculateSleepDuration() {
        if (sleepStartTime != null && wakeUpTime != null) {
            this.sleepDurationMinutes = Duration.between(sleepStartTime, wakeUpTime).toMinutes();
        }
    }
}
