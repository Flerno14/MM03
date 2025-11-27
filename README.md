# Avaliação Mão na Massa Número 3°

Baixar biblioteca de conexão com o MySQL:

    https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-9.5.0.zip


Código para criar o Banco de dados:

    CREATE DATABASE minimercado;
    USE minimercado;

    CREATE TABLE produtos (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        preco DECIMAL(10, 2) NOT NULL,
        estoque INT NOT NULL
    );
