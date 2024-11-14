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

    public static boolean registrarSocioPA(List<Object> parametros) throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        int tipoSocio = (Integer) parametros.get(0);
        String nombre = parametros.get(2).toString();
        String NIF = parametros.get(3).toString();
        BigDecimal cuotaMensual = (BigDecimal) parametros.get(5);
        Object extra = null;
        int idFederacion = 0;  // Inicializamos en 0 para los federados
        Integer idSocioPadre = null;  // Inicializamos en null para los infantiles
        String nombreFederacion = null; // Nombre de la federación para los federados

        if (tipoSocio == 0) {
            extra = (Seguro) parametros.get(4);
        } else if (tipoSocio == 1) {
            Federacion federacion = (Federacion) parametros.get(4);
            idFederacion = federacion.getId_federacion();
            nombreFederacion = federacion.getNombre(); // Asignamos el nombre de la federación
            extra = federacion;
        } else if (tipoSocio == 2) {
            idSocioPadre = (Integer) parametros.get(4);
        } else {
            throw new TipoSocioInvalidoException("El tipo de socio es inválido.");
        }

        if (socioDAO.socioExiste((Integer) parametros.get(1))) {
            throw new SocioYaExisteException("El socio con número " + parametros.get(1) + " ya existe.");
        }

        // Llamada a registrarSocioPA con el séptimo parámetro
        socioDAO.registrarSocioPA(nombre, tipoSocio, NIF, idFederacion, idSocioPadre, extra, nombreFederacion);
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

        socioDAO.eliminarSocioPA(numeroSocio);  // Cambiado a eliminarSocioPA
        return true;
    }


    public static List<Socio> listarSocios(int tipoSocio) {
        return socioDAO.listarSociosPorTipoPA(tipoSocio);
    }

    // ------------------------------
    // Métodos de gestión de Excursiones
    // ------------------------------

    public static boolean registrarExcursionPA(List<Object> parametros) throws ExcursionYaExisteException {
        String idExcursion = parametros.get(0).toString();
        String descripcion = parametros.get(1).toString();
        java.sql.Date sqlDate = (java.sql.Date) parametros.get(2);
        LocalDate fecha = sqlDate.toLocalDate();
        int numeroDias = (Integer) parametros.get(3);
        float precio = (Float) parametros.get(4);

        if (excursionDAO.excursionExiste(idExcursion)) {
            throw new ExcursionYaExisteException("La excursión ya existe.");
        }

        // Llamada directa a registrarExcursionPA con los parámetros desglosados
        excursionDAO.registrarExcursionPA(idExcursion, descripcion, fecha, numeroDias, precio);
        return true;
    }


    public static boolean eliminarExcursionPA(String idExcursion) throws ExcursionNoExisteException, EliminarExcursionConInscripcionesException {
        Excursion excursion = excursionDAO.getExcursionById(idExcursion);

        if (excursion == null) {
            throw new ExcursionNoExisteException("La excursión no existe.");
        }

        if (inscripcionDAO.excursionTieneInscripciones(excursion)) {
            throw new EliminarExcursionConInscripcionesException("La excursión tiene inscripciones activas.");
        }

        excursionDAO.eliminarExcursionPA(idExcursion);
        return true;
    }

    public static List<Excursion> listarExcursionesPorFechaPA(LocalDate fechaInicio, LocalDate fechaFin) {
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
        if (inscripcionDAO.inscripcionExiste(socio.getNumeroSocio(), excursion.getIdExcursion())) {
            throw new InscripcionYaExisteException("El socio ya está inscrito.");
        }

        // Llamada al método de procedimiento almacenado
        inscripcionDAO.inscribirEnExcursionPA(socio.getNumeroSocio(), excursion.getIdExcursion(), fechaInscripcion);
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

        boolean eliminado = inscripcionDAO.eliminarInscripcionPA(Integer.parseInt(numeroInscripcion));
        if (eliminado) {
            System.out.println("Inscripción eliminada correctamente.");
        } else {
            System.out.println("No se encontró ninguna inscripción con el ID proporcionado.");
        }
        return eliminado;
    }
}

