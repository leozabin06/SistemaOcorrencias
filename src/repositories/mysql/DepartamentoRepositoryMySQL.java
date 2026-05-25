package repositories.mysql;

import db.ConexaoDB;
import entities.Departamento;
import interfaces.IDepartamentoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoRepositoryMySQL implements IDepartamentoRepository {

    @Override
    public void salvar(Departamento departamento) {
        String sql = "INSERT INTO departamento (codigo, nome, descricao, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departamento.getCodigo());
            ps.setString(2, departamento.getNome());
            ps.setString(3, departamento.getDescricao());
            ps.setString(4, departamento.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar departamento: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Departamento departamento) {
        String sql = "UPDATE departamento SET nome=?, descricao=?, status=? WHERE codigo=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departamento.getNome());
            ps.setString(2, departamento.getDescricao());
            ps.setString(3, departamento.getStatus());
            ps.setInt(4, departamento.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar departamento: " + e.getMessage(), e);
        }
    }

    @Override
    public Departamento buscarPorCodigo(int codigo) {
        String sql = "SELECT * FROM departamento WHERE codigo = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Departamento(rs.getInt("codigo"), rs.getString("nome"),
                            rs.getString("descricao"), rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar departamento: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Departamento> listarTodos() {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM departamento ORDER BY nome";
        try (Connection conn = ConexaoDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Departamento(rs.getInt("codigo"), rs.getString("nome"),
                        rs.getString("descricao"), rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar departamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean existeCodigo(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }
}
