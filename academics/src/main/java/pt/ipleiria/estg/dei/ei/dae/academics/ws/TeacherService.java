package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.TeacherBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.utils.DTOconverter;

import java.util.List;
import java.util.stream.Collectors;

@Path("teachers") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class TeacherService {

    @EJB
    TeacherBean teacherBean;

    //region GET
    @GET
    @Path("/")
    public List<TeacherDTO> getAllTeachers() {
        return DTOconverter.teacherDTOS(teacherBean.getAll());
    }

    @GET
    @Path("{username}")
    public Response getTeacherDetails(@PathParam("username") String username) {
        Teacher teacher = teacherBean.find(username);
        if (teacher != null) {
            return Response.ok(DTOconverter.toDTO(teacher)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_TEACHER")
                .build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getTeacherSubjects(@PathParam("username") String username) {
        Teacher teacher = teacherBean.findTeacherWithSubjects(username);
        if (teacher != null) {
            var dtos = DTOconverter.subjectsToDTOs(teacher.getSubjects());
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
    public Response createNewTeacher (TeacherDTO teacherDTO){
        teacherBean.create(
                teacherDTO.getUsername(),
                teacherDTO.getPassword(),
                teacherDTO.getName(),
                teacherDTO.getEmail(),
                teacherDTO.getOffice()
        );

        Teacher newTeacher = teacherBean.find(teacherDTO.getUsername());

        if(newTeacher == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(DTOconverter.toDTO(newTeacher)).build();
    }

    @POST
    @Path("{username}/associate/{subjectCode}")
    public Response associateTeacherToSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        if (teacherBean.associateTeacherToSubject(username, subjectCode)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }

    @POST
    @Path("{username}/dissociate/{subjectCode}")
    public Response dissociateTeacherToSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) {
        if (teacherBean.dissociateTeacherToSubject(username, subjectCode)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_COURSE")
                .build();
    }
    //endregion

    @PUT
    @Path("{username}")
    public Response updateTeacher(@PathParam("username") String username, TeacherDTO teacherDTO) {
        teacherBean.updateTeacher(username,teacherDTO.getEmail(),teacherDTO.getName(),teacherDTO.getPassword());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteTeacher(@PathParam("username") String username) {
        teacherBean.deleteTeacher(username);
        return Response.status(Response.Status.OK).build();
    }
}
