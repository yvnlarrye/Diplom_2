import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class UserCreationTest extends UserTest {

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        setUserData(getUserService().getMapGeneratedDataUser());

        ResponseBodyExtractionOptions body = getUserService().createUser(getUserData())
                .then()
                .statusCode(200)
                .extract().body();

        String accessToken = body.path("accessToken");
        setAccessToken(accessToken);

        Assert.assertTrue("Поле 'success' при создании пользователя не соответствует ожидаемому",
                body.path("success"));
        Assert.assertEquals("Поле 'email' не соответствует ожидаемому при создании пользователя",
                getUserData().get("email").toLowerCase(), body.path("user.email"));
        Assert.assertEquals("Поле 'name' не соответствует ожидаемому при создании пользователя",
                getUserData().get("name"), body.path("user.name"));
        Assert.assertNotNull("Поле 'accessToken' равняется NULL при создании пользователя",
                accessToken);
        Assert.assertNotNull("Поле 'refreshToken' равняется NULL при при авторизации",
                body.path("refreshToken"));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createUserThatAlreadyExistsTest() {
        setUserData(getUserService().getMapGeneratedDataUser());

        Response user = getUserService().createUser(getUserData());
        String accessToken = user.then().extract().body().path("accessToken");
        setAccessToken(accessToken);

        ResponseBodyExtractionOptions body = getUserService().createUser(getUserData())
                .then()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при повторном создании пользователя не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь уже создан не соответствует ожидаемому"
                , "User already exists", body.path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без указания 'EMAIL'")
    public void createUserWithoutEmail() {
        Map<String, String> mapGeneratedDataUser = getUserService().getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("email");
        ResponseBodyExtractionOptions body = getUserService().createUser(mapGeneratedDataUser)
                .then()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'email' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'email' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без указания пароля")
    public void createUserWithoutPassword() {
        Map<String, String> mapGeneratedDataUser = getUserService().getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("password");
        ResponseBodyExtractionOptions body = getUserService().createUser(mapGeneratedDataUser)
                .then()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'password' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'password' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));

    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    public void createUserWithoutName() {
        Map<String, String> mapGeneratedDataUser = getUserService().getMapGeneratedDataUser();
        mapGeneratedDataUser.remove("name");
        ResponseBodyExtractionOptions body = getUserService().createUser(mapGeneratedDataUser)
                .then()
                .statusCode(403)
                .extract().body();
        Assert.assertFalse("Поле 'success' при создании пользователя без 'name' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение при создании пользователя без 'name' не соответствует ожидаемому"
                , "Email, password and name are required fields", body.path("message"));

    }
}
