# Frontend - Aplicação de Gerenciamento de Vendas (Swing)

Este é o frontend da aplicação de gerenciamento de vendas desenvolvido em Swing como parte do teste prático para a vaga de Desenvolvedor Java na VR Software.

## Visão Geral

O objetivo desta aplicação é fornecer uma interface gráfica para o usuário gerenciar vendas, clientes e produtos. Ela se integra com o backend (desenvolvido em Java) para realizar as operações de cadastro, consulta, edição e exclusão.

## Funcionalidades Implementadas

*   **Consulta de Clientes:**
    *   Listagem de clientes com opções de ordenação e filtragem (implementadas diretamente em Java).
    *   Janela de criação e edição de clientes (JDialog ou JFrame).
    *   Visualização detalhada de cada cliente.
*   **Cadastro de Clientes:**
    *   Formulário (JPanel) com os campos: Código, Nome, Limite de Compra (valor) e Dia de fechamento da fatura.
    *   Validação dos campos do formulário (implementada em Java).
*   **Consulta de Produtos:**
    *   Listagem de produtos com opções de ordenação e filtragem (implementadas diretamente em Java).
    *   Janela de criação e edição de produtos (JDialog ou JFrame).
    *   Visualização detalhada de cada produto.
*   **Cadastro de Produtos:**
    *   Formulário (JPanel) com os campos: Código, Descrição e Preço.
    *   Validação dos campos do formulário (implementada em Java).
*   **Cadastro de Venda:**
    *   Seleção de um cliente existente (JComboBox ou similar).
    *   Adição de produtos à venda, com a possibilidade de alterar a quantidade.
    *   Validação do limite de crédito do cliente (implementada em Java).
    *   Mensagens de erro claras em caso de limite de crédito excedido ou produtos repetidos (JOptionPane).
*   **Consulta de Vendas:**
    *   Listagem de vendas com opções de filtragem por cliente, produto e período (implementadas diretamente em Java).
    *   Visualização detalhada de cada venda, incluindo os produtos e valores associados.

## Tecnologias Utilizadas

*   **Linguagem:** Java
*   **Framework GUI:** Swing

## Pré-requisitos

*   Java Development Kit (JDK) - versão 8 ou superior (conforme especificado no teste).
*   Banco de dados PostgreSQL configurado e acessível.

## Compilação e Execução

1.  Clone o repositório:

    ```bash
    git clone [URL do Repositório]
    cd [nome-do-projeto]
    ```

2.  Compile o código Java:

    ```bash
    javac src/*.java  # Adapte o caminho se necessário
    ```

3.  Execute a aplicação:

    ```bash
    java MainClass  # Substitua MainClass pelo nome da sua classe principal
    ```

    Certifique-se de que o classpath esteja configurado corretamente para incluir as dependências (ex: driver JDBC).

## Considerações

*   Este frontend foi desenvolvido usando Swing, seguindo as orientações do teste prático.
*   A interface foi construída utilizando componentes Swing padrão (JFrame, JPanel, JTable, JButton, etc.).
*   O design da interface é básico, priorizando a funcionalidade e a clareza.
*   Validações e tratamento de erros foram implementados diretamente no código Java.

## Próximos Passos

*   Implementar tratamento de exceções mais robusto.
*   Adicionar ícones e melhorar a aparência da interface.
*   Utilizar um layout manager mais avançado para tornar a interface mais adaptável.
*   Implementar um sistema de logging.
*   Adicionar testes unitários.