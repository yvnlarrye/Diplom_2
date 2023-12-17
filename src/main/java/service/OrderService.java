package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.pojo.ingredient.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderService {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String ENDPOINT = "/api/orders";

    @Step("Создание заказа")
    public Response createOrder(List<Ingredient> ingredients, String accessToken) {
        Map<String, String[]> body = new HashMap<>();
        String[] arrayIngredient = new String[ingredients.size()];

        for (int i = 0; i < ingredients.size(); i++) {
            arrayIngredient[i] = ingredients.get(i).get_id();
        }

        body.put("ingredients", arrayIngredient);

        return given()
                .header("Content-type", JSON_CONTENT_TYPE)
                .header("Authorization", accessToken)
                .and()
                .body(body)
                .post(ENDPOINT);
    }

    @Step("Получение заказов")
    public Response getAllOrders(String accessToken){
        return given()
                .header("Authorization", accessToken)
                .get(ENDPOINT);
    }
}
