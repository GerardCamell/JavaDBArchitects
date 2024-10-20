package JavaDBArchitects.modelo;

import JavaDBArchitects.controlador.excepciones.*;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;

public class Datos {

    // ------------------------------
    // Métodos de gestión de Socios
    // ------------------------------

    /**
     * Registra un nuevo socio en la lista de socios.
     *
     * Excepciones:
     * - SocioYaExisteException: Se lanza si el número de socio ya está registrado.
     * - SeguroInvalidoException: Se lanza si un socio estándar no tiene seguro.
     * - TipoSocioInvalidoException: Se lanza si el tipo de socio no es válido.
     *
     * @param parametros Lista de parámetros necesarios para crear el socio.
     * @return true si el socio se ha registrado con éxito.
     */
    public static boolean registrarSocio(List<Object> parametros) throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        Socio socio = null;
        int tipoSocio = (Integer) parametros.get(0);

        // Creación del socio según el tipo
        if (tipoSocio == 0) {
            String numeroSocio = parametros.get(1).toString();
            String nombre = parametros.get(2).toString();
            String NIF = parametros.get(3).toString();
            Seguro seguro = (Seguro) parametros.get(4);

            if (seguro == null) {
                throw new SeguroInvalidoException("El seguro es obligatorio para los socios estándar.");
            }

            socio = new Estandar(numeroSocio, nombre, NIF, seguro);

        } else if (tipoSocio == 1) {
            String numeroSocio = parametros.get(1).toString();
            String nombre = parametros.get(2).toString();
            String NIF = parametros.get(3).toString();
            Federacion federacion = (Federacion) parametros.get(4);

            socio = new Federado(numeroSocio, nombre, NIF, federacion);

        } else if (tipoSocio == 2) {
            String numeroSocio = parametros.get(1).toString();
            String nombre = parametros.get(2).toString();
            String numSocioPadreOMadre = parametros.get(3).toString();

            socio = new Infantil(numeroSocio, nombre, numSocioPadreOMadre);

        } else {
            throw new TipoSocioInvalidoException("El tipo de socio es inválido.");
        }

        // Verifica si el socio ya existe en la lista
        if (ListaSocios.socioExiste(socio.getNumeroSocio())) {
            throw new SocioYaExisteException("El socio con número " + socio.getNumeroSocio() + " ya existe.");
        }

