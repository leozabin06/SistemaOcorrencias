package repositorios.mysql;

import db.ConexaoDB;
import entidades.Departamento;
import entidades.Funcionario;
import interfaces.IFuncionarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepositoryMySQL implements IFuncionarioRepository {

    private final DepartamentoRepositoryMySQL deptoRepo = new DepartamentoRepositoryMySQL();

    @Override
    public void salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (matricula, nome, codigo_departamento, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcionario.getMatricula());
            ps.setString(2, funcionario.getNome());
            ps.setInt(3, funcionario.getDepartamento().getCodigo());
            ps.setString(4, funcionario.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome=?, codigo_departamento=?, status=? WHERE matricula=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setInt(2, funcionario.getDepartamento().getCodigo());
            ps.setString(3, funcionario.getStatus());
            ps.setString(4, funcionario.getMatricula());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public Funcionario buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM funcionario WHERE matricula = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Departamento dep = deptoRepo.buscarPorCodigo(rs.getInt("codigo_departamento"));
                    return new Funcionario(rs.getString("matricula"), rs.getString("nome"), dep, rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario ORDER BY nome";
        try (Connection conn = ConexaoDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Departamento dep = deptoRepo.buscarPorCodigo(rs.getInt("codigo_departamento"));
                lista.add(new Funcionario(rs.getString("matricula"), rs.getString("nome"), dep, rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionarios: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }
}
