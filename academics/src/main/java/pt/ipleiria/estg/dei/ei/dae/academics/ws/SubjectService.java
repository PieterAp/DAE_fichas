package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.utils.DTOconverter;

import java.util.List;
import java.util.stream.Collectors;

@Path("subjects") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SubjectService {
    @EJB
    private SubjectBean subjectBean;


    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<SubjectDTO> getAllSubjects() {
        return DTOconverter.subjectsToDTOs(subjectBean.getAllSubjects());
    }

    @GET
    @Path("{subjectCode}/students")
    public Response getSubjectStudents(@PathParam("subjectCode") long subjectCode) {
        Subject subject = subjectBean.findStudentsInSubjects(subjectCode);
        if (subject != null) {
            var dtos = DTOconverter.studentsToDTOs(subject.getStudents());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }

    @GET
    @Path("{subjectCode}/teachers")
    public Response getSubjectTeachers(@PathParam("subjectCode") long subjectCode) {
        Subject subject = subjectBean.findTeachersInSubjects(subjectCode);
        if (subject != null) {
            var dtos = DTOconverter.teacherDTOS(subject.getTeachers());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewSubject (SubjectDTO subjectDTO){
        subjectBean.create(
                subjectDTO.getCode(),
                subjectDTO.getName(),
                subjectDTO.getCourseCode(),
                subjectDTO.getCourseYear(),
                subjectDTO.getScholarYear()
        );

        Subject newSubject = subjectBean.find(subjectDTO.getCode());

        if(newSubject == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(DTOconverter.toDTO(newSubject)).build();
        //return Response.status(Response.Status.CREATED).entity(toDTO(newSubject)).build();
    }

    @PUT
    @Path("{subjectCode}")
    public Response updateSubject(@PathParam("subjectCode") long subjectCode, SubjectDTO subjectDTO) {
        subjectBean.updateSubject(subjectCode, subjectDTO.getName(), subjectDTO.getCourseYear(), subjectDTO.getScholarYear());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{subjectCode}")
    public Response deleteSubject(@PathParam("subjectCode") long subjectCode) {
        subjectBean.deleteSubject(subjectCode);
        return Response.status(Response.Status.OK).build();
    }
}
