package ch.oberemok.marharyta.learnitself.lesson;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.course.Course;
import ch.oberemok.marharyta.learnitself.progress.LessonProgress;
import ch.oberemok.marharyta.learnitself.progress.LessonProgressService;
import ch.oberemok.marharyta.learnitself.progress.Status;
import ch.oberemok.marharyta.learnitself.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "Lessons", description = "Lesson management operations")
public class LessonController {
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;

    public LessonController(LessonService lessonService, LessonProgressService lessonProgressService) {
        this.lessonService = lessonService;
        this.lessonProgressService = lessonProgressService;
    }

    @GetMapping("api/courses/{courseId}/lessons")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Get all lessons of a course")
    @ApiResponse(responseCode = "200", description = "Lessons successfully retrieved")
    public ResponseEntity<List<Lesson>> getLessons(
            @Parameter(description = "ID of the course", required = true)
            @PathVariable Long courseId) {
        return new ResponseEntity<>(lessonService.getLessonsByCourse(courseId), HttpStatus.OK);
    }

    @GetMapping("api/courses/{courseId}/lessons/{id}")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Get a lesson by ID")
    @ApiResponse(responseCode = "200", description = "Lesson successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public ResponseEntity<Lesson> getLesson(
            @PathVariable Long courseId,
            @Parameter(description = "ID of the lesson", required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(lessonService.getLesson(id), HttpStatus.OK);
    }

    @PostMapping("api/courses/{courseId}/lessons")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Create a lesson")
    @ApiResponse(responseCode = "201", description = "Lesson successfully created")
    public ResponseEntity<Lesson> createLesson(
            @Parameter(description = "ID of the course", required = true)
            @PathVariable Long courseId,
            @Valid @RequestBody Lesson lesson) {
        return new ResponseEntity<>(lessonService.createLesson(lesson, courseId), HttpStatus.CREATED);
    }

    @PutMapping("api/courses/{courseId}/lessons/{id}")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Update a lesson")
    @ApiResponse(responseCode = "200", description = "Lesson successfully updated")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long id,
            @Valid @RequestBody Lesson lesson) {
        return new ResponseEntity<>(lessonService.updateLesson(lesson, id), HttpStatus.OK);
    }

    @DeleteMapping("api/courses/{courseId}/lessons/{id}")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Delete a lesson")
    @ApiResponse(responseCode = "200", description = "Lesson successfully deleted")
    public ResponseEntity<MessageResponse> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(lessonService.deleteLesson(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Change lesson-status (kanban)
    @PatchMapping("api/courses/{courseId}/lessons/{id}/status")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Update lesson status (Kanban)")
    @ApiResponse(responseCode = "200", description = "Status successfully updated")
    public ResponseEntity<LessonProgress> updateStatus(
            @PathVariable Long courseId,
            @PathVariable Long id,
            @RequestParam Status status,
            Authentication authentication) {
        return ResponseEntity.ok(lessonProgressService.updateStatus(id, status, authentication.getName()));
    }
}
