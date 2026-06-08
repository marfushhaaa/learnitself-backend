package ch.oberemok.marharyta.learnitself.course_participation;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.course.Course;
import ch.oberemok.marharyta.learnitself.course.CourseRepository;
import ch.oberemok.marharyta.learnitself.dataaccess.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseParticipationService {
    private final CourseParticipationRepository participationRepository;
    private final CourseRepository courseRepository;

    public CourseParticipationService(CourseParticipationRepository participationRepository, CourseRepository courseRepository) {
        this.participationRepository = participationRepository;
        this.courseRepository = courseRepository;
    }
    public List<Course> getMyParticipatedCourses(String keycloakId) {
        return participationRepository.findByKeycloakId(keycloakId)
                .stream()
                .map(CourseParticipation::getCourse) // get only Courses
                .toList();
    }

    public MessageResponse participate(Long courseId, String keycloakId) {
        // Is already participating?
        if (participationRepository.existsByKeycloakIdAndCourseId(keycloakId, courseId)) {
            return new MessageResponse("Already participate in course " + courseId);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(courseId, Course.class));

        CourseParticipation participation = new CourseParticipation();
        participation.setKeycloakId(keycloakId);
        participation.setCourse(course);
        participationRepository.save(participation);

        return new MessageResponse("Successfully participate in course " + courseId + ", " + keycloakId);
    }

    public MessageResponse leaveCourse(Long courseId, String keycloakId) {
        CourseParticipation participation = participationRepository
                .findByKeycloakIdAndCourseId(keycloakId, courseId)
                .orElseThrow(() -> new EntityNotFoundException(courseId, Course.class));

        participationRepository.delete(participation);
        return new MessageResponse("Successfully left course " + courseId);
    }

    public boolean isParticipating(Long courseId, String keycloakId) {
        return participationRepository.existsByKeycloakIdAndCourseId(keycloakId, courseId);
    }
}
