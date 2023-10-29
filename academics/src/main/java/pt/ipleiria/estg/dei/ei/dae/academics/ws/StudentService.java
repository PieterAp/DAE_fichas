package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.utils.DTOconverter;

import java.util.List;
import java.util.stream.Collectors;

@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class StudentService {
    @EJB
    private StudentBean studentBean;

    //region GET
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<StudentDTO> getAllStudents() {
        return DTOconverter.studentsToDTOs(studentBean.getAll());
    }

    @GET
    @Path("{username}")
    public Response getStudentDetails(@PathParam("username") String username) {
        Student student = studentBean.find(username);
        if (student != null) {
            return Response.ok(DTOconverter.toDTO(student)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        Student student = studentBean.findStudentWithSubjects(username);
        if (student != null) {
            var dtos = DTOconverter.subjectsToDTOs(student.getSubjects());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }
    //endregion

    //region POST
    @POST
    @Path("/")
    public Response createNewStudent (StudentDTO studentDTO){
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student newStudent = studentBean.find(studentDTO.getUsername());

        if(newStudent == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(DTOconverter.toDTO(newStudent)).build();
    }

    @POST
    @Path("{username}/enroll/{subjectCode}")
    public Response enrollStudentInSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        if (studentBean.enrollStudentInSubject(username, subjectCode)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }

    @POST
    @Path("{username}/unroll/{subjectCode}")
    public Response unrollStudentFromSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        if (studentBean.unrollStudentFromSubject(username, subjectCode)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }
    //endregion

    @PUT
    @Path("{username}")
    public Response updateStudent(@PathParam("username") String username, StudentDTO studentDTO) {
        studentBean.updateStudent(username, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getPassword());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteStudent(@PathParam("username") String username) {
        studentBean.deleteStudent(username);
        return Response.status(Response.Status.OK).build();
    }
}
