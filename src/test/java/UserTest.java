import org.junit.After;
import org.junit.Before;
import service.UserService;
import java.util.Map;

public class UserTest extends StellarBurgersAPITest {

    private final UserService userService = new UserService();
    private Map<String, String> userData;
    private String accessToken = null;

    public UserService getUserService() {
        return userService;
    }

    public Map<String, String> getUserData() {
        return userData;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Before
    public void setUp() {
        super.setUp();
        userData = getUserService().getMapGeneratedDataUser();
        accessToken = getUserService()
                .createUser(userData)
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
