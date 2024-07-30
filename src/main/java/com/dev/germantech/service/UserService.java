package com.dev.germantech.service;

import com.dev.germantech.model.User;
import com.dev.germantech.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.transaction.Transactional;
import java.util.List;

public class UserService implements UserRepository {

    private SessionFactory sessionFactory;

    public UserService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public User findByCpf(String cpf) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE cpf = :cpf", User.class)
                    .setParameter("cpf", cpf)
                    .uniqueResult();
        }
    }

    @Override
    public User findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    @Transactional
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Transactional
    public void deleteById(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);

            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        }
    }
}
