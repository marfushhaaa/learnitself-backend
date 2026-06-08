package ch.oberemok.marharyta.learnitself.course_participation;

import ch.oberemok.marharyta.learnitself.base.MessageResponse;
import ch.oberemok.marharyta.learnitself.course.Course;
import ch.oberemok.marharyta.learnitself.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "Course Participation", description = "Course participation management")
public class CourseParticipationController {

    private final CourseParticipationService participationService;

    public CourseParticipationController(CourseParticipationService participationService) {
        this.participationService = participationService;
    }

    // Participate a course
    @PostMapping("api/courses/{id}/participate")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Participate a course")
    @ApiResponse(responseCode = "200", description = "Successfully participated")
    public ResponseEntity<MessageResponse> participate(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                participationService.participate(id, authentication.getName())
        );
    }

    // Leave a Course
    @DeleteMapping("api/courses/{id}/participation")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Leave a course")
    @ApiResponse(responseCode = "200", description = "Successfully left course")
    public ResponseEntity<MessageResponse> leave(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                participationService.leaveCourse(id, authentication.getName())
        );
    }

    // Show all Courses
    @GetMapping("api/participation/my")
    @RolesAllowed(Roles.Read)
    @Operation(summary = "Get all my participated courses")
    @ApiResponse(responseCode = "200", description = "Courses successfully retrieved")
    public ResponseEntity<List<Course>> getMyParticipatedCourses(Authentication authentication) {
        return new ResponseEntity<>(
                participationService.getMyParticipatedCourses(authentication.getName()),
                HttpStatus.OK
        );
    }
}