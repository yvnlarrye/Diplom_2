import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserLoginTest extends UserTest {

    private final Map<String, String> loginData = new HashMap<>();

    @Before
    public void setUp() {
        super.setUp();
        loginData.put("email", getUserData().get("email"));
        loginData.put("password", getUserData().get("password"));
    }

    @Test
    @DisplayName("Логин пользователя с валидными данными")
    public void loginUserWithValidData() {
        ResponseBodyExtractionOptions body = getUserService().loginUser(loginData)
                .then()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'email' при авторизации не соответствует ожидаемому", getUserData().get("email").toLowerCase(), body.path("user.email"));
        Assert.assertEquals("Поле 'name' при авторизации не соответствует ожидаемому", getUserData().get("name"), body.path("user.name"));
        Assert.assertNotNull("Поле 'accessToken' равняется NULL при при авторизации", body.path("accessToken"));
        Assert.assertNotNull("Поле 'refreshToken' равняется NULL при при авторизации", body.path("refreshToken"));
    }

    @Test
    @DisplayName("Логин пользователя с не верным паролем")
    public void loginUserWithNotValidPassword() {
        ResponseBodyExtractionOptions body = getUserService().loginUser(loginData.get("email"), RandomStringUtils.randomAlphabetic(10))
                .then()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что 'email' или пароль не корретны не соответствует ожидаемому"
                , "email or password are incorrect", body.path("message"));
    }

    @Test
    @DisplayName("Логин пользователя с не верным email")
    public void loginUserWithNotValidEmail() {
        ResponseBodyExtractionOptions body = getUserService().loginUser(RandomStringUtils.randomAlphabetic(10), loginData.get("password"))
                .then()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что 'email' или пароль не корретны не соответствует ожидаемому"
                , "email or password are incorrect", body.path("message"));
    }
}
