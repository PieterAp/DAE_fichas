package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton // this EJB will have only one instance in the application
@Startup // this EJB will be automatically instantiated once the application is deployed onto the Wildfly application server
public class ConfigBean {

    @EJB
    private StudentBean studentBean;

    @EJB
    private CourseBean courseBean;

    @EJB
    private SubjectBean subjectBean;

    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private TeacherBean teacherBean;

    @PostConstruct // will be called by the server right after it instantiates this EJB
    public void populateDB() {
        System.out.println("BEGIN populateDB");

        courseBean.create(9119, "Engenharia Informática");
        courseBean.create(9741, "Engenharia Automóvel");
        courseBean.create(9123, "Engenharia Mecânica");
        courseBean.create(9089, "Engenharia Civil");


        // EI
        subjectBean.create(2321,"Sistemas de Apoio à Decisão",9119,2023,"2023/2024");
        subjectBean.create(2178,"Desenvolvimento de Aplicações Empresariais",9119,2023,"2023/2024");
        subjectBean.create(5045,"Advanced Topics in Software Engineering",9119,2023,"2023/2024");
        subjectBean.create(3640,"Engenharia do Conhecimento",9119,2024,"2023/2024");
        subjectBean.create(3734,"Inteligência Artificial",9119,2024,"2023/2024");
        // EA
        subjectBean.create(4684,"Análise Matemática",9741,2023,"2023/2024");
        subjectBean.create(1344,"Termodinâmica e Máquinas Térmicas",9741,2023,"2023/2024");
        subjectBean.create(8843,"Eletrotecnia Geral",9741,2023,"2023/2024");
        // EM
        subjectBean.create(9416,"Mecânica Aplicada",9123,2023,"2023/2024");


        // EI
        studentBean.create("piet","l2t3a4g","Pieter","pieter@my.ipleiria.pt",9119);
        studentBean.enrollStudentInSubject("piet",2321);
        studentBean.enrollStudentInSubject("piet",2178);
        studentBean.enrollStudentInSubject("piet",3640);
        studentBean.create("ma97","aww3agd","Emma","emma@my.ipleiria.pt",9119);
        studentBean.enrollStudentInSubject("ma97",2321);
        studentBean.enrollStudentInSubject("ma97",2178);
        // EA
        studentBean.create("cattzp","C$wLZjs","Catarina","catarina@my.ipleiria.pt",9741);
        studentBean.enrollStudentInSubject("cattzp",4684);
        studentBean.enrollStudentInSubject("cattzp",1344);
        studentBean.create("gzTag","ZDHDgRM","Tiago","tiago@my.ipleiria.pt",9741);
        studentBean.enrollStudentInSubject("gzTag",8843);
        // EM
        studentBean.create("sandman","AZBy*2q","José","jose@my.ipleiria.pt",9123);
        studentBean.enrollStudentInSubject("sandman", 9416);
        // EC
        studentBean.create("inesmaria","uHQ3^Fv","Inês","ines@my.ipleiria.pt",9089);

        administratorBean.create("jRibeiro","jroisbeiro", "José","jribeiro@my.ipleiria.pt");
        teacherBean.create("rmartinho","maarter","Ricardo","rmartinho@my.ipleiria.pt","A.S.0.1");
        teacherBean.associateTeacherToSubject("rmartinho", 2178);
        teacherBean.associateTeacherToSubject("rmartinho", 5045);

        System.out.println("END populateDB");
    }
}
