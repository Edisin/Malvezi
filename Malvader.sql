-- Criação do banco de dados
CREATE DATABASE sistema_bancario;
USE sistema_bancario;

-- Tabela de usuários
CREATE TABLE cliente_usuario (
    id_cliente_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome_completo VARCHAR(150),
    cpf_usuario VARCHAR(14),
    data_nascimento DATE,
    telefone_usuario VARCHAR(15),
    tipo_usuario ENUM('FUNCIONARIO', 'CLIENTE', 'GERENTE'),
    senha_usuario VARCHAR(100)
);

-- Tabela de funcionários
CREATE TABLE funcionario_detalhes (
    id_funcionario_detalhes INT PRIMARY KEY AUTO_INCREMENT,
    codigo_empregado VARCHAR(25),
    funcao VARCHAR(60),
    id_cliente_usuario INT,
    FOREIGN KEY (id_cliente_usuario)
        REFERENCES cliente_usuario (id_cliente_usuario)
);

-- Tabela de clientes
CREATE TABLE dados_cliente (
    id_dados_cliente INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente_usuario INT,
    FOREIGN KEY (id_cliente_usuario) REFERENCES cliente_usuario(id_cliente_usuario)
);

-- Tabela de endereços
CREATE TABLE enderecos_cliente (
    id_endereco_cliente INT PRIMARY KEY AUTO_INCREMENT,
    cep_cliente VARCHAR(10),
    endereco_cliente VARCHAR(120),
    numero_residencial INT,
    bairro_cliente VARCHAR(50),
    cidade_cliente VARCHAR(50),
    estado_cliente VARCHAR(2),
    id_cliente_usuario INT,
    FOREIGN KEY (id_cliente_usuario) REFERENCES cliente_usuario(id_cliente_usuario)
);

-- Tabela de contas
CREATE TABLE contas_bancarias (
    id_conta_bancaria INT PRIMARY KEY AUTO_INCREMENT,
    numero_conta_bancaria VARCHAR(25),
    agencia_bancaria VARCHAR(10),
    saldo_bancario DECIMAL(18, 2),
    tipo_conta_bancaria ENUM('POUPANCA', 'CORRENTE'),
    id_dados_cliente INT,
    FOREIGN KEY (id_dados_cliente) REFERENCES dados_cliente(id_dados_cliente)
);

-- Tabela de contas correntes
CREATE TABLE conta_corrente_detalhes (
    id_conta_corrente_detalhes INT PRIMARY KEY AUTO_INCREMENT,
    limite_credito DECIMAL(18, 2),
    data_vencimento_credito DATE,
    id_conta_bancaria INT,
    FOREIGN KEY (id_conta_bancaria) REFERENCES contas_bancarias(id_conta_bancaria)
);

-- Tabela de contas poupança
CREATE TABLE conta_poupanca_detalhes (
    id_conta_poupanca_detalhes INT PRIMARY KEY AUTO_INCREMENT,
    taxa_juros DECIMAL(5, 2),
    id_conta_bancaria INT,
    FOREIGN KEY (id_conta_bancaria) REFERENCES contas_bancarias(id_conta_bancaria)
);

-- Tabela de transações
CREATE TABLE transacoes_bancarias (
    id_transacao_bancaria INT PRIMARY KEY AUTO_INCREMENT,
    tipo_transacao_bancaria ENUM('DEPOSITO', 'SAQUE', 'TRANSFERENCIA'),
    valor_transacao DECIMAL(18, 2),
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_conta_bancaria INT,
    FOREIGN KEY (id_conta_bancaria) REFERENCES contas_bancarias(id_conta_bancaria)
);

-- Tabela de relatórios
CREATE TABLE relatorios_bancarios (
    id_relatorio_bancario INT PRIMARY KEY AUTO_INCREMENT,
    tipo_relatorio_bancario VARCHAR(60),
    data_criacao_relatorio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    conteudo_relatorio TEXT,
    id_funcionario_detalhes INT,
    FOREIGN KEY (id_funcionario_detalhes) REFERENCES funcionario_detalhes(id_funcionario_detalhes)
);

-- Tabela de autenticação de usuário
CREATE TABLE autenticacao_usuario (
    cpf_usuario VARCHAR(14) PRIMARY KEY,
    senha_usuario VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL
);

-- Seleção dos dados dos clientes
SELECT dc.id_dados_cliente, cu.nome_completo, cu.cpf_usuario
FROM dados_cliente dc
JOIN cliente_usuario cu ON dc.id_cliente_usuario = cu.id_cliente_usuario;
