package ch.oberemok.marharyta.learnitself.course;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.category.Category;
import ch.oberemok.marharyta.learnitself.category.CategoryRepository;
import ch.oberemok.marharyta.learnitself.progress.LessonProgressService;
import ch.oberemok.marharyta.learnitself.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "Courses", description = "Course management operations")
public class CourseController {
    private final CourseService courseService;
    private final CategoryRepository categoryRepository;
    private final LessonProgressService lessonProgressService;

    public CourseController(CourseService courseService, CategoryRepository categoryRepository, LessonProgressService lessonProgressService) {
        this.courseService = courseService;
        this.categoryRepository = categoryRepository;
        this.lessonProgressService = lessonProgressService;
    }

    @PostMapping("api/courses")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Create a course", description = "Creates a new course")
    @ApiResponse(responseCode = "201", description = "Course successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Course> createCourse(
            @Parameter(description = "Course object to be created", required = true)
            @Valid @RequestBody Course course, Authentication authentication)  {

        // Takes the username from keycloak from field "preferred_username"
        Jwt jwt = (Jwt) authentication.getPrincipal();
        course.setCreatorId(jwt.getSubject());
        course.setCreatorUsername(jwt.getClaimAsString("preferred_username"));

        Course newCourse = courseService.createCourse(course);
        return new ResponseEntity<>(newCourse,HttpStatus.CREATED);
    }

    @GetMapping("api/courses/{id}")
    @RolesAllowed(Roles.Read)
    @Operation(
            summary = "Get a course by ID",
            description = "Returns a single course by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Course found successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Not enough rights")
    public ResponseEntity<Course> getCourse(
            @Parameter(description = "ID of the course", required = true)
            @PathVariable @NonNull Long id) {
        Course course = courseService.getCourse(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }



    @GetMapping("api/courses")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Get all courses", description = "Returns a list of all available courses ordered by name")
    @ApiResponse(responseCode = "200", description = "Courses successfully retrieved")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<Course>> all() {
        List<Course> course = courseService.getCourses();
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


    @PutMapping("api/courses/{id}")
    @RolesAllowed(Roles.Admin)
    //@PreAuthorize("#course.ersteller.id == authentication.principal.id") // in plans
    @Operation(summary = "Update a course", description = "Updates an existing course by its ID")
    @ApiResponse(responseCode = "200", description = "Course successfully updated")
    @ApiResponse(responseCode = "404", description = "Course not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Course> updateCourse(
            @Parameter(description = "Updated course object", required = true)
            @Valid @RequestBody Course course,
            @Parameter(description = "ID of the course to update", required = true)
            @PathVariable @NonNull Long id) {
        Course updatedCourse = courseService.updateCourse(course, id);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }


    @DeleteMapping("api/courses/{id}")
    @RolesAllowed(Roles.Admin)
    //@PreAuthorize("hasRole('ADMIN')") // in plans
    @Operation(summary = "Delete a course", description = "Deletes a course by its ID")
    @ApiResponse(responseCode = "200", description = "Course successfully deleted")
    @ApiResponse(responseCode = "404", description = "Course not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<MessageResponse> deleteCourse(
            @Parameter(description = "ID of the course to delete", required = true)
            @PathVariable @NonNull Long id) {
        try {
            return ResponseEntity.ok(courseService.deleteCourse(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("api/courses/{id}/progress")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Get course progress in percent")
    @ApiResponse(responseCode = "200", description = "Progress successfully retrieved")
    public ResponseEntity<Double> getProgress(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(lessonProgressService.getCourseProgress(id, authentication.getName()));
    }
}
