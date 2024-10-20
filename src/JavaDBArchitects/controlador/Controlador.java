package JavaDBArchitects.controlador;

import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.modelo.*;
import JavaDBArchitects.vista.MenuPrincipal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase Controlador que gestiona la lógica de negocio para la aplicación.
 * Se encarga de interactuar entre las clases del modelo y la vista, procesando
 * la lógica de la aplicación.
 */
public class Controlador {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Método para registrar un nuevo socio.
     * Maneja la creación de un socio basado en los parámetros proporcionados y verifica
     * que el socio no exista previamente.
     *
     * Excepciones utilizadas:
     * - SocioYaExisteException: si el socio ya está registrado.
     * - SeguroInvalidoException: si se proporciona un seguro no válido para socios estándar.
     * - TipoSocioInvalidoException: si se especifica un tipo de socio no permitido.
     *
     * @param numeroSocio El número del socio.
     * @param nombre El nombre del socio.
     * @param tipoSocio El tipo de socio (0: Estandar, 1: Federado, 2: Infantil).
     * @param NIF El NIF del socio.
     * @param extra Información extra (como seguro o federación, dependiendo del tipo de socio).
     */
    public static void registrarSocio(String numeroSocio, String nombre, int tipoSocio, String NIF, Object extra) {
        try {
            List<Object> parametros = new ArrayList<>();
            parametros.add(tipoSocio);   // 0 = Estandar, 1 = Federado, 2 = Infantil
            parametros.add(numeroSocio);
            parametros.add(nombre);
            parametros.add(NIF);
            parametros.add(extra);  // Esto puede ser un seguro o una federación, dependiendo del tipo

            Datos.registrarSocio(parametros);
            MenuPrincipal.mostrarMensaje("Socio registrado con éxito.");
        } catch (SocioYaExisteException e) {
            // Lanza esta excepción si ya existe un socio con ese número.
            MenuPrincipal.mostrarError("Error: El socio ya existe.");
        } catch (SeguroInvalidoException e) {
            // Lanza esta excepción si el seguro es inválido para socios estándar.
            MenuPrincipal.mostrarError("Error: El seguro proporcionado no es válido.");
        } catch (TipoSocioInvalidoException e) {
            // Lanza esta excepción si se especifica un tipo de socio no permitido.
            MenuPrincipal.mostrarError("Error: El tipo de socio proporcionado no es válido.");
        }
    }

    /**
     * Método para eliminar un socio.
     * Elimina el socio si no tiene inscripciones activas.
     *
     * Excepciones utilizadas:
     * - SocioNoExisteException: si el socio no existe.
     * - SocioConInscripcionesException: si el socio tiene inscripciones activas y no se puede eliminar.
     *
     * @param numeroSocio El número del socio a eliminar.
     */
    public static void eliminarSocio(String numeroSocio) {
        try {
            Datos.eliminarSocio(numeroSocio);
            MenuPrincipal.mostrarMensaje("Socio eliminado con éxito.");
        } catch (SocioNoExisteException e) {
            // Lanza esta excepción si el socio no está registrado.
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        } catch (SocioConInscripcionesException e) {
            // Lanza esta excepción si el socio tiene inscripciones activas.
            MenuPrincipal.mostrarError("Error: El socio tiene inscripciones activas y no puede ser eliminado.");
        }
    }

    /**
     * Método para registrar una excursión.
     * Registra una excursión nueva, validando que no exista previamente.
     *
     * Excepciones utilizadas:
     * - ExcursionYaExisteException: si la excursión ya está registrada.
     *
     * @param codigo El código de la excursión.
     * @param descripcion La descripción de la excursión.
     * @param fecha La fecha de inicio de la excursión.
     * @param numeroDias El número de días que dura la excursión.
     * @param precio El precio de la inscripción a la excursión.
     */
    public static void registrarExcursion(String codigo, String descripcion, LocalDate fecha, int numeroDias, float precio) throws ExcursionYaExisteException {
        List<Object> parametros = new ArrayList<>();
        parametros.add(codigo);
        parametros.add(descripcion);
        parametros.add(java.sql.Date.valueOf(fecha)); // Convertir LocalDate a Date
        parametros.add(numeroDias);
        parametros.add(precio);

        // Llamar a registrarExcursion en Datos y propagar la excepción si ocurre
        Datos.registrarExcursion(parametros);
        MenuPrincipal.mostrarMensaje("Excursión registrada con éxito.");
    }


