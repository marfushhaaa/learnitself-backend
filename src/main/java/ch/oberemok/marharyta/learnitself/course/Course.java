package ch.oberemok.marharyta.learnitself.course;

import ch.oberemok.marharyta.learnitself.category.Category;
import ch.oberemok.marharyta.learnitself.user.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Automatically generated id")
    private long id;

    @Column(nullable = false, length = 40)
    @Size(min = 3, max = 40, message = "Course name must contain 3-40 characters")
    @NotEmpty
    @NotNull
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    @Positive(message = "Course length must be positive")
    private int courseLength;

    @Column(nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate creation_date = LocalDate.now();

    @Column(nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String creatorId;

    @Column(nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String creatorUsername = "unknown";

    @ManyToOne
    @JoinColumn(name = "fk_idCategory")
    private Category category;

}
