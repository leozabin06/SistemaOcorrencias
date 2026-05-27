package repositorios.mysql;

import db.ConexaoDB;
import entidades.Departamento;
import entidades.Gerente;
import enums.StatusEntidade;
import interfaces.IGerenteRepositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenteRepositorioMySQL implements IGerenteRepositorio {

    private final DepartamentoRepositorioMySQL deptoRepo = new DepartamentoRepositorioMySQL();

    @Override
    public void salvar(Gerente gerente) {
        String sql = "INSERT INTO gerente (matricula, nome, codigo_departamento, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gerente.getMatricula());
            ps.setString(2, gerente.getNome());
            ps.setInt(3, gerente.getDepartamento().getCodigo());
            ps.setString(4, gerente.getStatus().getValor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar gerente: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Gerente gerente) {
        String sql = "UPDATE gerente SET nome=?, codigo_departamento=?, status=? WHERE matricula=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gerente.getNome());
            ps.setInt(2, gerente.getDepartamento().getCodigo());
            ps.setString(3, gerente.getStatus().getValor());
            ps.setString(4, gerente.getMatricula());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gerente: " + e.getMessage(), e);
        }
    }

    @Override
    public Gerente buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM gerente WHERE matricula = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Departamento dep = deptoRepo.buscarPorCodigo(rs.getInt("codigo_departamento"));
                    return new Gerente(rs.getString("matricula"), rs.getString("nome"), dep, StatusEntidade.fromValor(rs.getString("status")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gerente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Gerente> listarTodos() {
        List<Gerente> lista = new ArrayList<>();
        String sql = "SELECT * FROM gerente ORDER BY nome";
        try (Connection conn = ConexaoDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Departamento dep = deptoRepo.buscarPorCodigo(rs.getInt("codigo_departamento"));
                lista.add(new Gerente(rs.getString("matricula"), rs.getString("nome"), dep, StatusEntidade.fromValor(rs.getString("status"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar gerentes: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }
}
