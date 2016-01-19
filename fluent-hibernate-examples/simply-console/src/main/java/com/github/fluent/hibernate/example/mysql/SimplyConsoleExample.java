package com.github.fluent.hibernate.example.mysql;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.jboss.logging.Logger;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.example.mysql.persistent.User;
import com.github.fluent.hibernate.example.mysql.persistent.UserAddress;
import com.github.fluent.hibernate.example.mysql.persistent.UserFriend;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class SimplyConsoleExample {

    private static final String USER_A_STREET = "street A";

    private static final String USER_LOGIN_A = "loginA";

    private static final Logger LOG = Logger.getLogger(SimplyConsoleExample.class);

    public static void main(String[] args) {
        try {
            HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
            .createSessionFactory();
            new SimplyConsoleExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        /*
        deleteAllUsers();
        insertUsers();

        countUsers();
        doSomeUserAddressStuff();
        doSomeFriendsStuff();

        dealWithGoodFriends();
         */

        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {

                hibernate.User user = new hibernate.User();
                user.setName("Joe");
                user.setGoal(250);
                session.save(user);

                return null;
            }

        });
    }

    private void dealWithGoodFriends() {
        addGoodFriendsToUser(findUserByLogin(USER_LOGIN_A));
        User user = H.<User> request(User.class).eq("login", USER_LOGIN_A).fetchJoin("goodFriends")
                .first();
        System.out.println(user.getGoodFriends());
    }

    private void addGoodFriendsToUser(User user) {
        user.setGoodFriends(Arrays.asList(User.create("good friend 1", "good friend 1", 20),
                User.create("good friend 2", "good friend 2", 20)));
        H.saveOrUpdate(user);
    }

    private void doSomeFriendsStuff() {
        addFrindsToUser(USER_LOGIN_A);
        findUsersByFriendName();
    }

    private void addFrindsToUser(String userLogin) {
        User user = getUserWithFriends(userLogin);
        user.addFriend(UserFriend.createByName("John"));
        user.addFriend(UserFriend.createByName("Doe"));
        H.saveOrUpdate(user);
    }

    private void findUsersByFriendName() {
        List<User> users = H.<User> request(User.class).innerJoin("friends").proj("name")
                .eq("friends.name", "John").transform(User.class).list();
        LOG.info(String.format("Users with friends: %s", users));
    }

    private void doSomeUserAddressStuff() {
        getUserByStreet();
        getStreetByUser();
    }

    private void getUserByStreet() {
        User user = H.<User> request(User.class).innerJoin("address")
                .eq("address.street", USER_A_STREET).first();
        LOG.info(String.format("User %s address: %s", user, user.getAddress()));
    }

    private void getStreetByUser() {
        final String userLogin = USER_LOGIN_A;
        UserAddress address = H.<UserAddress> request(UserAddress.class).innerJoin("user")
                .eq("user.login", userLogin).first();
        LOG.info(String.format("UserAddress: %s", address));
    }

    private User getUserWithFriends(String userLogin) {
        return H.<User> request(User.class).eq(User.LOGIN, userLogin).fetchJoin("friends").first();
    }

    private User findUserByLogin(String login) {
        return H.<User> request(User.class).eq(User.LOGIN, login).first();
    }

    private void deleteAllUsers() {
        H.update("delete from User").execute();
    }

    private void insertUsers() {
        H.saveOrUpdate(userA());
        H.saveOrUpdate(userB());
    }

    private void countUsers() {
        int count = H.<Long> request(User.class).count();
        LOG.info("Users count: " + count);
    }

    public static User userA() {
        return addAddress(User.create(USER_LOGIN_A, "A user", 20), USER_A_STREET);
    }

    public static User userB() {
        return addAddress(User.create("loginB", "B user", 30), "street B");
    }

    public static User addAddress(User toUser, String street) {
        toUser.setAddress(UserAddress.create(street, toUser));
        return toUser;
    }
}
