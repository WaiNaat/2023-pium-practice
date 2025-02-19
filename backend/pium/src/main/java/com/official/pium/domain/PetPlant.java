package com.official.pium.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.validation.annotation.Validated;

@Entity
@Getter
@Table(name = "pet_plant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetPlant extends BaseEntity {

    private static final int MIN_WATER_CYCLE = 1;
    private static final int MAX_WATER_CYCLE = 365;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_plant_id")
    private DictionaryPlant dictionaryPlant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @NotBlank
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotBlank
    @Column(name = "location", nullable = false)
    private String location;

    @NotBlank
    @Column(name = "flowerpot", nullable = false)
    private String flowerpot;

    @NotBlank
    @Column(name = "light", nullable = false)
    private String light;

    @NotBlank
    @Column(name = "wind", nullable = false)
    private String wind;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "next_water_date", nullable = false)
    private LocalDate nextWaterDate;

    @NotNull
    @Column(name = "last_water_date", nullable = false)
    private LocalDate lastWaterDate;

    @Min(MIN_WATER_CYCLE)
    @Max(MAX_WATER_CYCLE)
    @NotNull
    @Column(name = "water_cycle", nullable = false)
    private Integer waterCycle;

    @Builder
    private PetPlant(DictionaryPlant dictionaryPlant, Member member, String nickname, String imageUrl, String location, String flowerpot, String light, String wind, @NotNull LocalDate birthDate, @NotNull LocalDate nextWaterDate, @NotNull LocalDate lastWaterDate, @NotNull Integer waterCycle) {
        this.dictionaryPlant = dictionaryPlant;
        this.member = member;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.location = location;
        this.flowerpot = flowerpot;
        this.light = light;
        this.wind = wind;
        this.birthDate = birthDate;
        this.nextWaterDate = nextWaterDate;
        this.lastWaterDate = lastWaterDate;
        this.waterCycle = waterCycle;
    }

    public Long calculateDaySince(LocalDate currentDate) {
        if (currentDate.isBefore(birthDate)) {
            throw new IllegalArgumentException("함께한 날은 음수가 될 수 없습니다. Date: " + currentDate);
        }
        return ChronoUnit.DAYS.between(birthDate, currentDate) + 1;
    }

    /**
     * - 0 : 오늘 할 일
     * - 음수 : 할 일
     * - 양수 : 지각
     */
    public Long calculateDDay(LocalDate currentDate) {
        return ChronoUnit.DAYS.between(nextWaterDate, currentDate);
    }

    public void updatePetPlant(
            String nickname, String location, String flowerpot, String light,
            String wind, Integer waterCycle, LocalDate birthDate, LocalDate lastWaterDate
    ) {
        validateEmptyValue(nickname);
        validateEmptyValue(location);
        validateEmptyValue(flowerpot);
        validateEmptyValue(light);
        validateEmptyValue(wind);
        validateWaterCycle(waterCycle);
        validateLocalDate(birthDate);
        validateLocalDate(lastWaterDate);
        this.nickname = nickname;
        this.location = location;
        this.flowerpot = flowerpot;
        this.light = light;
        this.wind = wind;
        this.waterCycle = waterCycle;
        this.birthDate = birthDate;
        this.lastWaterDate = lastWaterDate;
    }

    private void validateEmptyValue(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("반려 식물 속성에는 빈 값 들어올 수 없습니다. value: " + value);
        }
    }

    private void validateWaterCycle(Integer waterCycle) {
        if (waterCycle < MIN_WATER_CYCLE || waterCycle > MAX_WATER_CYCLE) {
            throw new IllegalArgumentException("물주기 주기는 1이상 365이하의 값만 가능합니다. waterCycle: " + waterCycle);
        }
    }

    private void validateLocalDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("반려 식물 날짜 속성은 빈 값이 될 수 없습니다. date: null");
        }
    }
}
