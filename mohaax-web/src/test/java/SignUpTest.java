import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.Instant;
import java.util.UUID;

/**
 * Clase de pruebas automatizadas para la funcionalidad de Registro (Sign Up).
 * Extiende {@link BaseTest} para heredar la configuración del WebDriver.
 */
public class SignUpTest extends BaseTest {

    /** Objeto de la página de registro para interactuar con sus elementos. */
    private SignUpPage signUpPage;

    /**
     * Proveedor de datos para escenarios de registro exitoso.
     * Genera datos únicos para asegurar la no repetición en cada ejecución.
     *
     * @return Un array de objetos que contiene: username, email, volute, password, repeatPassword, expectedSuccessMessage, y scenarioName.
     */
    @DataProvider(name = "ValidRegisterData")
    public Object[][] getValidRegisterData() {
        String uniqueIdentifier = String.valueOf(Instant.now().getEpochSecond()); // Genera un ID basado en el tiempo.
        // Combina el tiempo con un UUID truncado para mayor unicidad.
        String uniqueExecutionId = uniqueIdentifier + "_" + UUID.randomUUID().toString().substring(0, 8);
        String uniqueUsername = "usuarioExito" + uniqueExecutionId;
        String uniqueEmail = uniqueUsername + "@gmail.com";
        String password = "Password#123";
        String baseVolute = "10000";


        return new Object[][]{
                // username, email, volute, password, repeatPassword, expectedSuccessMessage, scenarioName
                {uniqueUsername, uniqueEmail, baseVolute, password, password, "Usuario Creado Con éxito", "Successful registration"},
                {uniqueExecutionId + "short", uniqueExecutionId + "email8@gmail.com", baseVolute, "password#123", "password#123", "Usuario Creado Con éxito", "Register using a very short username"},
                {uniqueExecutionId + "user9", uniqueExecutionId + "email9@gmail.com", baseVolute, "password#", "password#", "Usuario Creado Con éxito", "Register using a password without numbers"},
                {uniqueExecutionId + "user10", uniqueExecutionId + "email10@gmail.com", baseVolute, "password123", "password123", "Usuario Creado Con éxito", "Register using a password without special characters"},
                {uniqueExecutionId + "user11", uniqueExecutionId + "email11@gmail.com", baseVolute, "123456#", "123456#", "Usuario Creado Con éxito", "Register using a password without letters"},
                {uniqueExecutionId + "user5", uniqueExecutionId + "email5@gmail.com", baseVolute, "paz", "paz", "Usuario Creado Con éxito", "Register using a password with less than 6 digits"}
        };
    }

    /**
     * Prueba el registro exitoso de un nuevo usuario con varios sets de datos válidos/aceptables.
     * Verifica que el mensaje de éxito sea el esperado.
     *
     * @param username El nombre de usuario a registrar.
     * @param email El correo electrónico a registrar.
     * @param volute El valor para el campo 'volute'.
     * @param password La contraseña.
     * @param repeatPassword La repetición de la contraseña.
     * @param expectedSuccessMessage El mensaje de éxito esperado.
     * @param scenarioName La descripción del escenario de prueba.
     */
    @Test(dataProvider = "ValidRegisterData")
    public void testSuccessfulRegistration(String username, String email, String volute, String password, String repeatPassword, String expectedSuccessMessage, String scenarioName) {
        String loginUrl = dotenv.get("BASE_URL");
        signUpPage = new SignUpPage(driver);
        signUpPage.navigateToSignUpPage(loginUrl);
        signUpPage.enterRegisterCredentials(username, email, volute, password, repeatPassword);
        signUpPage.clickSignUpButton();

        String actualSuccessMessage = signUpPage.getSuccessMessageText();

        Assert.assertEquals(actualSuccessMessage, expectedSuccessMessage,
                "Failure in scenario: " + scenarioName + ". The success message is not as expected.");
    }

    //-----------------------------------------------------------------------------------------------------------

