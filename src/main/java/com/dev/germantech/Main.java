package com.dev.germantech;

import com.dev.germantech.controller.UserController;
import com.dev.germantech.service.UserService;
import com.dev.germantech.utils.HibernateUtil;
import com.dev.germantech.view.UserView;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserService userService = new UserService(sessionFactory);

        UserController userController = new UserController(userService);
        UserView userView = new UserView(userController);
    }
}