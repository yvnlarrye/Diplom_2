package service;

import io.qameta.allure.Step;
import model.pojo.ingredient.Ingredient;
import model.pojo.ingredient.Ingredients;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientService {
    private static final String ENDPOINT = "/api/ingredients";

    @Step("Получение списка ингредиентов")
    public List<Ingredient> getAvailableIngredients() {
        return given()
                .get(ENDPOINT)
                .body()
                .as(Ingredients.class)
                .getData();
    }
}