import io.restassured.RestAssured;
import org.junit.Before;

public class StellarBurgersAPITest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
}
