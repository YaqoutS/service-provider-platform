package com.graduationproject.serviceproviderplatform.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDTO {
    private Long id;

    @NonNull
    private String name;

    private String description;

    private Image image;

    private Long companyId;
    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.image = category.getImage();
        this.companyId = category.getCompany() == null ? null: category.getCompany().getId();
    }
}
