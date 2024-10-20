package JavaDBArchitects.controlador;

import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.modelo.*;
import JavaDBArchitects.vista.MenuPrincipal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controlador {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Método para registrar un nuevo socio
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
            MenuPrincipal.mostrarError("Error: El socio ya existe.");
        } catch (SeguroInvalidoException e) {
            MenuPrincipal.mostrarError("Error: El seguro proporcionado no es válido.");
        } catch (TipoSocioInvalidoException e) {
            MenuPrincipal.mostrarError("Error: El tipo de socio proporcionado no es válido.");
        }
    }

    // Método para eliminar un socio
    public static void eliminarSocio(String numeroSocio) {
        try {
            Datos.eliminarSocio(numeroSocio);
            MenuPrincipal.mostrarMensaje("Socio eliminado con éxito.");
        } catch (SocioNoExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        } catch (SocioConInscripcionesException e) {
            MenuPrincipal.mostrarError("Error: El socio tiene inscripciones activas y no puede ser eliminado.");
        }
    }

    // Método para registrar una excursión
    public static void registrarExcursion(String codigo, String descripcion, LocalDate fecha, int numeroDias, float precio) throws ExcursionYaExisteException {
        List<Object> parametros = new ArrayList<>();
        parametros.add(codigo);
        parametros.add(descripcion);
        parametros.add(java.sql.Date.valueOf(fecha)); // Convertir LocalDate a Date
        parametros.add(numeroDias);
        parametros.add(precio);

        // Llamada al método en Datos que puede lanzar la excepción
        Datos.registrarExcursion(parametros);
        MenuPrincipal.mostrarMensaje("Excursión registrada con éxito.");
    }


    // Método para eliminar una excursión
    public static boolean eliminarExcursion(String codigoExcursion) {
        try {
            // Solo retorna el resultado, sin mostrar el mensaje aquí
            return Datos.eliminarExcursion(codigoExcursion);
        } catch (ExcursionNoExisteException e) {
            MenuPrincipal.mostrarError("Error: La excursión no existe.");
            return false;  // Devolver false si la excursión no existe
        } catch (EliminarExcursionConInscripcionesException e) {
            MenuPrincipal.mostrarError("Error: La excursión tiene inscripciones activas y no puede ser eliminada.");
            return false;  // Devolver false si no se puede eliminar por inscripciones activas
        }
    }


    public static void inscribirEnExcursion(String numeroSocio, String codigoExcursion, LocalDate fechaInscripcion) {
        try {
            List<Object> parametros = new ArrayList<>();
            parametros.add(numeroSocio);
            parametros.add(codigoExcursion);
            parametros.add(fechaInscripcion);  // Aquí estamos pasando LocalDate directamente

            Datos.registrarInscripcion(parametros);
            MenuPrincipal.mostrarMensaje("Inscripción realizada con éxito.");
        } catch (InscripcionYaExisteException e) {
            MenuPrincipal.mostrarError("Error: Ya estás inscrito en esta excursión.");
        } catch (SocioNoExisteException e) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
        } catch (ExcursionNoExisteException e) {
            MenuPrincipal.mostrarError("Error: La excursión no existe.");
        } catch (FechaInvalidaException e) {
            MenuPrincipal.mostrarError("Error: La fecha de inscripción no es válida.");
        }
    }

