package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String imageName;

    @Lob
    @Column(length = 100000)
    private byte[] imageData;

}

