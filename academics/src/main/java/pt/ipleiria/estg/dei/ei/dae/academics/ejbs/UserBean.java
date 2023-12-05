package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

@Stateless
public class UserBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;
    public User find(String username) {
        return entityManager.find(User.class, username);
    }

    public User findOrFail(String username) {
        var user = entityManager.getReference(User.class, username);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String username, String password) {
        var user = find(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    //todo
    public boolean setPassword(String username, String newPassword) {
        User user = entityManager.find(User.class, username);
        user.setPassword(hasher.hash(newPassword));
        return true;
    }
}
