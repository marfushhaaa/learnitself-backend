package ch.oberemok.marharyta.learnitself.progress;

import ch.oberemok.marharyta.learnitself.lesson.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String keycloakId; // ← Keycloak User ID

    @ManyToOne
    @JoinColumn(name = "fk_idLesson")
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;
}
