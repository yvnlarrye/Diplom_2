import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import model.pojo.ingredient.Ingredient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GetOrdersListTest extends OrderTest {
    private final List<Ingredient> expectedIngredient = new ArrayList<>();

    @Before
    public void setUp() {
        super.setUp();
        expectedIngredient.add(getAvailableIngredients().get(0));
        expectedIngredient.add(getAvailableIngredients().get(1));
        getOrderService().createOrder(expectedIngredient, getAccessToken());
    }

    @Test
    @DisplayName("Получение заказа конкретного пользователя")
    public void getAllOrdersSpecificUser(){
        ResponseBodyExtractionOptions body = getOrderService()
                .getAllOrders(getAccessToken())
                .then()
                .statusCode(200)
                .extract().body();

        Assert.assertTrue("Поле 'success' не соответствует ожидаемому", body.path("success"));
        Assert.assertNotNull("Заказы равны NULL", body.path("orders"));
    }

    @Test
    @DisplayName("Получение заказа конкретного пользователя без авторизации")
    public void getAllOrdersSpecificUserWithoutAuthorization(){
        ResponseBodyExtractionOptions body = getOrderService()
                .getAllOrders("")
                .then()
                .statusCode(401)
                .extract().body();

        Assert.assertFalse("Поле 'success' не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение о необходимости авторизации не соответствует ожидаемому"
                , "You should be authorised", body.path("message"));
    }
}
