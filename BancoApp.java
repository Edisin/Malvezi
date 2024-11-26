package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BancoApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BancoApp().criarGUI());
    }

    // Variáveis globais
    private final Map<String, Conta> contas = new HashMap<>();
    private final Map<String, Funcionario> funcionarios = new HashMap<>();
    private final String senhaAdministrador = "admin123"; // Senha de administrador
    private final String senhaFuncionario = "func123"; // Senha do funcionário

    // Criação da interface gráfica
    private void criarGUI() {
        JFrame frame = new JFrame("Sistema Bancário");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        JButton btnFuncionario = new JButton("Funcionário");
        btnFuncionario.setBounds(126, 138, 112, 23);
        JButton btnCliente = new JButton("Cliente");
        btnCliente.setBounds(126, 77, 112, 23);
        JButton btnSair = new JButton("Sair");
        btnSair.setBounds(126, 195, 112, 23);

        btnFuncionario.addActionListener(e -> mostrarMenuFuncionario());
        btnCliente.addActionListener(e -> mostrarMenuCliente());
        btnSair.addActionListener(e -> System.exit(0));

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(null);
        painelBotoes.add(btnFuncionario);
        painelBotoes.add(btnCliente);
        painelBotoes.add(btnSair);

        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        JLabel lblNewLabel = new JLabel("bem-vindo");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setBounds(148, 36, 135, 23);
        painelBotoes.add(lblNewLabel);

        JLabel lblBancoMalvader = new JLabel("Banco Malvader");
        lblBancoMalvader.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblBancoMalvader.setBounds(126, 11, 221, 14);
        painelBotoes.add(lblBancoMalvader);
        frame.getContentPane().add(painelPrincipal);
        frame.setVisible(true);
    }

    // Método de autenticação
    private void autenticar(String tipoUsuario) {
        String senha = JOptionPane.showInputDialog("Digite a senha de " + tipoUsuario);
        if (tipoUsuario.equals("Administrador") && senha.equals(senhaAdministrador)) {
            mostrarMenuFuncionario();
        } else if (tipoUsuario.equals("Funcionário") && senha.equals(senhaFuncionario)) {
            mostrarMenuFuncionario();
        } else {
            JOptionPane.showMessageDialog(null, "Senha incorreta!");
        }
    }

    // Menu de Funcionário - Modificado para pedir senha
    private void mostrarMenuFuncionario() {
        // Solicita a senha do funcionário antes de mostrar o menu
        String senha = JOptionPane.showInputDialog("Digite a senha do Funcionário:");
        if (senha == null || !senha.equals(senhaFuncionario)) {
            JOptionPane.showMessageDialog(null, "Senha incorreta!");
            return;
        }

        // Se a senha estiver correta, exibe o menu de opções
        String[] opcoes = { "1. Abertura de Conta", "2. Encerramento de Conta", "3. Consulta de Dados",
                "4. Alteração de Dados", "5. Cadastro de Funcionários",
                "6. Geração de Relatórios", "7. Sair" };

        String escolha = (String) JOptionPane.showInputDialog(null, "Escolha uma opção:",
                "Menu Funcionário", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha == null) return; // Cancelou

        switch (escolha) {
            case "1. Abertura de Conta": abrirConta(); break;
            case "2. Encerramento de Conta": encerrarConta(); break;
            case "3. Consulta de Dados": consultarDados(); break;
            case "4. Alteração de Dados": alterarDados(); break;
            case "5. Cadastro de Funcionários": cadastrarFuncionario(); break;
            case "6. Geração de Relatórios": gerarRelatoriosComSenha(); break;
            case "7. Sair": return;
        }
    }

    // Função para gerar relatórios (após solicitar a senha do funcionário)
    private void gerarRelatoriosComSenha() {
        // Solicitar a senha do funcionário antes de permitir o acesso à geração de relatórios
        String senha = JOptionPane.showInputDialog("Digite a senha do Funcionário para acessar relatórios:");
        if (senha == null || !senha.equals(senhaFuncionario)) {
            JOptionPane.showMessageDialog(null, "Senha incorreta! Acesso negado.");
            return;
        }

        // Se a senha estiver correta, permite gerar os relatórios
        gerarRelatorios();
    }

    private void gerarRelatorios() {
        // Função para gerar relatórios (simulada aqui)
        JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso!");
    }

    // Submenus e funções

    // Função para abrir uma nova conta
    private void abrirConta() {
        String tipoConta = JOptionPane.showInputDialog("Digite o tipo de conta (CC para Corrente, CP para Poupança):");
        if (tipoConta == null) return; // Cancelou

        String numeroConta = JOptionPane.showInputDialog("Digite o número da conta:");
        String nomeCliente = JOptionPane.showInputDialog("Digite o nome do cliente:");
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente:");
        String telefone = JOptionPane.showInputDialog("Digite o telefone do cliente:");
        String endereco = JOptionPane.showInputDialog("Digite o endereço do cliente:");

        if (tipoConta.equalsIgnoreCase("CC")) {
            // Conta Corrente
            double limite = Double.parseDouble(JOptionPane.showInputDialog("Digite o limite da conta:"));
            String dataVencimento = JOptionPane.showInputDialog("Digite a data de vencimento do limite:");
            contas.put(numeroConta, new ContaCorrente(numeroConta, nomeCliente, cpf, telefone, endereco, limite, dataVencimento));
        } else {
            // Conta Poupança
            contas.put(numeroConta, new ContaPoupanca(numeroConta, nomeCliente, cpf, telefone, endereco));
        }
        JOptionPane.showMessageDialog(null, "Conta aberta com sucesso!");
    }


    // Função para encerrar uma conta
    private void encerrarConta() {
        // Solicitar a senha de administrador antes de permitir o encerramento
        String senhaAdmin = JOptionPane.showInputDialog("Digite a senha de administrador para encerrar a conta:");
        if (senhaAdmin == null || !senhaAdmin.equals(senhaAdministrador)) {
            JOptionPane.showMessageDialog(null, "Senha incorreta! Acesso negado.");
            return;  // Sai da função se a senha estiver incorreta
        }

        // Solicita o número da conta a ser encerrada
        String numeroConta = JOptionPane.showInputDialog("Digite o número da conta a ser encerrada:");
        if (contas.containsKey(numeroConta)) {
            contas.remove(numeroConta);  // Remove a conta
            JOptionPane.showMessageDialog(null, "Conta encerrada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Conta não encontrada!");
        }
    }

    // Função para consultar dados
    private void consultarDados() {
        String[] opcoes = { "1. Consultar Conta", "2. Consultar Funcionário", "3. Consultar Cliente" };
        String escolha = (String) JOptionPane.showInputDialog(null, "Escolha uma opção:",
                "Consulta de Dados", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha == null) return; // Cancelou

        switch (escolha) {
            case "1. Consultar Conta": consultarConta(); break;
            case "2. Consultar Funcionário": consultarFuncionario(); break;
            case "3. Consultar Cliente": consultarCliente(); break;
        }
    }

    private void consultarConta() {
        String numeroConta = JOptionPane.showInputDialog("Digite o número da conta para consulta:");
        Conta conta = contas.get(numeroConta);
        if (conta != null) {
            JOptionPane.showMessageDialog(null, conta.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Conta não encontrada!");
        }
    }

    private void consultarFuncionario() {
        // Função para consultar os dados do funcionário (simulada aqui)
        JOptionPane.showMessageDialog(null, "Nome: y, Cargo: Gerente, CPF: 123.456.789-00");
    }

    private void consultarCliente() {
        // Função para consultar os dados do cliente (simulada aqui)
        JOptionPane.showMessageDialog(null, "Nome: x, CPF: 987.654.321-00, Endereço: Rua X, 123");
    }

    private void alterarDados() {
        // Função para alteração de dados, como limite de conta, telefone, etc.
    }

    private void cadastrarFuncionario() {
        // Função para cadastro de funcionário
    }

    // Menu de Cliente
    private void mostrarMenuCliente() {
        String[] opcoes = { "1. Saldo", "2. Depósito", "3. Saque", "4. Extrato", "5. Consultar Limite", "6. Sair" };
        String escolha = (String) JOptionPane.showInputDialog(null, "Escolha uma opção:",
                "Menu Cliente", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha == null) return; // Cancelou

        switch (escolha) {
            case "1. Saldo": mostrarSaldo(); break;
            case "2. Depósito": deposito(); break;
            case "3. Saque": saque(); break;
            case "4. Extrato": extrato(); break;
            case "5. Consultar Limite": consultarLimite(); break;
            case "6. Sair": return;
        }
    }

    private void mostrarSaldo() {
        // Função para mostrar saldo
    }

    private void deposito() {
        // Função para realizar depósitos
    }

    private void saque() {
        // Função para realizar saques
    }

    private void extrato() {
        // Função para gerar extrato (simulada aqui)
        JOptionPane.showMessageDialog(null, "Extrato gerado com sucesso!");
    }

    private void consultarLimite() {
        // Função para consultar o limite da conta
    }

    // Classes para representações das contas
    class Conta {
        String numeroConta;
        String nomeCliente;
        String cpf;
        String telefone;
        String endereco;

        Conta(String numeroConta, String nomeCliente, String cpf, String telefone, String endereco) {
            this.numeroConta = numeroConta;
            this.nomeCliente = nomeCliente;
            this.cpf = cpf;
            this.telefone = telefone;
            this.endereco = endereco;
        }

        @Override
        public String toString() {
            return "Conta [Número: " + numeroConta + ", Nome: " + nomeCliente + ", CPF: " + cpf + "]";
        }
    }

    class ContaPoupanca extends Conta {
        ContaPoupanca(String numeroConta, String nomeCliente, String cpf, String telefone, String endereco) {
            super(numeroConta, nomeCliente, cpf, telefone, endereco);
        }
    }

    class ContaCorrente extends Conta {
        double limite;
        String dataVencimento;

        ContaCorrente(String numeroConta, String nomeCliente, String cpf, String telefone, String endereco, double limite, String dataVencimento) {
            super(numeroConta, nomeCliente, cpf, telefone, endereco);
            this.limite = limite;
            this.dataVencimento = dataVencimento;
        }

        @Override
        public String toString() {
            return super.toString() + ", Limite: " + limite + ", Data de Vencimento: " + dataVencimento;
        }
    }

    class Funcionario {
        String cargo;
        String nome;
        String cpf;
        String dataNascimento;
        String telefone;
        String endereco;

        Funcionario(String cargo, String nome, String cpf, String dataNascimento, String telefone, String endereco) {
            this.cargo = cargo;
            this.nome = nome;
            this.cpf = cpf;
            this.dataNascimento = dataNascimento;
            this.telefone = telefone;
            this.endereco = endereco;
        }

        @Override
        public String toString() {
            return "Funcionário [Cargo: " + cargo + ", Nome: " + nome + ", CPF: " + cpf + "]";
        }

    }
     try (
    private Object DatabaseConnection;
    Connection conn = DatabaseConnection.conectar()) {
        String query = "INSERT INTO endereco_cliente (numeroConta, nomeCliente, endereco, telefone) VALUES (?, ?, ?, ?)";
        var stmt = conn.prepareStatement(query);
        stmt.setString(1, numeroConta);
        stmt.setString(2, nomeCliente);
        stmt.setString(3, endereco);
        stmt.setString(4, telefone);
        stmt.executeUpdate();
    } catch (
    SQLException e) {
        e.printStackTrace();
    }

}
