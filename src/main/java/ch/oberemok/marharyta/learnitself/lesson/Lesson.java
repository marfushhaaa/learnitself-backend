package ch.oberemok.marharyta.learnitself.lesson;

import ch.oberemok.marharyta.learnitself.course.Course;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 40)
    @NotEmpty
    @NotNull
    @Size(min = 3, max = 40, message = "Lesson name must contain 3-40 characters")
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "fk_idCourse")
    @JsonBackReference
    private Course course;
}
