package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.Federacion;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;

import java.sql.*;

public class FederacionDAO {

    public void addFederacion(Federacion federacion, Connection connection) throws SQLException {
        String query = "INSERT INTO federaciones (nombre) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, federacion.getNombre());
            preparedStatement.executeUpdate();

            // Obtener el id_federacion generado y asignarlo a la instancia de Federacion
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                federacion.setId_federacion(generatedKeys.getInt(1));
                System.out.println("Federación añadida: ID = " + federacion.getId_federacion() + ", Nombre = " + federacion.getNombre());
            } else {
                System.out.println("No se pudo obtener el ID generado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Rethrow para que SocioDAO maneje la excepción
        }
    }


    // Método para obtener una federación por su id_federacion
    public Federacion getFederacionById(int id_federacion) {
        String query = "SELECT * FROM federaciones WHERE id_federacion = ?";
        Federacion federacion = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (connection == null || connection.isClosed()) {
                System.out.println("Conexión fallida en getFederacionById.");
                return null;
            }

            preparedStatement.setInt(1, id_federacion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                federacion = new Federacion(id_federacion, nombre);
                System.out.println("Federación obtenida: ID = " + federacion.getId_federacion() + ", Nombre = " + federacion.getNombre());
            } else {
                System.out.println("Federación no encontrada con ID: " + id_federacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return federacion;
    }

    // Método para limpiar todas las federaciones
    public void clearFederaciones() {
        String query = "DELETE FROM federaciones";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Tabla federaciones limpiada. Filas afectadas: " + affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para actualizar una federación en la base de datos por su ID
    public void updateFederacion(Federacion federacion) {
        String query = "UPDATE federaciones SET nombre = ? WHERE id_federacion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Establecer los parámetros de la consulta
            preparedStatement.setString(1, federacion.getNombre());
            preparedStatement.setInt(2, federacion.getId_federacion());

            // Ejecutar la actualización
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Federación actualizada con éxito: " + federacion.getId_federacion());
            } else {
                System.out.println("No se encontró ninguna federación con ID: " + federacion.getId_federacion());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Método para eliminar una federación de la base de datos por su ID
    public void deleteFederacion(int id_federacion) {
        String query = "DELETE FROM federaciones WHERE id_federacion = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Establece el parámetro de la consulta
            preparedStatement.setInt(1, id_federacion);

            // Ejecuta la eliminación
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Federación eliminada con éxito con ID: " + id_federacion);
            } else {
                System.out.println("No se encontró ninguna federación con ID: " + id_federacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
