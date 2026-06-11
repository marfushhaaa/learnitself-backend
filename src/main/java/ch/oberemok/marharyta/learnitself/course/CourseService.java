package ch.oberemok.marharyta.learnitself.course;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.category.Category;
import ch.oberemok.marharyta.learnitself.category.CategoryRepository;
import ch.oberemok.marharyta.learnitself.dataaccess.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repository;
    private final CategoryRepository categoryRepository;

    public CourseService (CourseRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public List<Course> getCourses() {
        return repository.findByOrderByNameAsc();
    }

    public Course getCourse(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Course.class));
    }

    public Course createCourse(Course course) {
        Category category = categoryRepository.findById(course.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException(course.getId(), Category.class));
        course.setCategory(category);

        return repository.save(course);
    }

    public Course updateCourse(Course course, Long id) {
        Category category = categoryRepository.findById(course.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException(course.getCategory().getId(), Category.class));

        return repository.findById(id)
                .map(courseOrig -> {
                    courseOrig.setCategory(category);
                    courseOrig.setName(course.getName());
                    courseOrig.setCourseLength(course.getCourseLength());
                    courseOrig.setDescription(course.getDescription());
                    return repository.save(courseOrig);
                })
                .orElseThrow(() -> new EntityNotFoundException(id, Course.class));
    }

    public MessageResponse deleteCourse(Long id) {
        repository.deleteById(id);
        return new MessageResponse("Course " + id + " is successfully deleted");
    }
}
