package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;
import java.util.stream.Collectors;

@Path("subjects") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SubjectService {
    @EJB
    private SubjectBean subjectBean;

    @GET
    @Path("{subjectCode}/students")
    public Response getSubjectStudents(@PathParam("subjectCode") long subjectCode) {
        Subject subject = subjectBean.findStudentsInSubjects(subjectCode);
        if (subject != null) {
            var dtos = studentsToDTOs(subject.getStudents());
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
            var dtos = teacherDTOS(subject.getTeachers());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_SUBJECT")
                .build();
    }

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

    //region DTO conversion >>>> TeacherDTO
    // Converts an entity Student to a DTO Student class
    private TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getUsername(),
                teacher.getPassword(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getOffice()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<TeacherDTO> teacherDTOS(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
    }
    //endregion
}
