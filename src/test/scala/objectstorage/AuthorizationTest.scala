package objectstorage

/**
 *
 * @author Sergey Krauchenia
 */
class AuthorizationTest extends BaseSpec {

  "Connection" should {
    "authorized successfully with valid credentials" in {
      Connection(userName, apiKey) should beAnInstanceOf[Authorized]
    }

    "failed to authorized with invalid credentials" in {
      Connection("invalid-username", apiKey) should beAnInstanceOf[NotAuthorized]
    }
  }

}