        ListaSocios.addSocio(socio);
        return true;
    }

    /**
     * Elimina un socio de la lista.
     *
     * Excepciones:
     * - SocioNoExisteException: Se lanza si el socio no existe.
     * - SocioConInscripcionesException: Se lanza si el socio tiene inscripciones activas.
     *
     * @param numeroSocio Número del socio a eliminar.
     * @return true si el socio se ha eliminado con éxito.
     */
    public static boolean eliminarSocio(String numeroSocio) throws SocioNoExisteException, SocioConInscripcionesException {
        Socio socio = ListaSocios.getSocio(numeroSocio);

        if (socio == null) {
            throw new SocioNoExisteException("El socio con número " + numeroSocio + " no existe.");
        }

        if (ListaInscripciones.socioTieneInscripciones(numeroSocio)) {
            throw new SocioConInscripcionesException("El socio no puede ser eliminado.");
        }

        return ListaSocios.eliminarSocio(numeroSocio);
    }

    /**
     * Lista los socios por tipo.
     *
     * @param tipoSocio El tipo de socio a listar (0 = Estandar, 1 = Federado, 2 = Infantil).
     * @return Lista de socios filtrada por tipo.
     */
    public static List<Socio> listarSocios(int tipoSocio) {
        return ListaSocios.listarSociosPorTipo(tipoSocio);
    }

    // ------------------------------
    // Métodos de gestión de Excursiones
    // ------------------------------

    /**
     * Registra una nueva excursión en la lista de excursiones.
     *
     * Excepciones:
     * - ExcursionYaExisteException: Se lanza si el código de la excursión ya está registrado.
     *
     * @param parametros Lista de parámetros necesarios para crear la excursión.
     * @return true si la excursión se ha registrado con éxito.
     */
    public static boolean registrarExcursion(List<Object> parametros) throws ExcursionYaExisteException {
        String codigo = parametros.get(0).toString();
        String descripcion = parametros.get(1).toString();
        java.sql.Date sqlDate = (java.sql.Date) parametros.get(2);
        LocalDate fecha = sqlDate.toLocalDate();
        int numeroDias = (Integer) parametros.get(3);
        float precio = (Float) parametros.get(4);

        if (ListaExcursiones.excursionExiste(codigo)) {
            throw new ExcursionYaExisteException("La excursión ya existe.");
        }

        // Crear la excursión y agregarla a la lista
        Excursion excursion = new Excursion(codigo, descripcion, Date.valueOf(fecha), numeroDias, precio);
        ListaExcursiones.addExcursion(excursion);
        return true;
    }

    /**
     * Elimina una excursión de la lista.
     *
     * Excepciones:
     * - ExcursionNoExisteException: Se lanza si la excursión no existe.
     * - EliminarExcursionConInscripcionesException: Se lanza si la excursión tiene inscripciones activas.
     *
     * @param codigoExcursion Código de la excursión a eliminar.
     * @return true si la excursión se ha eliminado con éxito.
     */
    public static boolean eliminarExcursion(String codigoExcursion) throws ExcursionNoExisteException, EliminarExcursionConInscripcionesException {
        Excursion excursion = ListaExcursiones.getExcursion(codigoExcursion);

        if (excursion == null) {
            throw new ExcursionNoExisteException("La excursión no existe.");
        }

        if (ListaInscripciones.excursionTieneInscripciones(excursion)) {
            throw new EliminarExcursionConInscripcionesException("La excursión tiene inscripciones activas.");
        }

        return ListaExcursiones.eliminarExcursion(codigoExcursion);
    }

    /**
     * Obtiene las excursiones que se realizan entre dos fechas específicas.
     *
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin Fecha de fin del rango.
     * @return Lista de excursiones que ocurren dentro del rango de fechas.
     */
    public static List<Excursion> obtenerExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursionesEnRango = new ArrayList<>();

        for (Excursion excursion : ListaExcursiones.getExcursiones()) {
            LocalDate fechaExcursion = excursion.getFechaAsLocalDate();

            if ((fechaExcursion.isEqual(fechaInicio) || fechaExcursion.isAfter(fechaInicio)) &&
                    (fechaExcursion.isEqual(fechaFin) || fechaExcursion.isBefore(fechaFin))) {
                excursionesEnRango.add(excursion);
            }
        }

        return excursionesEnRango;
    }

    // ------------------------------
    // Métodos de gestión de Inscripciones
    // ------------------------------

    /**
     * Registra una nueva inscripción a una excursión.
     *
     * Excepciones:
     * - SocioNoExisteException: Se lanza si el socio no existe.
     * - ExcursionNoExisteException: Se lanza si la excursión no existe.
     * - FechaInvalidaException: Se lanza si la fecha de inscripción es posterior a la fecha de la excursión.
     * - InscripcionYaExisteException: Se lanza si el socio ya está inscrito en esa excursión.
     *
     * @param parametros Lista de parámetros necesarios para crear la inscripción.
     * @return true si la inscripción se ha registrado con éxito.
     */
    public static boolean registrarInscripcion(List<Object> parametros)
            throws SocioNoExisteException, ExcursionNoExisteException, FechaInvalidaException, InscripcionYaExisteException {

        // Obtener el socio y la excursión a partir de los parámetros
        Socio socio = ListaSocios.getSocio(parametros.get(0).toString());
        Excursion excursion = ListaExcursiones.getExcursion(parametros.get(1).toString());
        LocalDate fechaInscripcion = (LocalDate) parametros.get(2);  // Usar LocalDate en lugar de Date

        // Verificar si el socio existe
        if (socio == null) {
            throw new SocioNoExisteException("El socio no existe.");
        }

        // Verificar si la excursión existe
        if (excursion == null) {
            throw new ExcursionNoExisteException("La excursión no existe.");
        }

        // Obtener la fecha de la excursión como LocalDate
        LocalDate fechaExcursion = excursion.getFechaAsLocalDate();

        // Validar que la fecha de inscripción no sea después de la fecha de la excursión
        if (fechaInscripcion.isAfter(fechaExcursion)) {
            throw new FechaInvalidaException("La inscripción no puede ser después de la excursión.");
        }

        // Verificar si la inscripción ya existe
        if (ListaInscripciones.inscripcionExiste(socio.getNumeroSocio(), excursion.getCodigo())) {
            throw new InscripcionYaExisteException("El socio ya está inscrito.");
        }

        // Registrar la inscripción generando un número único
        Inscripcion inscripcion = new Inscripcion(
                String.valueOf(ListaInscripciones.generarNumeroInscripcion()),  // Genera un número único para la inscripción
                socio,
                excursion,
                java.sql.Date.valueOf(fechaInscripcion)  // Convertimos LocalDate a java.sql.Date
        );

        // Agregar la inscripción
        ListaInscripciones.addInscripcion(inscripcion);
        return true;
    }

    /**
     * Elimina una inscripción de una excursión.
     *
     * Excepciones:
     * - InscripcionNoExisteException: Se lanza si la inscripción no existe.
     * - CancelacionInvalidaException: Se lanza si la excursión ya ha sido realizada y no se puede cancelar.
     *
     * @param numeroInscripcion Número de la inscripción a eliminar.
     * @return true si la inscripción se ha eliminado con éxito.
     */
    public static boolean eliminarInscripcion(String numeroInscripcion) throws InscripcionNoExisteException, CancelacionInvalidaException {
        Inscripcion inscripcion = ListaInscripciones.getInscripcion(numeroInscripcion);

        if (inscripcion == null) {
            throw new InscripcionNoExisteException("La inscripción no existe.");
        }

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaExcursion = inscripcion.getExcursion().getFechaAsLocalDate();

        if (fechaExcursion.isBefore(fechaActual)) {
            throw new CancelacionInvalidaException("No se puede eliminar una inscripción de una excursión ya realizada.");
        }

        return ListaInscripciones.eliminarInscripcion(numeroInscripcion);
    }

    /**
     * Lista todas las inscripciones registradas.
     *
     * @return Lista de inscripciones.
     */
    public static List<Inscripcion> listarInscripciones() {
        return ListaInscripciones.getInscripciones();
    }
}