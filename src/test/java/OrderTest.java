import model.pojo.ingredient.Ingredient;
import org.junit.After;
import org.junit.Before;
import service.IngredientService;
import service.OrderService;
import service.UserService;

import java.util.List;

public class OrderTest extends StellarBurgersAPITest {
    private final IngredientService ingredientService = new IngredientService();
    private final UserService userService = new UserService();
    private final OrderService orderService = new OrderService();
    private List<Ingredient> availableIngredients;
    private String accessToken;

    public IngredientService getIngredientService() {
        return ingredientService;
    }

    public UserService getUserService() {
        return userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<Ingredient> getAvailableIngredients() {
        return availableIngredients;
    }

    @Before
    public void setUp() {
        super.setUp();
        availableIngredients = getIngredientService().getAvailableIngredients();
        accessToken = getUserService()
                .createUser(getUserService().getMapGeneratedDataUser())
                .then().extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userService.deleteUser(accessToken);
            accessToken = null;
        }
    }
}