    /**
     * Método para eliminar una excursión.
     * Elimina la excursión si no tiene inscripciones activas.
     *
     * Excepciones utilizadas:
     * - ExcursionNoExisteException: si la excursión no existe.
     * - EliminarExcursionConInscripcionesException: si la excursión tiene inscripciones activas.
     *
     * @param codigoExcursion El código de la excursión a eliminar.
     * @return true si la excursión fue eliminada correctamente, false si hubo algún problema.
     */
    public static boolean eliminarExcursion(String codigoExcursion) {
        try {
            return Datos.eliminarExcursion(codigoExcursion);
        } catch (ExcursionNoExisteException e) {
            // Lanza esta excepción si la excursión no está registrada.
            MenuPrincipal.mostrarError("Error: La excursión no existe.");
            return false;
        } catch (EliminarExcursionConInscripcionesException e) {
            // Lanza esta excepción si la excursión tiene inscripciones activas.
            MenuPrincipal.mostrarError("Error: La excursión tiene inscripciones activas y no puede ser eliminada.");
            return false;
        }
    }

    /**
     * Método para inscribir un socio en una excursión.
     * Valida que el socio y la excursión existan, y que la fecha de inscripción sea válida.
     *
     * Excepciones utilizadas:
     * - InscripcionYaExisteException: si el socio ya está inscrito en la excursión.
     * - SocioNoExisteException: si el socio no está registrado.
     * - ExcursionNoExisteException: si la excursión no está registrada.
     * - FechaInvalidaException: si la fecha de inscripción es posterior a la fecha de la excursión.
     *
     * @param numeroSocio El número del socio.
     * @param codigoExcursion El código de la excursión.
     * @param fechaInscripcion La fecha en la que se realiza la inscripción.
     */
    public static void inscribirEnExcursion(String numeroSocio, String codigoExcursion, LocalDate fechaInscripcion) {
        try {
            List<Object> parametros = new ArrayList<>();
            parametros.add(numeroSocio);
            parametros.add(codigoExcursion);
            parametros.add(fechaInscripcion);  // Aquí estamos pasando LocalDate directamente

            Datos.registrarInscripcion(parametros);
            MenuPrincipal.mostrarMensaje("Inscripción realizada con éxito.");
        } catch (InscripcionYaExisteException e) {
            // Lanza esta excepción si el socio ya está inscrito en la excursión.
            MenuPrincipal.mostrarError("Error: Ya estás inscrito en esta excursión.");
        } catch (SocioNoExisteException e) {
            // Lanza esta excepción si el socio no existe.
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        } catch (ExcursionNoExisteException e) {
            // Lanza esta excepción si la excursión no existe.
            MenuPrincipal.mostrarError("Error: La excursión no existe.");
        } catch (FechaInvalidaException e) {
            // Lanza esta excepción si la fecha de inscripción es inválida (posterior a la excursión).
            MenuPrincipal.mostrarError("Error: La fecha de inscripción no es válida.");
        }
    }

    /**
     * Método para eliminar una inscripción.
     * Elimina la inscripción de un socio en una excursión si la excursión aún no ha finalizado.
     *
     * Excepciones utilizadas:
     * - InscripcionNoExisteException: si la inscripción no está registrada.
     * - CancelacionInvalidaException: si se intenta eliminar una inscripción de una excursión ya realizada.
     *
     * @param numeroInscripcion El número de la inscripción a eliminar.
     */
    public static void eliminarInscripcion(String numeroInscripcion) {
        try {
            Datos.eliminarInscripcion(numeroInscripcion);
            MenuPrincipal.mostrarMensaje("Inscripción eliminada con éxito.");
        } catch (InscripcionNoExisteException e) {
            // Lanza esta excepción si la inscripción no existe.
            MenuPrincipal.mostrarError("Error: La inscripción no existe.");
        } catch (CancelacionInvalidaException e) {
            // Lanza esta excepción si la excursión ya ha finalizado y no se puede eliminar la inscripción.
            MenuPrincipal.mostrarError(e.getMessage());
        }
    }


    /**
     * Método para listar todas las inscripciones.
     * Muestra las inscripciones activas con las fechas correspondientes, calculando la fecha
     * de fin si la excursión dura más de un día.
     */
    public static void listarInscripciones() {
        List<Inscripcion> inscripciones = Datos.listarInscripciones();
        for (Inscripcion inscripcion : inscripciones) {
            java.sql.Date sqlDate = (java.sql.Date) inscripcion.getExcursion().getFecha(); // Conversión a sql.Date
            LocalDate fechaExcursion = sqlDate.toLocalDate(); // Convertir a LocalDate
            LocalDate fechaFinExcursion = inscripcion.getExcursion().calcularFechaFin();
            int nDias = inscripcion.getExcursion().getNumDias();

            // Mostrar la inscripción formateando la fecha
            if (nDias > 1) {
                MenuPrincipal.mostrarMensaje(inscripcion + "\n" + "\n-- Las Fechas de la Excursión --\n" +
                        "Fecha de INICIO de la Excursión: " + fechaExcursion.format(FORMATO_FECHA) + "\n" +
                        "Fecha FINAL de la Excursión: " + fechaFinExcursion.format(FORMATO_FECHA) + "\n");
            } else {
                MenuPrincipal.mostrarMensaje(inscripcion + "\n Fecha de la Excursión: " +
                        fechaExcursion.format(FORMATO_FECHA));
            }
        }
    }

