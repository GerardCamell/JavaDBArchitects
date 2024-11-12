package JavaDBArchitects.modelo;

import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.modelo.dao.DAOFactory;
import JavaDBArchitects.modelo.dao.ExcursionDAO;
import JavaDBArchitects.modelo.dao.InscripcionDAO;
import JavaDBArchitects.modelo.dao.SocioDAO;

import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.math.BigDecimal;

public class Datos {

    // Instancias de DAO
    private static final SocioDAO socioDAO = DAOFactory.getSocioDAO();
    private static final ExcursionDAO excursionDAO = DAOFactory.getExcursionDAO();
    private static final InscripcionDAO inscripcionDAO = DAOFactory.getInscripcionDAO();

    // ------------------------------
    // Métodos de gestión de Socios
    // ------------------------------

    public static boolean registrarSocio(List<Object> parametros) throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        Socio socio = null;
        int tipoSocio = (Integer) parametros.get(0);
        int numeroSocio = (Integer) parametros.get(1);
        String nombre = parametros.get(2).toString();
        String NIF = parametros.get(3).toString();
        BigDecimal cuotaMensual = (BigDecimal) parametros.get(5);

        if (tipoSocio == 0) {
            Seguro seguro = (Seguro) parametros.get(4);
            socio = new Estandar(numeroSocio, nombre, NIF, seguro, cuotaMensual);

        } else if (tipoSocio == 1) {
            Federacion federacion = (Federacion) parametros.get(4);
            socio = new Federado(numeroSocio, nombre, NIF, federacion, cuotaMensual);

        } else if (tipoSocio == 2) {
            int numSocioPadreOMadre = (Integer) parametros.get(4);
            socio = new Infantil(numeroSocio, nombre, numSocioPadreOMadre, cuotaMensual);

        } else {
            throw new TipoSocioInvalidoException("El tipo de socio es inválido.");
        }

        if (socioDAO.socioExiste(numeroSocio)) {
            throw new SocioYaExisteException("El socio con número " + numeroSocio + " ya existe.");
        }

        socioDAO.addSocio(socio);
        return true;
    }

    public static boolean eliminarSocio(int numeroSocio) throws SocioNoExisteException, SocioConInscripcionesException {
        Socio socio = socioDAO.getSocioByNumero(numeroSocio);  // Usar int

        if (socio == null) {
            throw new SocioNoExisteException("El socio con número " + numeroSocio + " no existe.");
        }

        if (inscripcionDAO.socioTieneInscripciones(numeroSocio)) { // Cambiado a int
            throw new SocioConInscripcionesException("El socio no puede ser eliminado.");
        }

        socioDAO.deleteSocio(numeroSocio);
        return true;
    }

    public static List<Socio> listarSocios(int tipoSocio) {
        return socioDAO.listarSociosPorTipo(tipoSocio);
    }

    // ------------------------------
    // Métodos de gestión de Excursiones
    // ------------------------------

    public static boolean registrarExcursion(List<Object> parametros) throws ExcursionYaExisteException {
        String idExcursion = parametros.get(0).toString();
        String descripcion = parametros.get(1).toString();
        java.sql.Date sqlDate = (java.sql.Date) parametros.get(2);
        LocalDate fecha = sqlDate.toLocalDate();
        int numeroDias = (Integer) parametros.get(3);
        float precio = (Float) parametros.get(4);

        if (excursionDAO.excursionExiste(idExcursion)) {
            throw new ExcursionYaExisteException("La excursión ya existe.");
        }

        Excursion excursion = new Excursion(idExcursion, descripcion, Date.valueOf(fecha), numeroDias, precio);
        excursionDAO.addExcursion(excursion);
        return true;
    }

    public static boolean eliminarExcursion(String idExcursion) throws ExcursionNoExisteException, EliminarExcursionConInscripcionesException {
        Excursion excursion = excursionDAO.getExcursionById(idExcursion);

        if (excursion == null) {
            throw new ExcursionNoExisteException("La excursión no existe.");
        }

        if (inscripcionDAO.excursionTieneInscripciones(excursion)) {
            throw new EliminarExcursionConInscripcionesException("La excursión tiene inscripciones activas.");
        }

        excursionDAO.deleteExcursion(idExcursion);
        return true;
    }

    public static List<Excursion> obtenerExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursionesEnRango = new ArrayList<>();

        for (Excursion excursion : excursionDAO.getAllExcursiones()) {
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

    public static boolean registrarInscripcion(List<Object> parametros)
            throws SocioNoExisteException, ExcursionNoExisteException, FechaInvalidaException, InscripcionYaExisteException {

        Socio socio = socioDAO.getSocioByNumero((Integer) parametros.get(0));  // Usar int
        Excursion excursion = excursionDAO.getExcursionById(parametros.get(1).toString());
        LocalDate fechaInscripcion = (LocalDate) parametros.get(2);

        if (socio == null) {
            throw new SocioNoExisteException("El socio no existe.");
        }

        if (excursion == null) {
            throw new ExcursionNoExisteException("La excursión no existe.");
        }

        LocalDate fechaExcursion = excursion.getFechaAsLocalDate();

        if (fechaInscripcion.isAfter(fechaExcursion)) {
            throw new FechaInvalidaException("La inscripción no puede ser después de la excursión.");
        }

        // Verificar si la inscripción ya existe
        if (inscripcionDAO.inscripcionExiste(socio.getNumeroSocio(), excursion.getIdExcursion())) { // Cambiado a int
            throw new InscripcionYaExisteException("El socio ya está inscrito.");
        }

        Inscripcion inscripcion = new Inscripcion(
                String.valueOf(inscripcionDAO.generarNumeroInscripcion()),
                socio,
                excursion,
                java.sql.Date.valueOf(fechaInscripcion)
        );

        inscripcionDAO.addInscripcion(inscripcion);
        return true;
    }

    public static boolean eliminarInscripcion(String numeroInscripcion) throws InscripcionNoExisteException, CancelacionInvalidaException {
        Inscripcion inscripcion = inscripcionDAO.getInscripcionById(numeroInscripcion);

        if (inscripcion == null) {
            throw new InscripcionNoExisteException("La inscripción no existe.");
        }

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaExcursion = inscripcion.getExcursion().getFechaAsLocalDate();

        if (fechaExcursion.isBefore(fechaActual)) {
            throw new CancelacionInvalidaException("No se puede eliminar una inscripción de una excursión ya realizada.");
        }

        inscripcionDAO.eliminarInscripcion(numeroInscripcion);
        return true;
    }

    public static List<Inscripcion> listarInscripciones() {
        return inscripcionDAO.getAllInscripciones();
    }
}
