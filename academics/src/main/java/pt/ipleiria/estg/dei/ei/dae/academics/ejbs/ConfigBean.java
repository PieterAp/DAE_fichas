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

    @PostConstruct // will be called by the server right after it instantiates this EJB
    public void populateDB() {
        System.out.println("Hello Java EE!");

        studentBean.create("piet","l2t3a4g","Pieter","pieter@my.ipleiria.pt");
    }
}
