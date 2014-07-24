package objectstorage

/**
 *
 * @author Sergey Krauchenia
 */
class AuthorizationTest extends BaseSpec {

  "Connection" should {
    "authorized successfully with valid credentials" in new ApiSpecContext {
      Connection(userName, apiKey) must beAnInstanceOf[Authorized]
    }

    "failed to authorized with invalid credentials" in new ApiSpecContext {
      Connection("invalid-username", apiKey) must beAnInstanceOf[NotAuthorized]
    }
  }

}
