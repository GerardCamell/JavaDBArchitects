package JavaDBArchitects.vista;

import JavaDBArchitects.controlador.Controlador;
import JavaDBArchitects.modelo.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import JavaDBArchitects.controlador.excepciones.ExcursionYaExisteException;

/**
 * Clase que representa el menú principal de la aplicación.
 * Proporciona métodos para gestionar las interacciones del usuario con la lógica de negocio.
 */
public class MenuPrincipal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Método para añadir socios predefinidos al sistema.
     * Este método se invoca al inicio del menú principal para tener algunos datos disponibles.
     */
    private static void añadirSociosPredefinidos() {
        // Crear federaciones predefinidas
        Federacion federacion1 = new Federacion("NACIONAL01", "Federación Nacional");
        Federacion federacion2 = new Federacion("INTERNACIONAL01", "Federación Internacional");

        // Crear seguros predefinidos
        Seguro seguroBasico = new Seguro("Básico", 50.0f);
        Seguro seguroCompleto = new Seguro("Completo", 100.0f);

        // Crear los socios predefinidos
        Estandar socioEstandar1 = new Estandar("1", "Gerard", "40000000c", seguroCompleto);
        Estandar socioEstandar2 = new Estandar("2", "Candela", "5000000d", seguroCompleto);

        Federado socioFederado1 = new Federado("3", "Daniel", "7000000x", federacion1);
        Federado socioFederado2 = new Federado("4", "Bernat", "8000000x", federacion2);

        Infantil socioInfantil1 = new Infantil("5", "Carlos", "1");
        Infantil socioInfantil2 = new Infantil("6", "María", "2");

        // Agregar los socios a la lista de socios
        ListaSocios.addSocio(socioEstandar1);
        ListaSocios.addSocio(socioEstandar2);
        ListaSocios.addSocio(socioFederado1);
        ListaSocios.addSocio(socioFederado2);
        ListaSocios.addSocio(socioInfantil1);
        ListaSocios.addSocio(socioInfantil2);
    }

    /**
     * Método que muestra el menú principal y gestiona la interacción del usuario.
     * Proporciona opciones para agregar excursiones, registrar socios, inscripciones, etc.
     */
    public static void mostrarMenu() {
        int opcion = -1;
        añadirSociosPredefinidos(); // Añadir socios predefinidos al inicio del menú

        while (opcion != 0) {
            // Imprimir opciones del menú
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Añadir Excursión");
            System.out.println("2. Registrar Socio");
            System.out.println("3. Inscribir en Excursión");
            System.out.println("4. Listar Excursiones por Fecha");
            System.out.println("5. Listar Inscripciones");
            System.out.println("6. Consultar Factura Mensual");
            System.out.println("7. Modificar Datos del Socio");
            System.out.println("8. Mostrar Socios por Tipo");
            System.out.println("9. Eliminar Inscripción");
            System.out.println("10. Eliminar Socio");
            System.out.println("11. Mostrar Inscripciones con Filtros");
            System.out.println("12. Eliminar Excursión");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            // Leer la opción seleccionada por el usuario
            opcion = scanner.nextInt();
            scanner.nextLine(); // Capturar el salto de línea

            // Procesar la opción seleccionada
            switch (opcion) {
                case 1:
                    registrarExcursion();
                    break;
                case 2:
                    registrarSocio();
                    break;
                case 3:
                    inscribirEnExcursion();
                    break;
                case 4:
                    listarExcursionesPorFechas();
                    break;
                case 5:
                    listarInscripciones();
                    break;
                case 6:
                    consultarFacturaMensual();
                    break;
                case 7:
                    modificarDatosSocio();
                    break;
                case 8:
                    mostrarSociosPorTipo();
                    break;
                case 9:
                    eliminarInscripcion();
                    break;
                case 10:
                    eliminarSocio();
                    break;
                case 11:
                    mostrarInscripcionesConFiltros();
                    break;
                case 12:
                    eliminarExcursion();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        }
    }

    /**
     * Método para registrar un socio en el sistema.
     */
    private static void registrarSocio() {
        System.out.println("=== Registrar Socio ===");
        System.out.print("Número de Socio: ");
        String numeroSocio = scanner.nextLine();
        System.out.print("Nombre del Socio: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo de Socio (0: Estandar, 1: Federado, 2: Infantil): ");
        int tipoSocio = scanner.nextInt();
        scanner.nextLine();  // Capturar la línea vacía
        System.out.print("NIF: ");
        String NIF = scanner.nextLine();

        Object extra = null;

        // Proporcionar información adicional según el tipo de socio
        if (tipoSocio == 0) {  // Estandar
            System.out.print("Seguro (Básico/Completo): ");
            String tipoSeguro = scanner.nextLine();
            System.out.print("Precio del Seguro: ");
            float precioSeguro = scanner.nextFloat();
            scanner.nextLine();  // Capturar la línea vacía
            extra = new Seguro(tipoSeguro, precioSeguro);
        } else if (tipoSocio == 1) {  // Federado
            System.out.print("Código de la Federación: ");
            String codigoFederacion = scanner.nextLine();
            System.out.print("Nombre de la Federación: ");
            String nombreFederacion = scanner.nextLine();
            extra = new Federacion(codigoFederacion, nombreFederacion);
        }

        // Llamada al controlador para registrar el socio
        Controlador.registrarSocio(numeroSocio, nombre, tipoSocio, NIF, extra);
    }

    /**
     * Método para eliminar un socio del sistema.
     */
    private static void eliminarSocio() {
        System.out.println("=== Eliminar Socio ===");
        System.out.print("Número de Socio: ");
        String numeroSocio = scanner.nextLine();

        // Llamada al controlador para eliminar el socio
        Controlador.eliminarSocio(numeroSocio);
    }

    /**
     * Método para inscribir un socio en una excursión.
     */
    private static void inscribirEnExcursion() {
        System.out.println("=== Inscribir en Excursión ===");
        System.out.print("Número de Socio: ");
        String numeroSocio = scanner.nextLine();
        System.out.print("Código de la Excursión: ");
        String codigoExcursion = scanner.nextLine();
        System.out.print("Fecha de Inscripción (DD/MM/YYYY): ");
        String fechaStr = scanner.nextLine();

        LocalDate fechaInscripcion = LocalDate.parse(fechaStr, formatter);

        // Llamada al controlador para inscribir al socio en la excursión
        Controlador.inscribirEnExcursion(numeroSocio, codigoExcursion, fechaInscripcion);
    }

    /**
     * Método para consultar la factura mensual de un socio.
     */
    private static void consultarFacturaMensual() {
        System.out.println("=== Consultar Factura Mensual ===");
        System.out.print("Número de Socio: ");
        String numeroSocio = scanner.nextLine();

        // Llamada al controlador para consultar la factura mensual
        Controlador.consultarFacturaMensual(numeroSocio);
    }

    /**
     * Método para modificar los datos de un socio.
     */
    private static void modificarDatosSocio() {
        System.out.println("=== Modificar Datos del Socio ===");
        System.out.print("Número de Socio: ");
        String numeroSocio = scanner.nextLine();
        System.out.print("Nuevo nombre del socio: ");
        String nuevoNombre = scanner.nextLine();

        // Llamada al controlador para modificar los datos del socio
        Controlador.modificarDatosSocio(numeroSocio, nuevoNombre);
    }

    /**
     * Método para listar todas las inscripciones.
     */
    private static void listarInscripciones() {
        System.out.println("=== Listar Inscripciones ===");
        // Llamada al controlador para listar inscripciones
        Controlador.listarInscripciones();
    }

    /**
     * Método para eliminar una inscripción de un socio.
     */
    private static void eliminarInscripcion() {
        System.out.println("=== Eliminar Inscripción ===");
        System.out.print("Número de Inscripción: ");
        String numeroInscripcion = scanner.nextLine();

        // Llamada al controlador para eliminar la inscripción
        Controlador.eliminarInscripcion(numeroInscripcion);
    }

    /**
     * Método para registrar una nueva excursión en el sistema.
     */
    private static void registrarExcursion() {
        System.out.println("=== Registrar Excursión ===");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Fecha (DD/MM/YYYY): ");
        String fechaStr = scanner.nextLine();

        // Parsear la fecha con el formateador
        LocalDate fecha = LocalDate.parse(fechaStr, formatter);

        System.out.print("Número de Días: ");
        int numeroDias = scanner.nextInt();
        scanner.nextLine();  // Capturar la línea vacía
        System.out.print("Precio: ");
        String precioStr = scanner.nextLine().replace(',', '.'); // Para que no de error cuando ingresamos "."

        try {
            float precio = Float.parseFloat(precioStr); // Convertir a float
            // Llamada al controlador para registrar la excursión
            Controlador.registrarExcursion(codigo, descripcion, fecha, numeroDias, precio);
        } catch (NumberFormatException e) {
            System.out.println("Error: El precio ingresado no es válido. Asegúrate de usar el formato correcto.");
        } catch (ExcursionYaExisteException e) {
            System.out.println("Error: La excursión ya existe.");
        }
    }


    /**
     * Método para listar excursiones en un rango de fechas.
     */
    private static void listarExcursionesPorFechas() {
        System.out.println("=== Listar Excursiones por Fechas ===");
        System.out.print("Fecha Inicio (DD/MM/YYYY): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Fecha Fin (DD/MM/YYYY): ");
        String fechaFinStr = scanner.nextLine();

        // Parsear las fechas
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

        // Llamada al controlador para mostrar excursiones entre las fechas
        Controlador.mostrarExcursionesEntreFechas(fechaInicio, fechaFin);
    }

    /**
     * Método para mostrar socios filtrados por tipo.
     */
    private static void mostrarSociosPorTipo() {
        System.out.println("=== Mostrar Socios por Tipo ===");
        System.out.print("Tipo de Socio (0: Estandar, 1: Federado, 2: Infantil): ");
        int tipoSocio = scanner.nextInt();
        scanner.nextLine();  // Capturar la línea vacía

        // Llamada al controlador para listar los socios por tipo
        Controlador.listarSocios(tipoSocio);
    }

    /**
     * Método para mostrar inscripciones con filtros aplicados.
     */
    private static void mostrarInscripcionesConFiltros() {
        System.out.println("=== Mostrar Inscripciones con Filtros ===");

        // Pedir el número de socio (puede ser opcional)
        System.out.print("Ingrese el número de socio (o presione Enter para omitir): ");
        String numeroSocio = scanner.nextLine();

        // Pedir la fecha de inicio (puede ser opcional)
        System.out.print("Ingrese la fecha de inicio (DD/MM/YYYY) o presione Enter para omitir: ");
        String fechaInicioStr = scanner.nextLine();
        LocalDate fechaInicio = null;  // Por defecto, null para no filtrar por fecha
        if (!fechaInicioStr.isEmpty()) {
            fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        }

        // Pedir la fecha de fin (puede ser opcional)
        System.out.print("Ingrese la fecha de fin (DD/MM/YYYY) o presione Enter para omitir: ");
        String fechaFinStr = scanner.nextLine();
        LocalDate fechaFin = null;  // Por defecto, null para no filtrar por fecha
        if (!fechaFinStr.isEmpty()) {
            fechaFin = LocalDate.parse(fechaFinStr, formatter);
        }

        // Llamar al controlador con los filtros proporcionados por el usuario
        Controlador.mostrarInscripcionesConFiltros(numeroSocio, fechaInicio, fechaFin);
    }

    /**
     * Método para eliminar una excursión del sistema.
     */
    private static void eliminarExcursion() {
        System.out.println("=== Eliminar Excursión ===");
        System.out.print("Código de la Excursión: ");
        String codigoExcursion = scanner.nextLine();

        // Llamada al controlador y manejo del resultado
        boolean resultado = Controlador.eliminarExcursion(codigoExcursion);
        if (resultado) {
            mostrarMensaje("Excursión eliminada con éxito.");
        } else {
            mostrarMensaje("No se pudo eliminar la excursión.");
        }
    }

    /**
     * Método para mostrar mensajes de éxito en la consola.
     *
     * @param mensaje El mensaje a mostrar
     */
    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Método para mostrar mensajes de error en la consola.
     *
     * @param mensaje El mensaje de error a mostrar
     */
    public static void mostrarError(String mensaje) {
        System.err.println(mensaje);  // Mostrar en la consola de errores
    }
}
