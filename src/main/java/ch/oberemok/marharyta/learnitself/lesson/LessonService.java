package ch.oberemok.marharyta.learnitself.lesson;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.course.Course;
import ch.oberemok.marharyta.learnitself.course.CourseRepository;
import ch.oberemok.marharyta.learnitself.dataaccess.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    public Lesson getLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Lesson.class));
    }

    public Lesson createLesson(Lesson lesson, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(courseId, Course.class));
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Lesson lesson, Long id) {
        return lessonRepository.findById(id)
                .map(orig -> {
                    orig.setName(lesson.getName());
                    orig.setContent(lesson.getContent());
                    return lessonRepository.save(orig);
                })
                .orElseThrow(() -> new EntityNotFoundException(id, Lesson.class));
    }

    public MessageResponse deleteLesson(Long id) {
        lessonRepository.deleteById(id);
        return new MessageResponse("Lesson " + id + " successfully deleted");
    }
}
