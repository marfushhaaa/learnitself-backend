package ch.oberemok.marharyta.learnitself.course_participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseParticipationRepository extends JpaRepository<CourseParticipation, Long> {
    Optional<CourseParticipation> findByKeycloakIdAndCourseId(String keycloakId, Long courseId);
    List<CourseParticipation> findByKeycloakId(String keycloakId);
    boolean existsByKeycloakIdAndCourseId(String keycloakId, Long courseId);
}
