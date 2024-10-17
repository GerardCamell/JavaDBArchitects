
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

    public class Main {
        private static List<Excursion> excursiones = new ArrayList<>();
        private static List<Socio> socios = new ArrayList<>();
        private static List<Inscripcion> inscripciones = new ArrayList<>();
        private static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) {
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
            System.out.print("Fecha (en formato largo como timestamp o manual): ");
            Date fecha = new Date(); // Aquí puedes mejorar la entrada de fecha
            System.out.print("Número de días: ");
            int numDias = Integer.parseInt(scanner.nextLine());
            System.out.print("Precio de Inscripción: ");
            float precio = Float.parseFloat(scanner.nextLine());

            Excursion excursion = new Excursion(codigo, descripcion, fecha, numDias, precio);
            excursiones.add(excursion);
            System.out.println("Excursión añadida correctamente.");
        }

        // Función para mostrar excursiones con filtro de fechas
        private static void mostrarExcursionesConFiltro() {
            // Ingresar fechas de filtro
            System.out.print("Ingrese la fecha de inicio: ");
            Date fechaInicio = new Date(); // Aquí puedes mejorar la entrada de fecha
            System.out.print("Ingrese la fecha de fin: ");
            Date fechaFin = new Date(); // Aquí puedes mejorar la entrada de fecha

            System.out.println("Excursiones entre " + fechaInicio + " y " + fechaFin);
            for (Excursion excursion : excursiones) {
                if (excursion.getFecha().after(fechaInicio) && excursion.getFecha().before(fechaFin)) {
                    System.out.println(excursion);
                }
            }
        }

        // Gestión de Socios
        private static void gestionSocios() {
            System.out.println("Gestión de Socios:");
            System.out.println("1. Añadir Socio Estándar");
            System.out.println("2. Modificar Tipo de Seguro de un Socio Estándar");
            System.out.println("3. Añadir Socio Federado");
            System.out.println("4. Añadir Socio Infantil");
            System.out.println("5. Eliminar Socio");
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
            System.out.println("Socio estándar añadido correctamente.");
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
                System.out.println("Seguro actualizado correctamente.");
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

            System.out.println("Socio Federado añadido correctamente.");
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

            System.out.println("Socio Infantil añadido correctamente.");
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
                    System.out.println("Socio eliminado correctamente.");
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

            int opcion = Integer.parseInt(scanner.nextLine());

            System.out.println("Lista de socios:");
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
                    System.out.println(socio.toString());
                }
            }
        }

        private static Socio buscarSocioPorNumero(String numeroSocio) {
            for (Socio socio : socios) {
                if (socio.getNumeroSocio().equals(numeroSocio)) {
                    return socio;
                }
            }
            System.out.println("Socio no encontrado.");
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
                    System.out.print("Ingrese la fecha de inscripción (por ahora usaremos la fecha actual): ");
                    Date fechaInscripcion = new Date(); // Puedes cambiar la lógica para solicitar una fecha real

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
                System.out.print("Ingrese la fecha de inicio: ");
                fechaInicio = new Date(); // Aquí puedes mejorar la entrada de fechas
                System.out.print("Ingrese la fecha de fin: ");
                fechaFin = new Date(); // Aquí puedes mejorar la entrada de fechas
            }

            System.out.println("Inscripciones encontradas:");
            for (Inscripcion inscripcion : inscripciones) {
                boolean cumpleFiltroSocio = (numeroSocio == null || inscripcion.getSocio().getNumeroSocio().equals(numeroSocio));
                boolean cumpleFiltroFechas = (fechaInicio == null || (inscripcion.getExcursion().getFecha().after(fechaInicio)
                        && inscripcion.getExcursion().getFecha().before(fechaFin)));

                if (cumpleFiltroSocio && cumpleFiltroFechas) {
                    System.out.println("Socio: " + inscripcion.getSocio().getNombre()
                            + " | Excursión: " + inscripcion.getExcursion().getDescripcion()
                            + " | Fecha: " + inscripcion.getExcursion().getFecha()
                            + " | Importe: " + calcularPrecioExcursion(inscripcion.getSocio(), inscripcion.getExcursion()));
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
            System.out.println("1. Socio Estándar");
            System.out.println("2. Socio Federado");
            System.out.println("3. Socio Infantil");

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