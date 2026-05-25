package repositories.mysql;

import db.ConexaoDB;
import entities.Departamento;
import entities.Funcionario;
import entities.Ocorrencia;
import enums.StatusOcorrencia;
import interfaces.IOcorrenciaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaRepositoryMySQL implements IOcorrenciaRepository {

    private final DepartamentoRepositoryMySQL deptoRepo   = new DepartamentoRepositoryMySQL();
    private final FuncionarioRepositoryMySQL  funcRepo    = new FuncionarioRepositoryMySQL();

    @Override
    public void salvar(Ocorrencia ocorrencia) {
        String sql = "INSERT INTO ocorrencia " +
                     "(descricao, data_ocorrencia, codigo_depto_reportante, matricula_funcionario, " +
                     " data_limite, status_temporario, status_definitivo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ocorrencia.getDescricao());
            ps.setDate(2, Date.valueOf(ocorrencia.getDataOcorrencia()));
            ps.setInt(3, ocorrencia.getDeptoReportante().getCodigo());
            ps.setString(4, ocorrencia.getFuncionarioAlocado().getMatricula());
            ps.setDate(5, Date.valueOf(ocorrencia.getDataLimite()));
            ps.setString(6, ocorrencia.getStatusTemporario().getValor());
            ps.setString(7, ocorrencia.getStatusDefinitivo().getValor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar ocorrencia: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Ocorrencia ocorrencia) {
        String sql = "UPDATE ocorrencia SET descricao=?, matricula_funcionario=?, data_limite=?, " +
                     "status_temporario=?, status_definitivo=? WHERE numero=?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ocorrencia.getDescricao());
            ps.setString(2, ocorrencia.getFuncionarioAlocado().getMatricula());
            ps.setDate(3, Date.valueOf(ocorrencia.getDataLimite()));
            ps.setString(4, ocorrencia.getStatusTemporario().getValor());
            ps.setString(5, ocorrencia.getStatusDefinitivo().getValor());
            ps.setInt(6, ocorrencia.getNumero());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ocorrencia: " + e.getMessage(), e);
        }
    }

    @Override
    public Ocorrencia buscarPorNumero(int numero) {
        String sql = "SELECT * FROM ocorrencia WHERE numero = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ocorrencia: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Ocorrencia> listarPorDepartamento(int codigoDepto) {
        List<Ocorrencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM ocorrencia WHERE codigo_depto_reportante = ? ORDER BY numero";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigoDepto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ocorrencias: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Ocorrencia> listarPorFuncionario(String matricula) {
        List<Ocorrencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM ocorrencia WHERE matricula_funcionario = ? ORDER BY numero";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ocorrencias do funcionario: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public int proximoNumero() {
        // MySQL gerencia AUTO_INCREMENT; retornamos o proximo valor esperado
        String sql = "SELECT IFNULL(MAX(numero), 0) + 1 FROM ocorrencia";
        try (Connection conn = ConexaoDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter proximo numero: " + e.getMessage(), e);
        }
        return 1;
    }

    private Ocorrencia mapear(ResultSet rs) throws SQLException {
        Departamento depto = deptoRepo.buscarPorCodigo(rs.getInt("codigo_depto_reportante"));
        Funcionario  func  = funcRepo.buscarPorMatricula(rs.getString("matricula_funcionario"));

        Ocorrencia oc = new Ocorrencia(
            rs.getInt("numero"),
            rs.getString("descricao"),
            rs.getDate("data_ocorrencia").toLocalDate(),
            depto,
            func,
            rs.getDate("data_limite").toLocalDate()
        );

        String statusTemp = rs.getString("status_temporario");
        String statusDef  = rs.getString("status_definitivo");
        oc.setStatusTemporario("encerrada".equalsIgnoreCase(statusTemp)
                ? StatusOcorrencia.ENCERRADA : StatusOcorrencia.ABERTA);
        oc.setStatusDefinitivo("encerrada".equalsIgnoreCase(statusDef)
                ? StatusOcorrencia.ENCERRADA : StatusOcorrencia.ABERTA);
        return oc;
    }
}
