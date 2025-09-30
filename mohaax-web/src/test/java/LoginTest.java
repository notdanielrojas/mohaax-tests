import io.github.cdimascio.dotenv.Dotenv;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.Duration;

/**
 * Clase de pruebas automatizadas para la funcionalidad de Inicio de Sesión (Login).
 * Extiende {@link BaseTest} para heredar la configuración del WebDriver.
 */
public class LoginTest extends BaseTest {

    /**
     * Proveedor de datos para escenarios de inicio de sesión inválido.
     * Lee las credenciales y URLs de la aplicación desde el archivo .env.
     *
     * @return Un array de objetos que contiene: username, password y nombre del escenario.
     */
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        Dotenv dotenvProvider = Dotenv.load();
        return new Object[][] {
                {dotenvProvider.get("APP_USERNAME"), "notuser", "Login with valid username and wrong password"},
                {"", dotenvProvider.get("APP_PASSWORD"), "Login with valid password and username field empty"},
                {dotenvProvider.get("APP_USERNAME"), "", "Login with valid username and password field empty"},
                {"invalidemail.com", dotenvProvider.get("APP_PASSWORD"), "Login with wrong email format"},
                {"notregistered@gmail.com", "nosoyusuario", "Login with unregistered user"}
        };
    }

    /**
     * Prueba el inicio de sesión con datos inválidos (parametrizada).
     * Verifica que el mensaje de error del sistema contenga el texto "email o Password Incorrecto".
     *
     * @param username El email o nombre de usuario a probar.
     * @param password La contraseña a probar.
     * @param scenarioName La descripción del escenario de prueba.
     */
    @Test(dataProvider = "invalidLoginData")
    public void testInvalidLogin(String username, String password, String scenarioName) {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        loginPage.enterLoginCredentials(username, password);
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.getErrorMessageText().contains("email o Password Incorrecto"),
                "Failure in scenario: " + scenarioName + ". The error message is not as expected.");
    }

    /**
     * Prueba el inicio de sesión exitoso con credenciales válidas.
     * Verifica que el mensaje de éxito sea "Sesión iniciada correctamente".
     */
    @Test
    public void testSuccessfulLogin() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        loginPage.enterLoginCredentials(dotenv.get("APP_USERNAME"), dotenv.get("APP_PASSWORD"));
        loginPage.clickLoginButton();

        Assert.assertEquals(loginPage.getSuccessMessageText(), "Sesión iniciada correctamente",
                "The success message is not as expected.");
    }

    /**
     * Prueba el inicio de sesión con un usuario que aún no ha verificado su cuenta.
     * Verifica el mensaje de error específico: "Debes validar tu cuenta para iniciar sesión".
     */
    @Test
    public void testLoginWithUnverifiedUser() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        loginPage.enterLoginCredentials(dotenv.get("USERNAME_NOT_VERIFIED"), dotenv.get("PASSWORD_NOT_VERIFIED"));
        loginPage.clickLoginButton();
        Assert.assertEquals(loginPage.getUnverifiedUserMessageText(), "Debes validar tu cuenta para iniciar sesión", "The verified message is not as expected.");
    }

    /**
     * Prueba la funcionalidad de alternar la visibilidad de la contraseña.
     * 1. Verifica que el campo inicie como tipo "password".
     * 2. Hace clic en el ícono de toggle.
     * 3. Verifica que el campo cambie a tipo "text".
     */
    @Test
    public void testPasswordToggle() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        Assert.assertEquals(loginPage.getPasswordInputType(), "password", "Initial input type should be 'password'.");
        loginPage.togglePasswordVisibility(); // Acción de click al icono
        Assert.assertEquals(loginPage.getPasswordInputType(), "text", "Input type did not change to 'text' after clicking the icon.");
    }

    /**
     * Prueba la respuesta del sistema después de 3 intentos fallidos de inicio de sesión.
     * Se verifica que el mensaje de error permanezca "email o Password Incorrecto".
     */
    @Test
    public void testMultipleInvalidLoginAttempts() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);

        // Se realizan 3 intentos de login fallidos
        for (int i = 0; i < 3; i++) {
            loginPage.enterLoginCredentials(dotenv.get("APP_USERNAME"), "wrongpassword" + i);
            loginPage.clickLoginButton();
        }

        Assert.assertEquals(loginPage.getErrorMessageText(), "email o Password Incorrecto", "El sistema debería mostrar el mismo mensaje de error después de 3 intentos fallidos.");
    }

    /**
     * Prueba que se puede pegar la contraseña en el campo de contraseña usando el atajo de teclado (Command+V).
     * 1. Copia la contraseña al portapapeles.
     * 2. Simula el pegado en el campo.
     * 3. Verifica que el valor del campo sea el de la contraseña copiada.
     */
    @Test
    public void testCopyPastePassword() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        String testPassword = dotenv.get("APP_PASSWORD");
        copyToClipboard(testPassword); // Copia al portapapeles
        loginPage.pastePasswordUsingShortcut(); // Pega usando atajo
        Assert.assertEquals(loginPage.getPasswordInputValue(), testPassword, "The password was not pasted correctly.");
    }

    /**
     * Prueba que al hacer clic en el enlace de registro, la URL actual contiene el path de registro esperado.
     */
    @Test
    public void testSignUpUrl() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        loginPage.clickSignUpLink();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Espera explícita a que la URL cambie para incluir el path de registro.
        wait.until(ExpectedConditions.urlContains(dotenv.get("REGISTER_PATH")));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(dotenv.get("REGISTER_PATH")), "The registration URL is not as expected.");
    }

    /**
     * Prueba que al hacer clic en el enlace de contraseña olvidada, la URL actual contiene el path de recuperación esperado.
     * **Nota:** Esta prueba falló en el reporte (TimeoutException), podría deberse a un error tipográfico en la URL esperada del test.
     */
    @Test
    public void testForgottenPasswordUrl() {
        String loginUrl = dotenv.get("BASE_URL");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage(loginUrl);
        loginPage.clickForgotPasswordLink();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Espera explícita a que la URL cambie para incluir el path de recuperación.
        wait.until(ExpectedConditions.urlContains(dotenv.get("RECOVER_PASSWORD_PATH")));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(dotenv.get("RECOVER_PASSWORD_PATH")), "The registration URL is not as expected.");
    }

    /**
     * Método auxiliar para copiar una cadena de texto al portapapeles del sistema operativo.
     * Requiere la API de AWT y permisos de sistema.
     *
     * @param text La cadena de texto a copiar.
     */
    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}