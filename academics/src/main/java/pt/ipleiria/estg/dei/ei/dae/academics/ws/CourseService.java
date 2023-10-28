package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.util.List;
import java.util.stream.Collectors;

@Path("courses") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class CourseService {

    @EJB
    private CourseBean courseBean;

    //region GET
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/courses/”
    public List<CourseDTO> getAllCourses() {
        return toDTOs(courseBean.getAllCourses());
    }

    @GET
    @Path("{courseCode}")
    public Response getCourseDetails(@PathParam("courseCode") long courseCode) {
        Course course = courseBean.find(courseCode);

        if (course != null) {
            return Response.ok(toDTO(course)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }

    //get subjects in course
    @GET
    @Path("{courseCode}/subjects")
    public Response getCourseSubjects(@PathParam("courseCode") long courseCode) {
        List<Subject> subjects = courseBean.getSubjects(courseCode);

        if (!subjects.isEmpty()) {
            return Response.ok(subjectsToDTOs(subjects)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }

    //get students in course
    @GET
    @Path("{courseCode}/students")
    public Response getCourseStudents(@PathParam("courseCode") long courseCode) {
        List<Student> students = courseBean.getStudents(courseCode);

        if (!students.isEmpty()) {
            return Response.ok(studentsToDTOs(students)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }
    //endregion

    @POST
    @Path("/")
    public Response createNewCourse (CourseDTO courseDTO){
        courseBean.create(
                courseDTO.getCode(),
                courseDTO.getName()
        );

        Course newCourse = courseBean.find(courseDTO.getCode());

        if(newCourse == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newCourse)).build();
    }

    //update
    @PUT
    @Path("{courseCode}")
    public Response updateCourse(@PathParam("courseCode") long courseCode, CourseDTO courseDTO) {
        courseBean.updateCourse(courseCode, courseDTO.getName());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{courseCode}")
    public Response deleteCourse(@PathParam("courseCode") long courseCode) {
        if (courseBean.deleteCourse(courseCode)) {
            Response.ok();
        }
        return Response.status(Response.Status.CONFLICT)
                .entity("CONFLICT_DELETING")
                .build();
    }

    //region DTO conversion
    // Converts an entity Course to a DTO Course class
    private CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getCode(),
                course.getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<CourseDTO> toDTOs(List<Course> courses) {
        return courses.stream().map(this::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region DTO conversion >>>> SubjectDTO
    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getCourseYear(),
                subject.getScholarYear()
        );
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region DTO conversion >>>> StudentDTO
    // Converts an entity Student to a DTO Student class
    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }
    //endregion
}
