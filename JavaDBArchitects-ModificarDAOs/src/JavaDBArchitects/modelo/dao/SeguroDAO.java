package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.Seguro;

import JavaDBArchitects.modelo.TipoSeguro;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeguroDAO {

    // Método para inicializar seguros en la base de datos
    public void initializeSeguros() {
        // Asegurarnos de que el seguro BASICO existe
        if (getSeguroByTipo(TipoSeguro.BASICO) == null) {
            addSeguro(new Seguro(TipoSeguro.BASICO, 50.0f));
        }

        // Asegurarnos de que el seguro COMPLETO existe
        if (getSeguroByTipo(TipoSeguro.COMPLETO) == null) {
            addSeguro(new Seguro(TipoSeguro.COMPLETO, 100.0f));
        }
    }

    // Método para agregar un seguro a la base de datos COn PreparedStatement
    public void addSeguro(Seguro seguro) {
        String query = "INSERT INTO Seguros (tipo, precio) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, seguro.getTipo().name()); // Convertir enum a String
            preparedStatement.setFloat(2, seguro.getPrecio());

            preparedStatement.executeUpdate();
            System.out.println("Seguro de tipo " + seguro.getTipo() + " añadido con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener un seguro por tipo
    public Seguro getSeguroByTipo(TipoSeguro tipo) {
        String query = "SELECT * FROM Seguros WHERE tipo = ?";
        Seguro seguro = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tipo.name()); // Convertir enum a String
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                float precio = resultSet.getFloat("precio");
                seguro = new Seguro(tipo, precio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seguro;
    }

    // Método para obtener todos los seguros
    public List<Seguro> getAllSeguros() {
        List<Seguro> seguros = new ArrayList<>();
        String query = "SELECT * FROM Seguros";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                TipoSeguro tipo = TipoSeguro.valueOf(resultSet.getString("tipo")); // Convertir a TipoSeguro
                float precio = resultSet.getFloat("precio");

                seguros.add(new Seguro(tipo, precio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seguros;
    }
}

