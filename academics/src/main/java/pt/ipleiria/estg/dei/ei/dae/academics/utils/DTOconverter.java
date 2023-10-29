package pt.ipleiria.estg.dei.ei.dae.academics.utils;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public final class DTOconverter {
    // toDTO -> Converts a certain entity to a DTO class relative to that entity
    // (...)ToDTOs -> converts an entire list of entities into a list of DTOs

    //region StudentDTO
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName()
        );
    }

    public static List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(DTOconverter::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region SubjectDTO
    public static SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getCourseYear(),
                subject.getScholarYear()
        );
    }

    public static List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(DTOconverter::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region CourseDTO
    public static CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getCode(),
                course.getName()
        );
    }

    public static List<CourseDTO> coursesToDTOs(List<Course> courses) {
        return courses.stream().map(DTOconverter::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region TeacherDTO
    public static TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getUsername(),
                teacher.getPassword(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getOffice()
        );
    }

    public static List<TeacherDTO> teacherDTOS(List<Teacher> teachers) {
        return teachers.stream().map(DTOconverter::toDTO).collect(Collectors.toList());
    }
    //endregion
}
