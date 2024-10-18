package grupofp;

import grupofp.modelo.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

    public class Main {
        private static List<Excursion> excursiones = new ArrayList<>();
        private static List<Socio> socios = new ArrayList<>();
        private static List<Inscripcion> inscripciones = new ArrayList<>();
        private static Scanner scanner = new Scanner(System.in);

        private static void añadirSociosPredefinidos() {

        // Crear federaciones
        Federacion federacion1 = new Federacion("NACIONAL01", "Federación Nacional");
        Federacion federacion2 = new Federacion("INTERNACIONAL01", "Federación Internacional");

        // Creamos los seguros

        Seguro seguroBasico = new Seguro("Básico", 50.0f);
        Seguro seguroCompleto = new Seguro("Completo", 100.0f);

        //Creamos los socios

        Estandar socioEstandar1 = new Estandar("1", "Gerard", "40000000c", seguroBasico);
        Estandar socioEstandar2 = new Estandar("2","Candela","5000000d", seguroCompleto);

        Federado socioFederado1 = new Federado("3","Daniel", "7000000x",federacion1);
        Federado socioFederado2 = new Federado("4","Bernat", "8000000x",federacion2);

        Infantil socioInfantil1 = new Infantil("5", "Carlos", "1");
        Infantil socioInfantil2 = new Infantil("6", "María", "2");

            socios.add(socioEstandar1);
            socios.add(socioEstandar2);
            socios.add(socioFederado1);
            socios.add(socioFederado2);
            socios.add(socioInfantil1);
            socios.add(socioInfantil2);
        }

        public static void main(String[] args) {
            añadirSociosPredefinidos();

            while (true) {
                mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        gestionExcursiones();
                        break;
                    case 2:
                        gestionSocios();
                        break;
                    case 3:
                        gestionInscripciones();
                        break;
                    case 4:
                        System.out.println("Saliendo...");
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida");
                }
            }
        }

        // Menú Principal
        private static void mostrarMenuPrincipal() {
            System.out.println("Menú Principal:");
            System.out.println("1. Gestión Excursiones");
            System.out.println("2. Gestión Socios");
            System.out.println("3. Gestión Inscripciones");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
        }

        // Gestión de Excursiones
        private static void gestionExcursiones() {
            System.out.println("Gestión de Excursiones:");
            System.out.println("1. Añadir Excursión");
            System.out.println("2. Mostrar Excursiones con Filtro entre Fechas");
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    anadirExcursion();
                    break;
                case 2:
                    mostrarExcursionesConFiltro();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

        // Función para añadir excursión
        private static void anadirExcursion() {
            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();

            System.out.print("Fecha de la excursión (formato: dd/MM/yyyy): ");
            String fechaInput = scanner.nextLine();
            Date fecha = parseFecha(fechaInput);

            System.out.print("Número de días: ");
            int numDias = Integer.parseInt(scanner.nextLine());

            System.out.print("Precio de Inscripción: ");
            float precio = Float.parseFloat(scanner.nextLine());

            Excursion excursion = new Excursion(codigo, descripcion, fecha, numDias, precio);
            excursiones.add(excursion);
            System.out.println("Excursión añadida correctamente.");
        }
        // Método para convertir String a Date
        private static Date parseFecha(String fechaInput) {
            try {
                // Especificar el formato de la fecha
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse(fechaInput);
            } catch (ParseException e) {
                System.out.println("Error al parsear la fecha. Usando la fecha actual.");
                return new Date(); // Retorna la fecha actual en caso de error
            }
        }



        // Función para mostrar excursiones con filtro de fechas
        private static void mostrarExcursionesConFiltro() {
            // Ingresar fechas de filtro
            System.out.print("Ingrese la fecha de inicio (formato: dd/MM/yyyy): ");
            String fechaInicioInput = scanner.nextLine();
            Date fechaInicio = parseFecha(fechaInicioInput);

            System.out.print("Ingrese la fecha de fin (formato: dd/MM/yyyy): ");
            String fechaFinInput = scanner.nextLine();
            Date fechaFin = parseFecha(fechaFinInput);

            // Asegurarse de que la fecha de inicio sea anterior a la fecha de fin
            if (fechaInicio.after(fechaFin)) {
                System.out.println("La fecha de inicio debe ser anterior a la fecha de fin.");
                return;
            }

            System.out.println("Excursiones entre " + fechaInicio + " y " + fechaFin + ":");
            boolean hayExcursiones = false; // Variable para verificar si hay excursiones

            for (Excursion excursion : excursiones) {
                if (!excursion.getFecha().before(fechaInicio) && !excursion.getFecha().after(fechaFin)) {
                    System.out.println(excursion);
                    hayExcursiones = true; // Se encontró al menos una excursión
                }
            }

            if (!hayExcursiones) {
                System.out.println("No se encontraron excursiones en el rango de fechas proporcionado.");
            }
        }

        // Gestión de Socios
        private static void gestionSocios() {
            System.out.println("Gestión de Socios:");
            System.out.println("1. Añadir grupofp.modelo.Socio Estándar");
            System.out.println("2. Modificar Tipo de grupofp.modelo.Seguro de un grupofp.modelo.Socio Estándar");
            System.out.println("3. Añadir grupofp.modelo.Socio grupofp.modelo.Federado");
            System.out.println("4. Añadir grupofp.modelo.Socio grupofp.modelo.Infantil");
            System.out.println("5. Eliminar grupofp.modelo.Socio");
            System.out.println("6. Mostrar Socios (Todos o por Tipo)");
            System.out.println("7. Mostrar Factura Mensual por Socios");
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    anadirSocioEstandar();
                    break;
                case 2:
                    modificarSeguroSocioEstandar();
                    break;
                case 3:
                    anadirSocioFederado();
                    break;
                case 4:
                    anadirSocioInfantil();
                    break;
                case 5:
                    eliminarSocio();
                    break;
                case 6:
                    mostrarSocios();
                    break;
                case 7:
                    mostrarFacturaMensual();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

        // Funciones para cada opción del menú de Gestión de Socios

        private static void anadirSocioEstandar() {
            System.out.print("Número de socio: ");
            String numeroSocio = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("NIF: ");
            String NIF = scanner.nextLine();
            System.out.print("Tipo de seguro (Básico o Completo): ");
            String tipoSeguro = scanner.nextLine();
            System.out.print("Precio del seguro: ");
            float precioSeguro = Float.parseFloat(scanner.nextLine());

            Seguro seguro = new Seguro(tipoSeguro, precioSeguro);
            Estandar estandar = new Estandar(numeroSocio, nombre, NIF, seguro);
            socios.add(estandar);
            System.out.println("grupofp.modelo.Socio estándar añadido correctamente.");
        }

        private static void modificarSeguroSocioEstandar() {
            System.out.print("Ingrese el número de socio: ");
            String numeroSocio = scanner.nextLine();
            Socio socio = buscarSocioPorNumero(numeroSocio);

            if (socio instanceof Estandar) {
                System.out.print("Nuevo tipo de seguro (Básico o Completo): ");
                String tipoSeguro = scanner.nextLine();
                System.out.print("Nuevo precio del seguro: ");
                float precioSeguro = Float.parseFloat(scanner.nextLine());

                Seguro nuevoSeguro = new Seguro(tipoSeguro, precioSeguro);
                ((Estandar) socio).setSeguro(nuevoSeguro);
                System.out.println("grupofp.modelo.Seguro actualizado correctamente.");
            } else {
                System.out.println("El socio no es de tipo estándar.");
            }
        }
        // Función para añadir un socio federado
        private static void anadirSocioFederado() {
            System.out.print("Ingrese el número de socio: ");
            String numeroSocio = scanner.nextLine();
            System.out.print("Ingrese el nombre del socio: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el NIF: ");
            String nif = scanner.nextLine();

            // Datos de la federación
            System.out.print("Ingrese el nombre de la federación: ");
            String nombreFederacion = scanner.nextLine();
            Federacion federacion = new Federacion("F" + numeroSocio, nombreFederacion);

            // Crear el socio federado
            Federado socioFederado = new Federado(numeroSocio, nombre, nif, federacion);
            socios.add(socioFederado);

            System.out.println("grupofp.modelo.Socio grupofp.modelo.Federado añadido correctamente.");
        }
        // Función para añadir un socio infantil
        private static void anadirSocioInfantil() {
            System.out.print("Ingrese el número de socio: ");
            String numeroSocio = scanner.nextLine();
            System.out.print("Ingrese el nombre del socio infantil: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese el número de socio del padre o madre: ");
            String numSocioPadreOMadre = scanner.nextLine();

            // Crear el socio infantil
            Infantil socioInfantil = new Infantil(numeroSocio, nombre, numSocioPadreOMadre);
            socios.add(socioInfantil);

            System.out.println("grupofp.modelo.Socio grupofp.modelo.Infantil añadido correctamente.");
        }
        // Función para eliminar un socio
        private static void eliminarSocio() {
            System.out.print("Ingrese el número de socio a eliminar: ");
            String numeroSocio = scanner.nextLine();

            // Verificar si el socio tiene inscripciones
            boolean tieneInscripciones = false;
            for (Inscripcion inscripcion : inscripciones) {
                if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio)) {
                    tieneInscripciones = true;
                    break;
                }
            }

            if (tieneInscripciones) {
                System.out.println("El socio no se puede eliminar porque tiene inscripciones a excursiones.");
            } else {
                Socio socioAEliminar = null;
                for (Socio socio : socios) {
                    if (socio.getNumeroSocio().equals(numeroSocio)) {
                        socioAEliminar = socio;
                        break;
                    }
                }

                if (socioAEliminar != null) {
                    socios.remove(socioAEliminar);
                    System.out.println("grupofp.modelo.Socio eliminado correctamente.");
                } else {
                    System.out.println("No se encontró un socio con ese número.");
                }
            }
        }
        // Función para mostrar socios
        private static void mostrarSocios() {
            System.out.println("Seleccione el tipo de socios a mostrar: ");
            System.out.println("1. Todos los socios");
            System.out.println("2. Socios Estándar");
            System.out.println("3. Socios Federados");
            System.out.println("4. Socios Infantiles");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese una opción válida.");
                return;
            }

            System.out.println("Lista de socios:");
            boolean haySocios = false; // Para verificar si se mostraron socios

            for (Socio socio : socios) {
                boolean mostrar = false;

                switch (opcion) {
                    case 1:  // Mostrar todos
                        mostrar = true;
                        break;
                    case 2:  // Mostrar solo socios estándar
                        if (socio instanceof Estandar) {
                            mostrar = true;
                        }
                        break;
                    case 3:  // Mostrar solo socios federados
                        if (socio instanceof Federado) {
                            mostrar = true;
                        }
                        break;
                    case 4:  // Mostrar solo socios infantiles
                        if (socio instanceof Infantil) {
                            mostrar = true;
                        }
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        return;
                }

                if (mostrar) {
                    System.out.println(socio.toString()); // Polimorfismo: llama al toString de la subclase correcta
                    haySocios = true;
                }
            }

            if (!haySocios) {
                System.out.println("No se encontraron socios para la opción seleccionada.");
            }
        }

        private static Socio buscarSocioPorNumero(String numeroSocio) {
            for (Socio socio : socios) {
                if (socio.getNumeroSocio().equals(numeroSocio)) {
                    return socio;
                }
            }
            System.out.println("grupofp.modelo.Socio no encontrado.");
            return null;
        }

        // Factura mensual
        private static void mostrarFacturaMensual() {
            System.out.print("Ingrese el número de socio: ");
            String numeroSocio = scanner.nextLine();
            Socio socio = buscarSocioPorNumero(numeroSocio);

            if (socio != null) {
                double cuotaMensual = socio.calcularCuotaMensual();
                double totalExcursiones = 0;

                for (Inscripcion inscripcion : inscripciones) {
                    if (inscripcion.getSocio().equals(socio)) {
                        Excursion excursion = inscripcion.getExcursion();
                        totalExcursiones += calcularPrecioExcursion(socio, excursion);
                    }
                }

                double totalFactura = cuotaMensual + totalExcursiones;
                System.out.println("Factura mensual para " + socio.getNombre() + ": " + totalFactura);
            }
        }

        private static double calcularPrecioExcursion(Socio socio, Excursion excursion) {
            if (socio instanceof Estandar) {
                return excursion.getPrecioInscripcion() + ((Estandar) socio).getSeguro().getPrecio();
            } else if (socio instanceof Federado) {
                return excursion.getPrecioInscripcion() * 0.9; // 10% de descuento
            } else if (socio instanceof Infantil) {
                return excursion.getPrecioInscripcion(); // No hay cargos ni descuentos
            }
            return 0;
        }

        // Gestión de Inscripciones
        private static void gestionInscripciones() {
            System.out.println("Gestión de Inscripciones:");
            System.out.println("1. Añadir Inscripción");
            System.out.println("2. Eliminar Inscripción");
            System.out.println("3. Mostrar Inscripciones con Filtro");
            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    anadirInscripcion();
                    break;
                case 2:
                    eliminarInscripcion();
                    break;
                case 3:
                    mostrarInscripcionesConFiltro();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
        // Función para añadir una inscripción

        private static void anadirInscripcion() {
            System.out.print("Ingrese el código de la excursión: ");
            String codigoExcursion = scanner.nextLine();
            Excursion excursion = buscarExcursionPorCodigo(codigoExcursion);

            if (excursion != null) {
                System.out.print("Ingrese el número de socio (si es nuevo, ingrese 'nuevo'): ");
                String numeroSocio = scanner.nextLine();

                Socio socio;
                if (numeroSocio.equalsIgnoreCase("nuevo")) {
                    socio = anadirNuevoSocio();
                } else {
                    socio = buscarSocioPorNumero(numeroSocio);
                }

                if (socio != null) {
                    // Generar o solicitar el número de inscripción
                    System.out.print("Ingrese el número de inscripción: ");
                    String numInscripcion = scanner.nextLine();

                    // Pedir la fecha de inscripción
                    System.out.print("Ingrese la fecha de inscripción (dd/MM/yyyy): ");
                    String fechaInscripcionStr = scanner.nextLine();
                    Date fechaInscripcion = null;

                    // Convertir la cadena ingresada a objeto Date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false); // Para evitar fechas inválidas

                    try {
                        fechaInscripcion = sdf.parse(fechaInscripcionStr); // Intentar convertir
                    } catch (ParseException e) {
                        System.out.println("Error: La fecha ingresada no es válida. Se usará la fecha actual.");
                        fechaInscripcion = new Date(); // Usar fecha actual en caso de error
                    }

                    // Crear la inscripción con todos los argumentos requeridos
                    Inscripcion inscripcion = new Inscripcion(numInscripcion, socio, excursion, fechaInscripcion);
                    inscripciones.add(inscripcion);

                    System.out.println("Inscripción añadida correctamente.");
                } else {
                    System.out.println("El socio no fue encontrado.");
                }
            } else {
                System.out.println("Excursión no encontrada.");
            }
        }
        // Función para eliminar una inscripción
        private static void eliminarInscripcion() {
            System.out.print("Ingrese el número de socio: ");
            String numeroSocio = scanner.nextLine();
            System.out.print("Ingrese el código de la excursión: ");
            String codigoExcursion = scanner.nextLine();

            Inscripcion inscripcionAEliminar = null;
            for (Inscripcion inscripcion : inscripciones) {
                if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio)
                        && inscripcion.getExcursion().getCodigo().equals(codigoExcursion)) {
                    inscripcionAEliminar = inscripcion;
                    break;
                }
            }

            if (inscripcionAEliminar != null) {
                inscripciones.remove(inscripcionAEliminar);
                System.out.println("Inscripción eliminada correctamente.");
            } else {
                System.out.println("No se encontró la inscripción para el socio y excursión proporcionados.");
            }
        }
        // Función para mostrar inscripciones con filtro (por socio y/o por fechas)
        private static void mostrarInscripcionesConFiltro() {
            System.out.print("¿Desea filtrar por número de socio? (s/n): ");
            String filtrarPorSocio = scanner.nextLine();
            String numeroSocio = null;

            if (filtrarPorSocio.equalsIgnoreCase("s")) {
                System.out.print("Ingrese el número de socio: ");
                numeroSocio = scanner.nextLine();
            }

            System.out.print("¿Desea filtrar por fechas? (s/n): ");
            String filtrarPorFechas = scanner.nextLine();
            Date fechaInicio = null;
            Date fechaFin = null;

            if (filtrarPorFechas.equalsIgnoreCase("s")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false); // Para evitar fechas inválidas

                // Pedir la fecha de inicio
                System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
                String fechaInicioStr = scanner.nextLine();
                try {
                    fechaInicio = sdf.parse(fechaInicioStr);
                } catch (ParseException e) {
                    System.out.println("Error: La fecha de inicio ingresada no es válida. Se usará una fecha por defecto (null).");
                    // Si es necesario, puedes establecer una fecha por defecto aquí
                }

                // Pedir la fecha de fin
                System.out.print("Ingrese la fecha de fin (dd/MM/yyyy): ");
                String fechaFinStr = scanner.nextLine();
                try {
                    fechaFin = sdf.parse(fechaFinStr);
                } catch (ParseException e) {
                    System.out.println("Error: La fecha de fin ingresada no es válida. Se usará una fecha por defecto (null).");

                }
            }
        }
        // Función para buscar excursión por su código
        private static Excursion buscarExcursionPorCodigo(String codigo) {
            for (Excursion excursion : excursiones) {
                if (excursion.getCodigo().equals(codigo)) {
                    return excursion;
                }
            }
            System.out.println("Excursión no encontrada.");
            return null;
        }
        // Función para añadir un nuevo socio (según su tipo)
        private static Socio anadirNuevoSocio() {
            System.out.println("Añadir nuevo socio:");
            System.out.println("Seleccione el tipo de socio: ");
            System.out.println("1. grupofp.modelo.Socio Estándar");
            System.out.println("2. grupofp.modelo.Socio grupofp.modelo.Federado");
            System.out.println("3. grupofp.modelo.Socio grupofp.modelo.Infantil");

            int tipoSocio = Integer.parseInt(scanner.nextLine());
            switch (tipoSocio) {
                case 1:
                    anadirSocioEstandar();
                    break;
                case 2:
                    anadirSocioFederado();
                    break;
                case 3:
                    anadirSocioInfantil();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            // Devuelve el socio recién añadido
            return socios.get(socios.size() - 1); // Último socio añadido
        }
    }