    /**
     * Método para mostrar inscripciones filtradas por número de socio y fechas.
     * Permite al usuario buscar inscripciones de acuerdo a filtros aplicados.
     *
     * @param numeroSocio El número de socio (puede ser nulo para omitir el filtro).
     * @param fechaInicio Fecha de inicio para filtrar (puede ser nulo para omitir el filtro).
     * @param fechaFin Fecha de fin para filtrar (puede ser nulo para omitir el filtro).
     */
    public static void mostrarInscripcionesConFiltros(String numeroSocio, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Inscripcion> inscripciones = Datos.listarInscripciones();

        if (numeroSocio != null && !numeroSocio.isEmpty()) {
            inscripciones = inscripciones.stream()
                    .filter(inscripcion -> inscripcion.getSocio().getNumeroSocio().equals(numeroSocio))
                    .collect(Collectors.toList());
        }

        if (fechaInicio != null && fechaFin != null) {
            inscripciones = inscripciones.stream()
                    .filter(inscripcion -> {
                        java.sql.Date sqlDate = (java.sql.Date) inscripcion.getExcursion().getFecha();
                        LocalDate fechaExcursion = sqlDate.toLocalDate();
                        return !fechaExcursion.isBefore(fechaInicio) && !fechaExcursion.isAfter(fechaFin);
                    })
                    .collect(Collectors.toList());
        }

        if (inscripciones.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron inscripciones con los filtros aplicados.");
        } else {
            for (Inscripcion inscripcion : inscripciones) {
                java.sql.Date sqlDate = (java.sql.Date) inscripcion.getExcursion().getFecha();
                LocalDate fechaExcursion = sqlDate.toLocalDate();
                MenuPrincipal.mostrarMensaje("Inscripción: " + inscripcion + " - Fecha: " +
                        fechaExcursion.format(FORMATO_FECHA));
            }
        }
    }

    /**
     * Método para consultar la factura mensual (simulado).
     * Se muestra una factura predefinida para un socio.
     *
     * @param numeroSocio El número de socio cuya factura se va a consultar.
     */
    public static void consultarFacturaMensual(String numeroSocio) {
        try {
            MenuPrincipal.mostrarMensaje("Factura mensual para el socio " + numeroSocio + ": 100€");
        } catch (Exception e) {
            MenuPrincipal.mostrarError("Error al consultar la factura mensual.");
        }
    }

    /**
     * Método para modificar los datos de un socio.
     * Permite cambiar el nombre del socio.
     *
     * Excepciones utilizadas:
     * - SocioNoExisteException: si el socio no existe.
     *
     * @param numeroSocio El número del socio.
     * @param nuevoNombre El nuevo nombre del socio.
     */
    public static void modificarDatosSocio(String numeroSocio, String nuevoNombre) {
        try {
            Socio socio = ListaSocios.getSocio(numeroSocio);
            if (socio == null) {
                throw new SocioNoExisteException("El socio no existe.");
            }
            socio.setNombre(nuevoNombre);
            MenuPrincipal.mostrarMensaje("Datos del socio actualizados con éxito.");
        } catch (SocioNoExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        }
    }

    /**
     * Método para mostrar excursiones entre dos fechas.
     * Permite al usuario ver todas las excursiones que se realizarán entre dos fechas específicas.
     *
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin Fecha de fin del rango.
     */
    public static void mostrarExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursiones = Datos.obtenerExcursionesEntreFechas(fechaInicio, fechaFin);
        if (excursiones.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron excursiones entre las fechas proporcionadas.");
        } else {
            for (Excursion excursion : excursiones) {
                LocalDate fechaExcursion = excursion.getFechaAsLocalDate();
                LocalDate fechaFinExcursion = excursion.calcularFechaFin();

                if (excursion.getNumDias() > 1) {
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion + "\n" +
                            "Fecha Fin de Excursión: " + fechaFinExcursion.format(FORMATO_FECHA));
                } else {
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion);
                }
            }
        }
    }

    /**
     * Método para listar todos los socios o filtrar por tipo.
     * Permite al usuario ver los socios agrupados por su tipo (Estandar, Federado, Infantil).
     *
     * @param tipoSocio El tipo de socio por el cual filtrar (0: Estandar, 1: Federado, 2: Infantil).
     */
    public static void listarSocios(int tipoSocio) {
        List<Socio> socios = Datos.listarSocios(tipoSocio);
        if (socios.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron socios.");
        } else {
            for (Socio socio : socios) {
                MenuPrincipal.mostrarMensaje(socio.toString());
            }
        }
    }
}

