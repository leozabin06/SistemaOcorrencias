-- ============================================================
--  Sistema de Registro de Ocorrencias - TI
--  Script de criacao do banco de dados MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS ocorrencias_ti
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ocorrencias_ti;

-- ---- Diretor ----
CREATE TABLE IF NOT EXISTS diretor (
    matricula VARCHAR(20)  NOT NULL,
    nome      VARCHAR(100) NOT NULL,
    status    VARCHAR(30)  NOT NULL,
    PRIMARY KEY (matricula)
);

-- ---- Departamento ----
CREATE TABLE IF NOT EXISTS departamento (
    codigo    INT          NOT NULL,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) DEFAULT '',
    status    VARCHAR(30)  NOT NULL,
    PRIMARY KEY (codigo)
);

-- ---- Gerente ----
CREATE TABLE IF NOT EXISTS gerente (
    matricula           VARCHAR(20)  NOT NULL,
    nome                VARCHAR(100) NOT NULL,
    codigo_departamento INT          NOT NULL,
    status              VARCHAR(30)  NOT NULL,
    PRIMARY KEY (matricula),
    FOREIGN KEY (codigo_departamento) REFERENCES departamento(codigo)
);

-- ---- Funcionario ----
CREATE TABLE IF NOT EXISTS funcionario (
    matricula           VARCHAR(20)  NOT NULL,
    nome                VARCHAR(100) NOT NULL,
    codigo_departamento INT          NOT NULL,
    status              VARCHAR(30)  NOT NULL,
    PRIMARY KEY (matricula),
    FOREIGN KEY (codigo_departamento) REFERENCES departamento(codigo)
);

-- ---- Ocorrencia ----
CREATE TABLE IF NOT EXISTS ocorrencia (
    numero                  INT          NOT NULL AUTO_INCREMENT,
    descricao               TEXT         NOT NULL,
    data_ocorrencia         DATE         NOT NULL,
    codigo_depto_reportante INT          NOT NULL,
    matricula_funcionario   VARCHAR(20)  NOT NULL,
    data_limite             DATE         NOT NULL,
    status_temporario       VARCHAR(20)  NOT NULL DEFAULT 'aberta',
    status_definitivo       VARCHAR(20)  NOT NULL DEFAULT 'aberta',
    PRIMARY KEY (numero),
    FOREIGN KEY (codigo_depto_reportante) REFERENCES departamento(codigo),
    FOREIGN KEY (matricula_funcionario)   REFERENCES funcionario(matricula)
);
