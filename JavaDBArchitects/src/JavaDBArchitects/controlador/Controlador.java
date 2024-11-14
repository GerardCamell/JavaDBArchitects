package JavaDBArchitects.controlador;

import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.modelo.*;
import JavaDBArchitects.modelo.dao.*;
import JavaDBArchitects.vista.MenuPrincipal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;




public class Controlador {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Instancias de DAO usando DAOFactory
    private static final SocioDAO socioDAO = DAOFactory.getSocioDAO();
    private static final ExcursionDAO excursionDAO = DAOFactory.getExcursionDAO();
    private static final InscripcionDAO inscripcionDAO = DAOFactory.getInscripcionDAO();
    private static final FederacionDAO federacionDAO = DAOFactory.getFederacion();
    private static final SeguroDAO seguroDAO = DAOFactory.getSeguroDAO();

    public static void iniciarAplicacion() {
        seguroDAO.initializeSeguros();  // Asegura que los dos tipos de seguros se crean al inicio
        MenuPrincipal.mostrarMenu();    // Llama al menú principal después de la inicialización
    }


    // Método para registrar un socio mediante procedimiento almacenado y transacciones
    public static void registrarSocioPA(String nombre, int tipoSocio, String nif, int idFederacion, Integer idSocioPadre, Object extra, String nombreFederacion) {
        socioDAO.registrarSocioPA(nombre, tipoSocio, nif, idFederacion, idSocioPadre, extra, nombreFederacion);
        MenuPrincipal.mostrarMensaje("Socio registrado correctamente.");
    }



    // Método para eliminar un socio mediante procedimiento almacenado y una transacción
    public static void eliminarSocioPA(int numeroSocio) {
        SocioDAO.eliminarSocioPA(numeroSocio);
        MenuPrincipal.mostrarMensaje("Socio eliminado con éxito mediante procedimiento almacenado.");
    }

//Método para registrar una excursion con procedimiento almacenado y transacciones
    public static void registrarExcursionPA(String codigo, String descripcion, LocalDate fecha, int numeroDias, float precio) {
        excursionDAO.registrarExcursionPA(codigo, descripcion, fecha, numeroDias, precio);
        MenuPrincipal.mostrarMensaje("Excursión registrada con éxito.");
    }

    // Método en Controlador para eliminar una excursión usando procedimiento almacenado y transacciones
    public static boolean eliminarExcursionPA(String idExcursion) {
        return ExcursionDAO.eliminarExcursionPA(idExcursion);
    }



    // Método para inscribir en una excursión mediante procedimiento almacenado y una transacción
    public static void inscribirEnExcursionPA(int numeroSocio, String codigoExcursion, LocalDate fechaInscripcion) {
        InscripcionDAO.inscribirEnExcursionPA(numeroSocio, codigoExcursion, fechaInscripcion);
        MenuPrincipal.mostrarMensaje("Inscripción realizada con éxito mediante procedimiento almacenado.");
    }


    // Método para eliminar una inscripción mediante procedimiento almacenado y transacciones
    public static void eliminarInscripcionPA(int numeroInscripcion) {
        boolean eliminado = InscripcionDAO.eliminarInscripcionPA(numeroInscripcion);
        if (eliminado) {
            MenuPrincipal.mostrarMensaje("Inscripción eliminada con éxito mediante procedimiento almacenado.");
        } else {
            MenuPrincipal.mostrarError("No se encontró la inscripción o no se pudo eliminar.");
        }
    }


    // Método para listar inscripciones mediante procedimiento almacenado
    public static void listarInscripcionesPA() {
        InscripcionDAO.listarInscripcionesPA();
    }

    // Método para listar excursiones por fechas mediante procedimiento almacenado
    public static void listarExcursionesPorFecha(String fechaInicio, String fechaFin) {
        ExcursionDAO.listarExcursionesPorFechaPA(fechaInicio, fechaFin);
    }

