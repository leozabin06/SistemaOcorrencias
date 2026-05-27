package repositorios.mysql;

import db.ConexaoDB;
import entidades.Diretor;
import enums.StatusEntidade;
import interfaces.IDiretorRepositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiretorRepositorioMySQL implements IDiretorRepositorio {

    @Override
    public void salvar(Diretor diretor) {
        String sql = "INSERT INTO diretor (matricula, nome, status) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diretor.getMatricula());
            ps.setString(2, diretor.getNome());
            ps.setString(3, diretor.getStatus().getValor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar diretor: " + e.getMessage(), e);
        }
    }

    @Override
    public Diretor buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM diretor WHERE matricula = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Diretor(rs.getString("matricula"), rs.getString("nome"), StatusEntidade.fromValor(rs.getString("status")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar diretor: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Diretor> listarTodos() {
        List<Diretor> lista = new ArrayList<>();
        String sql = "SELECT * FROM diretor ORDER BY nome";
        try (Connection conn = ConexaoDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Diretor(rs.getString("matricula"), rs.getString("nome"), StatusEntidade.fromValor(rs.getString("status"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar diretores: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }
}
