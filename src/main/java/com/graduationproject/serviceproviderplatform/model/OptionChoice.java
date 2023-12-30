package com.graduationproject.serviceproviderplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OptionChoice {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    private ServiceOption option;

    @NonNull
    private boolean choice;

    @NonNull
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Request request;
}