    // Método para listar socios por tipo mediante procedimiento almacenado
    public static void listarSociosPorTipoPA(int tipoSocio) {
        List<Socio> socios = socioDAO.listarSociosPorTipoPA(tipoSocio);
        if (socios.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron socios.");
        } else {
            for (Socio socio : socios) {
                MenuPrincipal.mostrarMensaje(socio.toString());
            }
        }
    }



  //-----RESTO DE MÉTODOS SIN PROCEDIMIENTOS ALMACENADOS


    // Método para mostrar inscripciones con filtros
    public static void mostrarInscripcionesConFiltros(Integer numeroSocio, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Inscripcion> inscripciones = inscripcionDAO.getAllInscripciones();

        // Filtrar por número de socio si se proporciona
        if (numeroSocio != null) {
            inscripciones = inscripciones.stream()
                    .filter(inscripcion -> inscripcion.getSocio().getNumeroSocio() == numeroSocio)
                    .collect(Collectors.toList());
        }

        // Filtrar por rango de fechas si se proporcionan ambas fechas
        if (fechaInicio != null && fechaFin != null) {
            inscripciones = inscripciones.stream()
                    .filter(inscripcion -> {
                        LocalDate fechaExcursion = inscripcion.getExcursion().getFechaAsLocalDate();
                        return !fechaExcursion.isBefore(fechaInicio) && !fechaExcursion.isAfter(fechaFin);
                    })
                    .collect(Collectors.toList());
        }

        // Mostrar mensaje si no hay inscripciones después de los filtros
        if (inscripciones.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron inscripciones con los filtros aplicados.");
        } else {
            for (Inscripcion inscripcion : inscripciones) {
                LocalDate fechaExcursion = inscripcion.getExcursion().getFechaAsLocalDate();
                MenuPrincipal.mostrarMensaje("Inscripción: " + inscripcion + " - Fecha: " + fechaExcursion.format(FORMATO_FECHA));
            }
        }
    }



    // Método para modificar los datos de un socio
    public static void modificarDatosSocio(int numeroSocio, String nuevoNombre) {
        try {
            Socio socio = socioDAO.getSocioByNumero(numeroSocio);
            if (socio == null) {
                throw new SocioNoExisteException("El socio no existe.");
            }

            socio.setNombre(nuevoNombre);
            socioDAO.updateSocio(socio);
            MenuPrincipal.mostrarMensaje("Datos del socio actualizados con éxito.");
        } catch (SocioNoExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        }
    }




    public static void consultarFacturaMensual(int numeroSocio) {
        Socio socio = socioDAO.getSocioByNumero(numeroSocio);

        if (socio == null) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
            return;
        }

        // Obtener la cuota mensual calculada desde la base de datos
        BigDecimal cuotaMensual = socio.getCuotaMensual();  // Este valor ya incluye el descuento y el precio del seguro, si corresponde.

        // Inicializar el total de la factura con la cuota mensual calculada
        BigDecimal totalFactura = cuotaMensual != null ? cuotaMensual : BigDecimal.ZERO;

        // Obtener todas las inscripciones del socio para calcular el total de excursiones
        List<Inscripcion> inscripciones = inscripcionDAO.getInscripcionesBySocio(numeroSocio);

        // Sumar el precio de cada excursión, aplicando los cargos o descuentos según el tipo de socio
        for (Inscripcion inscripcion : inscripciones) {
            BigDecimal precioExcursion = BigDecimal.valueOf(inscripcion.getExcursion().getPrecioInscripcion());

            if (socio instanceof Federado) {
                // Para los federados, se aplica un 10% de descuento en el precio de la excursión
                totalFactura = totalFactura.add(precioExcursion.multiply(BigDecimal.valueOf(0.9)));
            } else {
                // Para los socios estándar e infantiles, se añade el precio de la excursión sin descuento adicional
                totalFactura = totalFactura.add(precioExcursion);
            }
        }

