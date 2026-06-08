package ch.oberemok.marharyta.learnitself.progress;

import ch.oberemok.marharyta.learnitself.dataaccess.EntityNotFoundException;
import ch.oberemok.marharyta.learnitself.lesson.Lesson;
import ch.oberemok.marharyta.learnitself.lesson.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonProgressService {
    private final LessonProgressRepository progressRepository;
    private final LessonRepository lessonRepository;

    public LessonProgressService(LessonProgressRepository progressRepository, LessonRepository lessonRepository) {
        this.progressRepository = progressRepository;
        this.lessonRepository = lessonRepository;
    }

    // Change lesson status
    public LessonProgress updateStatus(Long lessonId, Status status, String keycloakId) {
        LessonProgress progress = progressRepository
                .findByKeycloakIdAndLessonId(keycloakId, lessonId)
                .orElseGet(() -> {
                    LessonProgress neu = new LessonProgress();
                    neu.setKeycloakId(keycloakId);
                    neu.setLesson(lessonRepository.findById(lessonId)
                            .orElseThrow(() -> new EntityNotFoundException(lessonId, Lesson.class)));
                    return neu;
                });

        progress.setStatus(status);
        return progressRepository.save(progress);
    }

    // Count Progress in %
    public double getCourseProgress(Long courseId, String keycloakId) {
        long total = progressRepository.countByLessonCourseId(courseId);
        if (total == 0) return 0.0;
        long done = progressRepository.countByKeycloakIdAndLessonCourseIdAndStatus(
                keycloakId, courseId, Status.DONE);
        return Math.round((done * 100.0) / total * 10.0) / 10.0; // for example 66.7
    }
}
