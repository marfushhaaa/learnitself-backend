package ch.oberemok.marharyta.learnitself.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long>{
    List<LessonProgress> findByKeycloakId(String keycloakId);
    Optional<LessonProgress> findByKeycloakIdAndLessonId(String keycloakId, Long lessonId);
    long countByKeycloakIdAndLessonCourseIdAndStatus(String keycloakId, Long courseId, Status status);
    long countByLessonCourseId(Long courseId);
}