    /**
     * Proveedor de datos para escenarios de registro inválido.
     * Incluye pruebas para campos vacíos, contraseñas no coincidentes,
     * y datos ya existentes (username/email).
     *
     * @return Un array de objetos que contiene: datos de registro, mensaje de error esperado,
     * el nombre del método de PageObject para obtener el error, y el nombre del escenario.
     */
    @DataProvider(name = "InvalidRegisterData")
    public Object[][] getInvalidRegisterData() {
        Dotenv dotenvProvider = Dotenv.load();
        String usernameRegistered = dotenvProvider.get("APP_USERNAME");
        String emailRegistered = dotenvProvider.get("EMAIL_REGISTERED");
        String baseVolute = "10000";

        // Genera un ID único para la ejecución.
        String uniqueExecutionId = String.valueOf(Instant.now().getEpochSecond()) + "_" + UUID.randomUUID().toString().substring(0, 8);

        return new Object[][]{
                // username, email, volute, password, repeatPassword, expectedError, errorMessageMethod, scenarioName
                {"", uniqueExecutionId + "email1@gmail.com", baseVolute, "password123#", "password123#", "Nombre de usuario es obligatorio", "getUsernameErrorMessageText", "Register leaving username field empty"},
                {uniqueExecutionId + "user2", "", baseVolute, "password123#", "password123#", "Correo no válido", "getEmailErrorMessageText", "Register leaving email field empty"},
                {uniqueExecutionId + "user3", uniqueExecutionId + "email3@gmail.com", "", "password123#", "password123#", "Todos los campos son requeridos", "getVoluteEmptyFieldMessageText", "Register leaving volute field empty"},
                {uniqueExecutionId + "user4", uniqueExecutionId + "email4@gmail.com", baseVolute, "", "password123#", "Las contraseñas no coinciden", "getPasswordMismatchErrorText", "Register leaving password field empty"},
                {uniqueExecutionId + "user6", uniqueExecutionId + "email6@gmail.com", baseVolute, "password#123", "password#1234", "Las contraseñas no coinciden", "getPasswordMismatchErrorText", "Register without matching passwords"},
                {"usuario" + UUID.randomUUID().toString().replace("-", ""), uniqueExecutionId + "email7@gmail.com", baseVolute, "password#123", "password#123", "Hubo un error interno en el servidor", "getServerInternalErrorMessageText", "Register using a very long username"},
                {uniqueExecutionId + "user12", uniqueExecutionId + "email12@gmail.com", baseVolute, "password", "passwordlo", "Las contraseñas no coinciden", "getPasswordMismatchErrorText", "Register with passwords that don't match"},
                {uniqueExecutionId + "user13", emailRegistered, baseVolute, "password", "password", "Ya hay un jugador registrado con ese email", "getEmailExistsMessageText", "Register with an existing email"},
                {usernameRegistered, uniqueExecutionId + "email14@gmail.com", baseVolute, "password", "password", "Ya hay un jugador con ese nombre", "getUsernameExistsMessageText", "Register with an existing username"},
        };
    }

    /**
     * Prueba escenarios de registro con datos inválidos, verificando que se muestre el mensaje de error correcto.
     * Se utiliza un `switch` para llamar dinámicamente al método del PageObject que obtiene el mensaje de error esperado.
     *
     * @param username El nombre de usuario a probar.
     * @param email El correo electrónico a probar.
     * @param volute El valor para el campo 'volute'.
     * @param password La contraseña a probar.
     * @param repeatPassword La repetición de la contraseña a probar.
     * @param expectedErrorMessage El mensaje de error parcial esperado (se usa contains).
     * @param errorMessageMethod El nombre del método en SignUpPage para obtener el mensaje de error.
     * @param scenarioName La descripción del escenario de prueba.
     */
    @Test(dataProvider = "InvalidRegisterData")
    public void testInvalidRegisterData(String username, String email, String volute, String password, String repeatPassword, String expectedErrorMessage, String errorMessageMethod, String scenarioName) {
        String loginUrl = dotenv.get("BASE_URL");
        signUpPage = new SignUpPage(driver);
        signUpPage.navigateToSignUpPage(loginUrl);
        signUpPage.enterRegisterCredentials(username, email, volute, password, repeatPassword);
        signUpPage.clickSignUpButton();

        // Determina qué método de obtención de mensaje de error llamar dinámicamente
        String actualErrorMessage = switch (errorMessageMethod) {
            case "getUsernameErrorMessageText" -> signUpPage.getUsernameErrorMessageText();
            case "getEmailErrorMessageText" -> signUpPage.getEmailErrorMessageText();
            case "getPasswordMismatchErrorText" -> signUpPage.getPasswordMismatchErrorText();
            case "getUsernameExistsMessageText" -> signUpPage.getUsernameExistsMessageText();
            case "getEmailExistsMessageText" -> signUpPage.getEmailExistsMessageText();
            case "getVoluteEmptyFieldMessageText" -> signUpPage.getVoluteEmptyFieldMessageText();
            case "getServerInternalErrorMessageText" -> signUpPage.getServerInternalErrorMessageText();
            default -> signUpPage.getAllFieldsRequiredMessageText(); // En caso de que el nombre del método no coincida
        };

        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
                "Failure in scenario: " + scenarioName + ". The error message is not as expected.");
    }

    //-----------------------------------------------------------------------------------------------------------

    /**
     * Prueba que el campo de email muestre un mensaje de validación del navegador (tooltip)
     * al ingresar un formato de email inválido.
     * Nota: Esta prueba depende del comportamiento de validación HTML5/navegador.
     */
    @Test
    public void testGetEmailValidationMessage() {
        String loginUrl = dotenv.get("BASE_URL");
        signUpPage = new SignUpPage(driver);
        signUpPage.navigateToSignUpPage(loginUrl);
        // Se ingresan credenciales con un email inválido (sin '@')
        signUpPage.enterRegisterCredentials("usuario", "123gmail.comasdasd", "1000", "password#123", "password#123");

        // Se interactúa con otros campos para forzar el mensaje de validación del navegador.
        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.click();
        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.click();

        // Se obtiene el mensaje de validación del navegador mediante el atributo 'validationMessage'.
        String validationMessage = emailInput.getAttribute("validationMessage");

        Assert.assertNotNull(validationMessage, "El mensaje de validación no debería ser nulo.");
        // Se verifica el contenido del mensaje (traducido del inglés por el navegador).
        Assert.assertTrue(validationMessage.contains("Please include an '@' in the email address."), "El mensaje de validación no es el esperado.");
    }
}