// Método para eliminar una inscripción
    public static void eliminarInscripcion(String numeroInscripcion) {
        try {
            Datos.eliminarInscripcion(numeroInscripcion);
            MenuPrincipal.mostrarMensaje("Inscripción eliminada con éxito.");
        } catch (InscripcionNoExisteException e) {
            MenuPrincipal.mostrarError("Error: La inscripción no existe.");
        } catch (CancelacionInvalidaException e) {
            MenuPrincipal.mostrarError(e.getMessage());
        }
    }


    // Método para listar todas las inscripciones
    public static void listarInscripciones() {
        List<Inscripcion> inscripciones = Datos.listarInscripciones();
        for (Inscripcion inscripcion : inscripciones) {
            java.sql.Date sqlDate = (java.sql.Date) inscripcion.getExcursion().getFecha(); // Conversión a sql.Date
            LocalDate fechaExcursion = sqlDate.toLocalDate(); // Convertir a LocalDate
            LocalDate fechaFinExcursion = inscripcion.getExcursion().calcularFechaFin();
            int nDias = inscripcion.getExcursion().getNumDias();
            

            // Mostrar la inscripción formateando la fecha
            if(nDias > 1){
                MenuPrincipal.mostrarMensaje( inscripcion + "\n"+"\n-- Las Fechas de la Excursión --\n" + "Fecha de INICIO de la Excursión: " + fechaExcursion.format(FORMATO_FECHA)+ "\n" + "Fecha FINAL de la Excursión: " + fechaFinExcursion.format(FORMATO_FECHA)+"\n");
            }else{
                MenuPrincipal.mostrarMensaje( inscripcion + "\n Fecha de la Excursión: " + fechaExcursion.format(FORMATO_FECHA));
            }

        }
    }

    // Método para mostrar inscripciones con filtros
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
                MenuPrincipal.mostrarMensaje("Inscripción: " + inscripcion + " - Fecha: " + fechaExcursion.format(FORMATO_FECHA));
            }
        }
    }

    // Método para consultar la factura mensual
    public static void consultarFacturaMensual(String numeroSocio) {
        // Obtener el socio del sistema
        Socio socio = ListaSocios.getSocio(numeroSocio);

        // Verificar si el socio existe
        if (socio == null) {
            MenuPrincipal.mostrarError("Error: El socio no existe.");
            return;
        }

        // Definir cuota base mensual
        float cuotaBase = 10; // Ejemplo: cuota base es 10€

        // Calcular la cuota mensual con descuentos
        float cuotaMensual;
        if (socio instanceof Estandar) {
            cuotaMensual = cuotaBase; // Sin descuento para estándar
        } else if (socio instanceof Infantil) {
            cuotaMensual = cuotaBase * 0.5f; // 50% de descuento
        } else if (socio instanceof Federado) {
            cuotaMensual = cuotaBase * 0.95f; // 5% de descuento
        } else {
            cuotaMensual = 0; // Caso no válido
        }

        // Inicializar el total de la factura
        float totalFactura = cuotaMensual;

        // Obtener las inscripciones del socio
        List<Inscripcion> inscripciones = ListaInscripciones.getInscripciones();

        // Calcular el coste total de las excursiones
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio)) {
                float precioExcursion = inscripcion.getExcursion().getPrecioInscripcion(); // Obtener precio de la excursión

                // Calcular el precio según el tipo de socio
                if (socio instanceof Estandar) {
                    // Precio de la excursión y el seguro (entendemos que el seguro es por excursión, en caso que no sea así se modificaría)
                    float seguroPrecio = ((Estandar) socio).getSeguro().getPrecio(); // Acceder al seguro
                    totalFactura += precioExcursion + seguroPrecio;
                } else if (socio instanceof Federado) {
                    // 10% de descuento en el precio de la excursión
                    totalFactura += precioExcursion * 0.9f;
                } else if (socio instanceof Infantil) {
                    // Precio completo de la excursión sin cargos adicionales
                    totalFactura += precioExcursion;
                }
            }
        }

        // Mostrar el total calculado
        MenuPrincipal.mostrarMensaje("Factura mensual para el socio " + numeroSocio + ": " + totalFactura + "€");
    }



    // Método para modificar datos del socio (simulado)
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

    // Método para mostrar excursiones entre dos fechas
    public static void mostrarExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursiones = Datos.obtenerExcursionesEntreFechas(fechaInicio, fechaFin);
        if (excursiones.isEmpty()) {
            MenuPrincipal.mostrarMensaje("No se encontraron excursiones entre las fechas proporcionadas.");
        } else {
            for (Excursion excursion : excursiones) {
                // Usa el metodo getFechaAsLocalDate() para obtener la fecha correctamente
                LocalDate fechaExcursion = excursion.getFechaAsLocalDate();
                LocalDate fechaFinExcursion = excursion.calcularFechaFin();

               if (excursion.getNumDias() > 1) { // Si la excursion tiene más de un día
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion + "\n" + "Fecha Fin de Excursión: " + fechaFinExcursion.format(FORMATO_FECHA));// Revisa el final de la excursión
                    //MenuPrincipal.mostrarMensaje("MAS DE UN DIA."); test de más de un dia
                } else {// Si la excursión dura SÓLO un día
                    MenuPrincipal.mostrarMensaje("Excursión: " + excursion);// + "- Fecha: " + fechaExcursion.format(FORMATO_FECHA));
                    //MenuPrincipal.mostrarMensaje("UN DIA"); test de un día
                }

            }
        }
    }


    // Método para listar todos los socios o por tipo
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
