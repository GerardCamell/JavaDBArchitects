package JavaDBArchitects.modelo;

import JavaDBArchitects.controlador.excepciones.*;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;

public class Datos {

    // Método para registrar un nuevo socio
    public static boolean registrarSocio(List<Object> parametros) throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        Socio socio = null;
        int tipoSocio = (Integer) parametros.get(0);

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

        if (ListaSocios.socioExiste(socio.getNumeroSocio())) {
            throw new SocioYaExisteException("El socio con número " + socio.getNumeroSocio() + " ya existe.");
        }

        ListaSocios.addSocio(socio);
        return true;
    }

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

    // Método para listar socios según el tipo
    public static List<Socio> listarSocios(int tipoSocio) {
        return ListaSocios.listarSociosPorTipo(tipoSocio);  // Llama al método de ListaSocios
    }

    // Método para registrar una excursión
    public static boolean registrarExcursion(List<Object> parametros) throws ExcursionYaExisteException {
        String codigo = parametros.get(0).toString();
        String descripcion = parametros.get(1).toString();
        java.sql.Date sqlDate = (java.sql.Date) parametros.get(2);  // Usar java.sql.Date
        LocalDate fecha = sqlDate.toLocalDate();  // Convertir a LocalDate
        int numeroDias = (Integer) parametros.get(3);
        float precio = (Float) parametros.get(4);

        if (ListaExcursiones.excursionExiste(codigo)) {
            throw new ExcursionYaExisteException("La excursión ya existe.");
        }

        // Crear la excursión usando LocalDate y convertirla a java.sql.Date
        Excursion excursion = new Excursion(codigo, descripcion, Date.valueOf(fecha), numeroDias, precio);
        ListaExcursiones.addExcursion(excursion);
        return true;
    }

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

    public static boolean registrarInscripcion(List<Object> parametros)
            throws SocioNoExisteException, ExcursionNoExisteException, FechaInvalidaException, InscripcionYaExisteException {

        // Obtener los parámetros
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
        LocalDate fechaExcursion = excursion.getFechaAsLocalDate();  // No usamos toInstant()

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


    public static List<Inscripcion> listarInscripciones() {
        return ListaInscripciones.getInscripciones();
    }

    public static List<Excursion> obtenerExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursionesEnRango = new ArrayList<>();

        for (Excursion excursion : ListaExcursiones.getExcursiones()) {
            // Convertir java.sql.Date o java.util.Date a LocalDate
            LocalDate fechaExcursion = excursion.getFechaAsLocalDate();  // Usamos getFechaAsLocalDate para manejar correctamente la fecha

            // Comparar fechas correctamente
            if ((fechaExcursion.isEqual(fechaInicio) || fechaExcursion.isAfter(fechaInicio)) &&
                    (fechaExcursion.isEqual(fechaFin) || fechaExcursion.isBefore(fechaFin))) {
                excursionesEnRango.add(excursion);
            }
        }

        return excursionesEnRango;
    }

    public static boolean modificarSeguroSocioEstandar(String numeroSocio, String tipoSeguro, float precio) throws SocioNoExisteException {
        Socio socio = ListaSocios.getSocio(numeroSocio);
        if (socio == null) {
            throw new SocioNoExisteException("El socio no existe.");
        }

        if (socio instanceof Estandar) {
            ((Estandar) socio).setSeguro(new Seguro(tipoSeguro, precio));
            return true;
        } else {
            return false;
        }
    }
}
