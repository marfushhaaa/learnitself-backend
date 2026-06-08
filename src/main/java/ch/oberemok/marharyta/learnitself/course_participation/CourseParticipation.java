package ch.oberemok.marharyta.learnitself.course_participation;

import ch.oberemok.marharyta.learnitself.course.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class CourseParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String keycloakId; // Keycloak User ID

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_idCourse")
    private Course course;

    @Column(nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate joinedAt = LocalDate.now();
}