        // Mostrar el total calculado de la factura mensual
        MenuPrincipal.mostrarMensaje("Factura mensual para el socio " + numeroSocio + ": " + totalFactura + "€");
    }



    // Método para mostrar excursiones entre dos fechas
    public static void mostrarExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursiones = excursionDAO.getExcursionesEntreFechas(fechaInicio, fechaFin);

        if (excursiones.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron excursiones entre las fechas proporcionadas.");
        } else {
            for (Excursion excursion : excursiones) {
                LocalDate fechaExcursion = excursion.getFechaAsLocalDate();
                LocalDate fechaFinExcursion = excursion.calcularFechaFin();

                if (excursion.getNumDias() > 1) {
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion + "\n" + "Fecha Fin de Excursión: " + fechaFinExcursion);
                } else {
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion + " - Fecha: " + fechaExcursion);
                }
            }






            //---MÉTODOS ANTERIORES SIN PROCEDIMIENTOS ALMACENADOS QUE YA NO SE USAN:

             /*public static void registrarSocio(int numeroSocio, String nombre, int tipoSocio, String NIF, Object extra, BigDecimal cuotaMensual) {
        try {
            Socio socio;
            if (tipoSocio == 0) { // Estandar
                if (extra instanceof Seguro) {
                    socio = new Estandar(numeroSocio, nombre, NIF, (Seguro) extra, cuotaMensual);
                } else {
                    throw new IllegalArgumentException("Error: El socio estándar debe tener un seguro válido.");
                }
            } else if (tipoSocio == 1) { // Federado
                socio = new Federado(numeroSocio, nombre, NIF, (Federacion) extra, cuotaMensual);
            } else if (tipoSocio == 2) { // Infantil
                socio = new Infantil(numeroSocio, nombre, (Integer) extra, cuotaMensual);
            } else {
                throw new TipoSocioInvalidoException("Tipo de socio no válido");
            }
            socioDAO.addSocio(socio);
            MenuPrincipal.mostrarMensaje("Socio registrado con éxito.");
        } catch (SocioYaExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio ya existe.");
        } catch (TipoSocioInvalidoException e) {
            MenuPrincipal.mostrarError("Error: El tipo de socio proporcionado no es válido.");
        }
    }*/



            // Método para eliminar un socio
    /*public static void eliminarSocio(int numeroSocio) {
        try {
            socioDAO.deleteSocio(numeroSocio);
            MenuPrincipal.mostrarMensaje("Socio eliminado con éxito.");
        } catch (SocioNoExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        } catch (SocioConInscripcionesException e) {
            MenuPrincipal.mostrarError("Error: El socio tiene inscripciones activas y no puede ser eliminado.");
        }
    }*/

            // Método para eliminar una excursión
    /*public static boolean eliminarExcursion(String codigoExcursion) {
        try {
            excursionDAO.deleteExcursion(codigoExcursion);
            MenuPrincipal.mostrarMensaje("Excursión eliminada con éxito.");
            return true;
        } catch (ExcursionNoExisteException e) {
            MenuPrincipal.mostrarError("Error: La excursión no existe.");
            return false;
        } catch (EliminarExcursionConInscripcionesException e) {
            MenuPrincipal.mostrarError("Error: La excursión tiene inscripciones activas y no puede ser eliminada.");
            return false;
        }
    }*/


            // Método para listar todas las inscripciones
    /*public static void listarInscripciones() {
        List<Inscripcion> inscripciones = inscripcionDAO.getAllInscripciones();
        for (Inscripcion inscripcion : inscripciones) {
            LocalDate fechaExcursion = inscripcion.getExcursion().getFechaAsLocalDate();
            LocalDate fechaFinExcursion = inscripcion.getExcursion().calcularFechaFin();
            int nDias = inscripcion.getExcursion().getNumDias();

            if (nDias > 1) {
                MenuPrincipal.mostrarMensaje(inscripcion + "\n" +
                        "\n-- Las Fechas de la Excursión --\n" +
                        "Fecha de INICIO de la Excursión: " + fechaExcursion.format(FORMATO_FECHA) + "\n" +
                        "Fecha FINAL de la Excursión: " + fechaFinExcursion.format(FORMATO_FECHA) + "\n");
            } else {
                MenuPrincipal.mostrarMensaje(inscripcion + "\n Fecha de la Excursión: " + fechaExcursion.format(FORMATO_FECHA));
            }
        }
    }
*/





        }
    }
}

