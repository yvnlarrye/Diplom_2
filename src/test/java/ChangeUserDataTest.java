import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;


public class ChangeUserDataTest extends UserTest {

    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void changedUserNameWithAuthorization() {
        getUserData().put("name", RandomStringUtils.randomAlphabetic(10));
        ResponseBodyExtractionOptions body = getUserService().changedUserData(getUserData(), getAccessToken())
                .then()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'name' не соответствует данным пользователя", getUserData().get("name"), body.path("user.name"));
        Assert.assertEquals("Поле 'email' не соответствует данным пользователя", getUserData().get("email").toLowerCase(), body.path("user.email"));
    }

    @Test
    @DisplayName("Изменение email с авторизацией")
    public void changedUserEmailWithAuthorization() {
        getUserData().put("email", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ResponseBodyExtractionOptions body = getUserService().changedUserData(getUserData(), getAccessToken())
                .then()
                .statusCode(200)
                .extract()
                .body();

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Поле 'name' не соответствует данным пользователя", getUserData().get("name"), body.path("user.name"));
        Assert.assertEquals("Поле 'email' не соответствует данным пользователя", getUserData().get("email").toLowerCase(), body.path("user.email"));
    }

    @Test
    @DisplayName("Изменение имени без авторизации")
    public void changedUserNameWithoutAuthorization() {
        getUserData().put("name", RandomStringUtils.randomAlphabetic(10));
        ResponseBodyExtractionOptions body = getUserService().changedUserData(getUserData(), "")
                .then()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь не авторизован не соответствует ожидаемому"
                , "You should be authorised", body.path("message"));
    }

    @Test
    @DisplayName("Изменение email без авторизации")
    public void changedUserEmailWithoutAuthorization() {
        getUserData().put("email", RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ResponseBodyExtractionOptions body = getUserService().changedUserData(getUserData(), "")
                .then()
                .statusCode(401)
                .extract()
                .body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение, что пользователь не авторизован не соответствует ожидаемому"
                , "You should be authorised", body.path("message"));
    }
}
