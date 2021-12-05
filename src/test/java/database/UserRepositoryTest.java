package database;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRepositoryTest {

    private UserRepository underTest;


    @BeforeEach
    void setup() {
        underTest = UserRepository.getInstance(TestConfigs.ADMIN_CSV_TEST_PATH, TestConfigs.USER_CSV_TEST_PATH);
    }

    @AfterEach
    void teardown() {
    }

    @Test
    void testAwardLoyaltyPoint() {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        u.setEmailAddress("a,");
        u.setAccountType("admin");
        underTest.register(u);
        underTest.awardLoyaltyPoint(u.getUsername());
    }

    @Test
    void testGetAllCustomers() {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        u.setEmailAddress("a,");
        u.setAccountType("customer");

        underTest.register(u);

        List<User> expected = new ArrayList<>();
        expected.add(u);

        assertThat(expected).isEqualTo(underTest.getAllCustomers());
    }

    @Test
    void testUpdateUser() {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        u.setEmailAddress("a,");
        u.setAccountType("customer");

        underTest.register(u);

        u.setPassword("nothing");

        underTest.updateUser(u);
    }

    @Test
    void testIsAdmin() {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        u.setEmailAddress("a.gangapersaud@gmail.com");
        u.setAccountType("admin");

        underTest.register(u);

        underTest.login("username", "password");

        assertThat(underTest.isAdmin()).isTrue();
    }

    @Test
    void testLoginFailed() {
        User u = new User();
        u.setPassword("book");
        u.setUsername("anil");
        u.setAccountType("customer");
        u.setEmailAddress("a,gang");

        assertThat(underTest.login("book", "anil")).isFalse();
    }

    @Test
    void testGuestRegistrationFailedNull() {
        User u = null;
        assertThat(underTest.saveGuestAccount(u)).isFalse();
    }

    @Test
    void testChangeUsernameSuccess() {
        User u = new User();
        u.setUsername("username1");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);
        underTest.register(u);
        underTest.login("username1", "password");
        underTest.changeUsername("username123", "username1");
    }

    @Test
    void testChangeUsernameFailedNonUnique() {
        User u = new User();
        u.setUsername("username3");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);

        underTest.login("username3", "password");

        underTest.changeUsername("username3", "username3");
    }

    @Test
    void testChangePasswordSuccess() {
        User u = new User();
        u.setUsername("username54");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);

        underTest.login("username54", "password");

        underTest.changePassword("password123", "username54");
    }

    @Test
    void testChangePasswordFailedInvalidPassword() {
        User u = new User();
        u.setUsername("username67");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);

        underTest.login("username67", "password");

        underTest.changePassword("", "username67");
    }

    @Test
    void testChangePasswordFailedNoUser() {
        underTest.changePassword("newPass", "nonexistant");
    }

    @Test
    void testChangeEmailSuccess() {
        User u = new User();
        u.setUsername("username83");
        u.setPassword("password");
        u.setEmailAddress("username83");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);

        underTest.login("username83", "password");

        boolean result = underTest.changeEmail("a.gangapersaud@gmail.com", "username83");
        assertThat(result).isTrue();
    }

    @Test
    void testChangeEmailFailedInvalidEmail() {
        User u = new User();
        u.setUsername("username345");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);

        boolean result = underTest.changeEmail("", "username345");
        assertThat(result).isFalse();
    }

    @Test
    void testChangeEmailFailedNoUser() {
        underTest.changeEmail("newEmail", "nouser");
    }

    @Test
    void testDeleteUser() {
        underTest.deleteUser("username");
    }

    @Test
    void testGetLoggedInUser() {
        User u = new User();
        u.setUsername("username345");
        u.setPassword("password");
        u.setEmailAddress("username");
        u.setAccountType("customer");
        u.setLoyaltyPoints(0);

        underTest.register(u);
        underTest.login("username345", "password");
        assertThat(underTest.getUser(u.getUsername())).isEqualTo(u);
        assertThat(underTest.getLoggedInUser()).isEqualTo(u);
    }

    @Test
    void testSaveGuestAccountSuccess() {
        User u = new User();
        u.setUsername("hello");
        u.setEmailAddress("a,gapsdo");
        assertThat(underTest.saveGuestAccount(u)).isTrue();
    }
}