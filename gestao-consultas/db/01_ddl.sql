CREATE TABLE usuarios (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          nome VARCHAR(255) NOT NULL,
                          cpf VARCHAR(14) UNIQUE NOT NULL,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          ultimo_login DATETIME,
                          perfil VARCHAR(255) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE pacientes (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           nome VARCHAR(255) NOT NULL,
                           email VARCHAR(50) UNIQUE NOT NULL,
                           cpf VARCHAR(14) UNIQUE NOT NULL,
                           data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           telefone VARCHAR(20)
);

CREATE TABLE dentistas (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           nome VARCHAR(255) NOT NULL,
                           cpf VARCHAR(14) UNIQUE NOT NULL,
                           email VARCHAR(50) UNIQUE NOT NULL,
                           cro VARCHAR(255) NOT NULL,
                           data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE especialidades (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                nome VARCHAR (255) NOT NULL
);

CREATE TABLE dentista_especialidade (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        id_dentista INT NOT NULL,
                                        id_especialidade INT NOT NULL,
                                        FOREIGN KEY (id_dentista) REFERENCES dentistas(id),
                                        FOREIGN KEY (id_especialidade) REFERENCES especialidades(id)
);

CREATE TABLE consultas (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           id_paciente INT NOT NULL,
                           id_dentista INT NOT NULL,
                           id_usuario INT NOT NULL,
                           FOREIGN KEY (id_paciente) REFERENCES pacientes(id),
                           FOREIGN KEY (id_dentista) REFERENCES dentistas(id),
                           FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
                           descricao VARCHAR(255) NOT NULL,
                           motivo_cancelamento VARCHAR(255),
                           data_inicio DATETIME NOT NULL,
                           data_fim DATETIME NOT NULL,
                           data_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           status VARCHAR(255) NOT NULL
);