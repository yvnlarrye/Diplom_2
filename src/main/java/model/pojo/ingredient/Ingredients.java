package model.pojo.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredients {

    private List<Ingredient> data;

    public Ingredients() {}

    public Ingredients(List<Ingredient> data) {
        this.data = data;
    }


    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    public List<Ingredient> getData() {
        return data;
    }
}
