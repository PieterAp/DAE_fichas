package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

@Stateless
public class AdministratorBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public void create (String username, String password, String name, String email) {
        Administrator administrator = new Administrator(username, hasher.hash(password), name, email);
        entityManager.persist(administrator);
    }
}
