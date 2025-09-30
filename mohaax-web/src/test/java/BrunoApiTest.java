import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class BrunoApiTest {

    /**
     * Test que ejecuta los tests de Bruno CLI para la colección "mohaax".
     * Este método detecta el sistema operativo (Windows o macOS/Linux),
     * arma el comando correspondiente y ejecuta Bruno desde Java.
     *
     * @throws Exception si falla la ejecución del proceso Bruno CLI.
     */
    @Test
    public void runBrunoApiTests() throws Exception {
        System.out.println("Executing Bruno API tests for 'mohaax' collection...");

        // Ruta absoluta por defecto (para macOS/Linux).
        // ⚠️ Si trabajas en Linux/Mac, cámbiala a donde tengas tu colección Bruno.
        String brunoCollectionPath = "/Users/danieleduardo.rojas/Downloads/mohaax";

        // Detectar el sistema operativo actual (Windows, Linux o Mac).
        String os = System.getProperty("os.name").toLowerCase();
        String[] command;

        if (os.contains("win")) {
            // --- Configuración para Windows ---
            // En Windows se usa "cmd.exe" con el modificador "/c" para ejecutar comandos.
            command = new String[]{
                    "cmd.exe", "/c",
                    "bru run . -r --env-file environments\\mohaax.bru"
            };

            // Ruta absoluta a la colección Bruno en tu PC con Windows
            brunoCollectionPath = "C:\\Users\\danie\\OneDrive\\Escritorio\\mohaax-proyect\\mohaax-api";

        } else {
            // --- Configuración para macOS/Linux ---
            // En Unix se usa "/bin/bash" con el modificador "-c".
            command = new String[]{
                    "/bin/bash", "-c",
                    "bru run . -r --env-file environments/mohaax.bru"
            };
        }

        try {
            // Construir el proceso que ejecutará Bruno CLI
            ProcessBuilder builder = new ProcessBuilder(command);

            // Establecer el directorio de trabajo (la carpeta donde está la colección Bruno)
            builder.directory(new File(brunoCollectionPath));

            // Unificar salida estándar y de errores
            builder.redirectErrorStream(true);

            // Iniciar el proceso
            Process process = builder.start();

            // Capturar y mostrar la salida de Bruno CLI en consola
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Esperar a que termine el proceso y obtener el código de salida
            int exitCode = process.waitFor();

            // Si Bruno devuelve un código distinto de 0, se considera fallo
            if (exitCode != 0) {
                throw new RuntimeException("Bruno CLI execution failed with exit code: " + exitCode);
            }

            // Si todo va bien
            System.out.println("Bruno tests finished successfully.");

        } catch (Exception e) {
            // Imprimir la traza del error y volver a lanzarlo
            e.printStackTrace();
            throw e;
        }
    }
}
