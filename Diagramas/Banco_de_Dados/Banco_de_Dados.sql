CREATE TABLE IF NOT EXISTS Usuario (
  id_usuario_pk INT NOT NULL AUTO_INCREMENT,
  nome_usuario VARCHAR(50) NOT NULL,
  email_usuario VARCHAR(50) NOT NULL,
  senha_usuario VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_usuario_pk)
);

CREATE TABLE IF NOT EXISTS Deck (
  id_deck_pk INT NOT NULL AUTO_INCREMENT,
  nome_deck VARCHAR(50) NOT NULL,
  id_usuario_deck_fk INT NOT NULL,
  id_deck_pai_fk INT NULL,
  PRIMARY KEY (id_deck_pk),
  FOREIGN KEY (id_usuario_deck_fk)
    REFERENCES Usuario (id_usuario_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_deck_pai_fk)
    REFERENCES Deck (id_deck_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Carta (
  id_carta_pk INT NOT NULL,
  frente_carta VARCHAR(250) NOT NULL,
  verso_carta VARCHAR(250) NOT NULL,
  nova_carta TINYINT NOT NULL,
  progresso_aprendizado_carta INT NOT NULL,
  proxima_revisao_carta DATETIME NULL,
  data_adicionada_carta DATETIME NOT NULL,
  vezes_revisada_carta INT NOT NULL,
  PRIMARY KEY (id_carta_pk)
);

CREATE TABLE IF NOT EXISTS Nivel (
  id_nivel_pk INT NOT NULL AUTO_INCREMENT,
  nivel INT NOT NULL,
  experiencia_nivel INT NOT NULL,
  id_usuario_nivel_fk INT NOT NULL,
  PRIMARY KEY (id_nivel_pk),
  FOREIGN KEY (id_usuario_nivel_fk)
    REFERENCES Usuario (id_usuario_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Carta_possui_Deck (
  id_carta_fk_pk INT NOT NULL,
  id_deck_fk_pk INT NOT NULL,
  PRIMARY KEY (id_carta_fk_pk, id_deck_fk_pk),
  FOREIGN KEY (id_carta_fk_pk)
    REFERENCES Carta (id_carta_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_deck_fk_pk)
    REFERENCES Deck (id_deck_pk)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE OR REPLACE VIEW Cartas_para_Hoje AS
SELECT * FROM Carta
WHERE DATE(proxima_revisao_carta) = DATE(NOW())
ORDER BY frente_carta;
