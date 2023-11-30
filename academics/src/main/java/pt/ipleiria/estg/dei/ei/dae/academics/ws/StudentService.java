package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.academics.utils.DTOconverter;

import java.util.List;
import java.util.stream.Collectors;

@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
@Authenticated
@RolesAllowed({"Teacher", "Administrator", "Student"})
public class StudentService {
    @EJB
    private StudentBean studentBean;

    @EJB
    private EmailBean emailBean;

    //region GET
    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<StudentDTO> getAllStudents() {
        return DTOconverter.studentsToDTOsNoSubjects(studentBean.getAll());
    }

    @GET
    @Path("{username}")
    public Response getStudentDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        Student student = studentBean.findWithSubjects(username);
        return Response.ok(DTOconverter.toDTO(student)).build();
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

    @GET
    @Path("{username}/subjectsAvailable")
    public Response getAvailableSubjects(@PathParam("username") String username) {
        List<Subject> subjects = studentBean.subjectsAvailable(username);

        if (subjects != null) {
            var dtos = DTOconverter.subjectsToDTOs(subjects);
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }
    //endregion

    //region POST
    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
        Student student = studentBean.find(username);
        if (student == null) {
            throw new MyEntityNotFoundException("Student with username '" + username + "' not found in our records.");
        }
        emailBean.send(student.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }

    @POST
    @Path("/")
    public Response createNewStudent (StudentDTO studentDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        Student newStudent = studentBean.find(studentDTO.getUsername());
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
        studentBean.updateStudent(username, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getPassword(), studentDTO.getCourseCode());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteStudent(@PathParam("username") String username) {
        studentBean.deleteStudent(username);
        return Response.status(Response.Status.OK).build();
    }
}
