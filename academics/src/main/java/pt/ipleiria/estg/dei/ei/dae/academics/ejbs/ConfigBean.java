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

    @PostConstruct // will be called by the server right after it instantiates this EJB
    public void populateDB() {
        System.out.println("BEGIN populateDB");

        courseBean.create(9119, "Engenharia Informática (D)");
        courseBean.create(9885, "Engenharia Informática (PL)");
        courseBean.create(9741, "Engenharia Automóvel (D)");
        courseBean.create(9123, "Engenharia Mecânica (D)");
        courseBean.create(9089, "Engenharia Civil (D)");

        studentBean.create("piet","l2t3a4g","Pieter","pieter@my.ipleiria.pt",9119);
        studentBean.create("ma97","aww3agd","Emma","emma@my.ipleiria.pt",9119);
        studentBean.create("cattzp","C$wLZjs","Catarina","catarina@my.ipleiria.pt",9885);
        studentBean.create("gzTag","ZDHDgRM","Tiago","tiago@my.ipleiria.pt",9741);
        studentBean.create("sandman","AZBy*2q","José","jose@my.ipleiria.pt",9123);
        studentBean.create("inesmaria","uHQ3^Fv","Inês","ines@my.ipleiria.pt",9089);

        System.out.println("END populateDB");
    }
}
