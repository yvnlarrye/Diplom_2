import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ResponseBodyExtractionOptions;
import model.pojo.ingredient.Ingredient;
import model.pojo.order.SuccessResponseCreateOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Создание заказов")
public class CreateOrderTest extends OrderTest {

    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getAvailableIngredients().get(0));
        ingredients.add(getAvailableIngredients().get(1));

        SuccessResponseCreateOrder body = getOrderService().createOrder(ingredients, getAccessToken())
                .then()
                .statusCode(200)
                .extract().body().as(SuccessResponseCreateOrder.class);

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.success);
        Assert.assertNotNull("Название бургера равно NULL", body.name);

        ArrayList<Ingredient> actualIngredients = body.order.ingredients;

        assertThat(actualIngredients)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(ingredients)
                .as("Добавленные ингредиенты не соответствуют отправленным в заказ");
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getAvailableIngredients().get(0));
        ingredients.add(getAvailableIngredients().get(1));

        SuccessResponseCreateOrder body = getOrderService().createOrder(ingredients, "")
                .then()
                .statusCode(200)
                .extract().body().as(SuccessResponseCreateOrder.class);

        Assert.assertTrue("Поле 'success' при авторизации не соответствует ожидаемому", body.success);
        Assert.assertNotNull("Название бургера равно NULL", body.name);
    }

    @Test
    @DisplayName("Создание заказа без указания ингредиентов")
    public void createOrderWithoutIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        ResponseBodyExtractionOptions body = getOrderService().createOrder(ingredients, getAccessToken())
                .then()
                .statusCode(400)
                .extract().body();

        Assert.assertFalse("Поле 'success' при авторизации не соответствует ожидаемому", body.path("success"));
        Assert.assertEquals("Сообщение о не добавленных ингредиентах не соответствует ожидаемому"
                , "Ingredient ids must be provided", body.path("message"));
    }

    @Test
    @DisplayName("Создание заказа c указанием неверного хеша ингредиентов")
    public void createOrderWithNotValidHashIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getAvailableIngredients().get(0));
        ingredients.add(getAvailableIngredients().get(1));

        ingredients.get(0).set_id(RandomStringUtils.randomAlphabetic(10));
        ingredients.get(1).set_id(RandomStringUtils.randomAlphabetic(10));

        getOrderService().createOrder(ingredients, getAccessToken())
                .then()
                .statusCode(500);
    }
}