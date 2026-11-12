/*
 * Autores:
 *[Luis David Pardey Cardozo]
 *
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Scanner;

public class EncriptadorRecursivo {
    private static final int CLAVE = 23;
    private static final Path ARCHIVO = Path.of("frase_encriptada.txt");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String opcion;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Leer y encriptar una frase");
            System.out.println("2. Desencriptar frase del archivo");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    System.out.print("Ingrese una frase: ");
                    String frase = scanner.nextLine();
                    try {
                        String cifrada = encriptar(frase);
                        Files.writeString(ARCHIVO, cifrada, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("Frase encriptada y guardada en " + ARCHIVO);
                    } catch (IOException e) {
                        System.out.println("Error al guardar el archivo: " + e.getMessage());
                    }
                    break;

                case "2":
                    if (!Files.exists(ARCHIVO)) {
                        System.out.println("No hay una frase guardada todavia.");
                        break;
                    }
                    try {
                        String cifrada = Files.readString(ARCHIVO, StandardCharsets.UTF_8);
                        System.out.println("Frase desencriptada: " + desencriptar(cifrada));
                    } catch (IOException | IllegalArgumentException e) {
                        System.out.println("Error al leer o desencriptar: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (!opcion.equals("3"));

        scanner.close();
    }

    // Convierte la frase a bytes y los encripta mediante una funcion recursiva.
    public static String encriptar(String frase) {
        byte[] datos = frase.getBytes(StandardCharsets.UTF_8);
        encriptarRecursivo(datos, 0);
        return Base64.getEncoder().encodeToString(datos);
    }

    private static void encriptarRecursivo(byte[] datos, int indice) {
        if (indice == datos.length) {
            return; // Caso base: no quedan bytes por procesar.
        }
        datos[indice] = (byte) (datos[indice] ^ CLAVE);
        encriptarRecursivo(datos, indice + 1); // Llamada recursiva.
    }

    // Recupera los bytes originales usando la misma clave.
    public static String desencriptar(String contenido) {
        byte[] datos = Base64.getDecoder().decode(contenido);
        desencriptarRecursivo(datos, 0);
        return new String(datos, StandardCharsets.UTF_8);
    }

    private static void desencriptarRecursivo(byte[] datos, int indice) {
        if (indice == datos.length) {
            return; // Caso base.
        }
        datos[indice] = (byte) (datos[indice] ^ CLAVE);
        desencriptarRecursivo(datos, indice + 1); // Llamada recursiva.
    }
}
