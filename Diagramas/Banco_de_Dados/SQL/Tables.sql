DROP TABLE IF EXISTS Administrador;
DROP TABLE IF EXISTS Passagem;
DROP TABLE IF EXISTS Metodo_Pagamento;
DROP TABLE IF EXISTS CheckIn;
DROP TABLE IF EXISTS Passageiro;
DROP TABLE IF EXISTS Pais;
DROP TABLE IF EXISTS Voo;
DROP TABLE IF EXISTS Portao_Embarque;
DROP TABLE IF EXISTS Aeroporto;
DROP TABLE IF EXISTS Manutencao;
DROP TABLE IF EXISTS Aeronave;
DROP TABLE IF EXISTS Companhia_Aerea;

CREATE TABLE IF NOT EXISTS Companhia_Aerea (
  id_companhia_pk INT NOT NULL AUTO_INCREMENT,
  codigo_icao_companhia VARCHAR(4) NOT NULL,
  nome_companhia VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_companhia_pk)
);


CREATE TABLE IF NOT EXISTS Aeronave (
  id_aeronave_pk INT NOT NULL AUTO_INCREMENT,
  modelo_aeronave VARCHAR(100) NOT NULL,
  capacidade_aeronave INT NOT NULL,
  id_companhia_aeronave_fk INT NOT NULL,
  PRIMARY KEY (id_aeronave_pk),
  FOREIGN KEY (id_companhia_aeronave_fk)
    REFERENCES Companhia_Aerea (id_companhia_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS Manutencao (
  id_manutencao_pk INT NOT NULL AUTO_INCREMENT,
  descricao_manutencao VARCHAR(200) NOT NULL,
  data_inicio_manutencao DATETIME NOT NULL,
  data_fim_manutencao DATETIME NOT NULL,
  status_manutencao VARCHAR(25) NOT NULL,
  id_aeronave_manutencao_fk INT NOT NULL,
  PRIMARY KEY (id_manutencao_pk),
  FOREIGN KEY (id_aeronave_manutencao_fk)
    REFERENCES Aeronave (id_aeronave_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS Aeroporto (
  id_aeroporto_pk INT NOT NULL AUTO_INCREMENT,
  nome_aeroporto VARCHAR(100) NOT NULL,
  localizacao_aeroporto VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_aeroporto_pk)
);


CREATE TABLE IF NOT EXISTS Portao_Embarque (
  id_portao_pk INT NOT NULL AUTO_INCREMENT,
  codigo_portao VARCHAR(20) NOT NULL,
  disponivel_portao TINYINT NOT NULL,
  id_aeroporto_portao_fk INT NOT NULL,
  PRIMARY KEY (id_portao_pk),
  FOREIGN KEY (id_aeroporto_portao_fk)
    REFERENCES Aeroporto (id_aeroporto_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS Voo (
  id_voo_pk INT NOT NULL AUTO_INCREMENT,
  numero_voo VARCHAR(50) NOT NULL,
  status_voo VARCHAR(25) NOT NULL,
  origem_voo VARCHAR(50) NOT NULL,
  destino_voo VARCHAR(50) NOT NULL,
  horario_embarque_voo DATETIME NOT NULL,
  horario_desembarque_voo DATETIME NOT NULL,
  id_aeronave_voo_fk INT NOT NULL,
  id_portao_embarque_voo_fk INT NOT NULL,
  id_aeroporto_chegada_voo_fk INT NOT NULL,
  PRIMARY KEY (id_voo_pk),
  FOREIGN KEY (id_aeronave_voo_fk)
    REFERENCES Aeronave (id_aeronave_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_portao_embarque_voo_fk)
    REFERENCES Portao_Embarque (id_portao_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_aeroporto_chegada_voo_fk)
    REFERENCES Aeroporto (id_aeroporto_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS Pais (
  id_pais_pk INT NOT NULL AUTO_INCREMENT,
  nome_pais VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_pais_pk)
);


CREATE TABLE IF NOT EXISTS Passageiro (
  cpf_passageiro_pk VARCHAR(11) NOT NULL,
  email_passageiro VARCHAR(100) NOT NULL,
  nome_passageiro VARCHAR(100) NOT NULL,
  senha_passageiro VARCHAR(100) NOT NULL,
  passaporte_passageiro VARCHAR(8) NOT NULL,
  telefone_passageiro VARCHAR(11) NOT NULL,
  id_pais_passageiro_fk INT NOT NULL,
  PRIMARY KEY (cpf_passageiro_pk),
  FOREIGN KEY (id_pais_passageiro_fk)
    REFERENCES Pais (id_pais_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS CheckIn (
  id_checkin_pk INT NOT NULL AUTO_INCREMENT,
  data_checkin DATETIME NOT NULL,
  PRIMARY KEY (id_checkin_pk)
);


CREATE TABLE IF NOT EXISTS Metodo_Pagamento (
  id_metodo_pagamento_pk INT NOT NULL AUTO_INCREMENT,
  metodo_pagamento VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_metodo_pagamento_pk)
);


CREATE TABLE IF NOT EXISTS Passagem (
  id_passagem_pk INT NOT NULL AUTO_INCREMENT,
  data_compra_passagem DATETIME NOT NULL,
  cpf_passageiro_passagem_fk VARCHAR(11) NOT NULL,
  id_voo_passagem_fk INT NOT NULL,
  id_metodo_pagamento_passagem_fk INT NOT NULL,
  id_checkin_passagem_fk INT NULL,
  PRIMARY KEY(id_passagem_pk),
  FOREIGN KEY (cpf_passageiro_passagem_fk)
    REFERENCES Passageiro (cpf_passageiro_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_voo_passagem_fk)
    REFERENCES Voo (id_voo_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_checkin_passagem_fk)
    REFERENCES CheckIn (id_checkin_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_metodo_pagamento_passagem_fk)
    REFERENCES Metodo_Pagamento (id_metodo_pagamento_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS Administrador (
  cpf_administrador_pk VARCHAR(11) NOT NULL,
  nome_administrador VARCHAR(100) NOT NULL,
  senha_administrador VARCHAR(100) NOT NULL,
  PRIMARY KEY (cpf_administrador_pk)
);
