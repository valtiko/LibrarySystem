package Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DatabaseAccess {

    private final SessionFactory sessionFactory;
    private static DatabaseAccess databaseAccess;
    private final EntityManager entityManager;

    public static DatabaseAccess getInstance() {
        if (databaseAccess == null) databaseAccess = new DatabaseAccess();
        return databaseAccess;
    }

    public DatabaseAccess() {
        this.sessionFactory = getSessionFactory();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("biblioteka");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static EntityManager getEntityManager(){
        return getInstance().entityManager;
    }

    public SessionFactory getSessionFactory() {
        StandardServiceRegistry registry;
        SessionFactory sessionFactory;
        registry = new StandardServiceRegistryBuilder().configure().build();
        MetadataSources sources = new MetadataSources(registry);
        Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        return sessionFactory;
    }

    public Object find(Class c, int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object o = session.find(c, id);
        session.close();
        return o;
    }

    public void save(Object o) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(o);
        transaction.commit();
        session.close();
    }

    public void delete(Object o) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(o);
        transaction.commit();
        session.close();
    }
    public void update(Object o) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(o);
        transaction.commit();
        session.close();
    }
    public List<Object> getQuery(String query) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        Query<Object> q = session.createQuery(query);
        List<Object> list = q.list();
        t.commit();
        session.close();
        return list;
    }

    public List<Object[]> getQueryAsTableOfObjects(String query) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        Query<Object[]> q = session.createQuery(query);
        List<Object[]> list = q.list();
        t.commit();
        session.close();
        return list;
    }
    
}
