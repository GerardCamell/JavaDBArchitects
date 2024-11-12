package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.Excursion;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.ExcursionNoExisteException;
import JavaDBArchitects.controlador.excepciones.EliminarExcursionConInscripcionesException;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class ExcursionDAO {

    // Método para agregar una nueva excursión
    public void addExcursion(Excursion excursion) {
        String query = "INSERT INTO Excursiones (idExcursion, descripcion, fecha, num_dias, precio) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, excursion.getIdExcursion());
            preparedStatement.setString(2, excursion.getDescripcion());
            preparedStatement.setDate(3, new java.sql.Date(excursion.getFecha().getTime()));
            preparedStatement.setInt(4, excursion.getNumDias());
            preparedStatement.setFloat(5, excursion.getPrecioInscripcion());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Metodo para regitsrar excursion mediante procedimiento almacenado

    public static void registrarExcursionPA(String idExcursion, String descripcion, LocalDate fecha, int numDias, float precioInscripcion) {
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021");
            conn.setAutoCommit(false);  // Inicia la transacción

            String sql = "{CALL registrarExcursion(?, ?, ?, ?, ?)}";
            stmt = conn.prepareCall(sql);

            // Establecer los parámetros del procedimiento
            stmt.setString(1, idExcursion);
            stmt.setString(2, descripcion);
            stmt.setDate(3, Date.valueOf(fecha));  // Convertir LocalDate a java.sql.Date
            stmt.setInt(4, numDias);
            stmt.setFloat(5, precioInscripcion);

            // Ejecutar el procedimiento
            stmt.executeUpdate();

            conn.commit();  // Confirma la transacción
            System.out.println("Excursión registrada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al ejecutar el procedimiento: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();  // Revertir la transacción en caso de error
                    System.out.println("Transacción revertida.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al revertir la transacción: " + rollbackEx.getMessage());
                }
            }
        } finally {
            // Cerrar recursos
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);  // Restaurar el auto-commit
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar la conexión: " + closeEx.getMessage());
            }
        }
    }
    //Sin transacción
    /* public static void registrarExcursionPA(String idExcursion, String descripcion, LocalDate fecha, int numDias, float precioInscripcion) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021")) {
            String sql = "{CALL registrarExcursion(?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                // Establecer los parámetros del procedimiento
                stmt.setString(1, idExcursion);
                stmt.setString(2, descripcion);
                Date fechaSQL = Date.valueOf(fecha);
                stmt.setDate(3, Date.valueOf(fecha));
                stmt.setInt(4, numDias);
                stmt.setFloat(5, precioInscripcion);

                // Ejecutar el procedimiento
                stmt.executeUpdate();
                System.out.println("Excursión registrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al ejecutar el procedimiento: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    } */



    // Método para verificar si una excursión existe según su código
    public boolean excursionExiste(String codigoExcursion) {
        String query = "SELECT COUNT(*) FROM Excursiones WHERE idExcursion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, codigoExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Método para obtener una excursión por su ID
    public Excursion getExcursionById(String idExcursion) {
        String query = "SELECT * FROM Excursiones WHERE idExcursion = ?";
        Excursion excursion = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, idExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String codigo = resultSet.getString("idExcursion");
                String descripcion = resultSet.getString("descripcion");
                Date fecha = resultSet.getDate("fecha");
                int numDias = resultSet.getInt("num_dias");
                float precioInscripcion = resultSet.getFloat("precio");

                excursion = new Excursion(codigo, descripcion, fecha, numDias, precioInscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return excursion;
    }

    // Método para verificar si una excursión tiene inscripciones activas
    private boolean tieneInscripcionesActivas(String idExcursion) {
        String query = "SELECT COUNT(*) FROM Inscripciones WHERE id_excursion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, idExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para eliminar una excursión por su ID, lanzando excepción si tiene inscripciones activas
    public void deleteExcursion(String idExcursion) throws ExcursionNoExisteException, EliminarExcursionConInscripcionesException {
        // Comprobar si la excursión tiene inscripciones activas
        if (tieneInscripcionesActivas(idExcursion)) {
            throw new EliminarExcursionConInscripcionesException("No se puede eliminar la excursión porque tiene inscripciones activas.");
        }

        String query = "DELETE FROM Excursiones WHERE idExcursion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, idExcursion);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new ExcursionNoExisteException("La excursión con ID " + idExcursion + " no existe.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método en ExcursionDAO para obtener excursiones entre dos fechas
    public List<Excursion> getExcursionesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Excursion> excursiones = new ArrayList<>();
        String query = "SELECT * FROM Excursiones WHERE fecha >= ? AND fecha <= ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, Date.valueOf(fechaInicio));
            preparedStatement.setDate(2, Date.valueOf(fechaFin));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String codigo = resultSet.getString("idExcursion");
                String descripcion = resultSet.getString("descripcion");
                Date fecha = resultSet.getDate("fecha");
                int numeroDias = resultSet.getInt("num_dias");
                float precio = resultSet.getFloat("precio");

                excursiones.add(new Excursion(codigo, descripcion, fecha, numeroDias, precio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return excursiones;
    }

    public List<Excursion> getAllExcursiones() {
        List<Excursion> excursiones = new ArrayList<>();
        String query = "SELECT * FROM Excursiones";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String codigo = resultSet.getString("idExcursion");
                String descripcion = resultSet.getString("descripcion");
                Date fecha = resultSet.getDate("fecha");
                int numDias = resultSet.getInt("num_dias");
                float precio = resultSet.getFloat("precio");

                Excursion excursion = new Excursion(codigo, descripcion, fecha, numDias, precio);
                excursiones.add(excursion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return excursiones;
    }

    // Método para eliminar todas las excursiones
    public void deleteAllExcursiones() {
        String query = "DELETE FROM Excursiones";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.executeUpdate();
            System.out.println("Todas las excursiones han sido eliminadas.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una excursión existente
    public boolean updateExcursion(Excursion excursion) {
        String query = "UPDATE Excursiones SET descripcion = ?, fecha = ?, num_dias = ?, precio = ? WHERE idExcursion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, excursion.getDescripcion());
            preparedStatement.setDate(2, new java.sql.Date(excursion.getFecha().getTime()));
            preparedStatement.setInt(3, excursion.getNumDias());
            preparedStatement.setFloat(4, excursion.getPrecioInscripcion());
            preparedStatement.setString(5, excursion.getIdExcursion());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false en caso de fallo
    }

}






