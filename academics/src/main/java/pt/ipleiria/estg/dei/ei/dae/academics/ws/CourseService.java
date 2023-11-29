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
import pt.ipleiria.estg.dei.ei.dae.academics.utils.DTOconverter;

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
        return DTOconverter.coursesToDTOs(courseBean.getAllCourses());
    }

    @GET
    @Path("{courseCode}")
    public Response getCourseDetails(@PathParam("courseCode") long courseCode) {
        Course course = courseBean.find(courseCode);

        if (course != null) {
            return Response.ok(DTOconverter.toDTO(course)).build();
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
            return Response.ok(DTOconverter.subjectsToDTOs(subjects)).build();
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
            return Response.ok(DTOconverter.studentsToDTOs(students)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }
    //endregion

    @POST
    @Path("/")
    public Response createNewCourse (CourseDTO courseDTO) {
        courseBean.create(
                courseDTO.getCode(),
                courseDTO.getName()
        );

        Course newCourse = courseBean.find(courseDTO.getCode());

        if(newCourse == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(DTOconverter.toDTO(newCourse)).build();
    }

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
}
