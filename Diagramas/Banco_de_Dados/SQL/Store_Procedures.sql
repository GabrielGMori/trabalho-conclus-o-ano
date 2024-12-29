DELIMITER $$

DROP PROCEDURE IF EXISTS get_voos_entre_intervalo $$
CREATE PROCEDURE get_voos_entre_intervalo(IN data_inicial DATETIME, IN data_final DATETIME)
BEGIN
    SELECT * FROM voos
    WHERE horario_partida > data_inicial AND horario_partida < data_final
    ORDER BY horario_partida ASC;
END $$

DROP PROCEDURE IF EXISTS get_voos_por_companhia $$
CREATE PROCEDURE get_voos_por_companhia(IN id INT)
BEGIN
    SELECT * FROM voos
    WHERE id = id_companhia_aerea;
END $$

DROP PROCEDURE IF EXISTS get_voos_por_aeronave $$
CREATE PROCEDURE get_voos_por_aeronave(IN id INT)
BEGIN
    SELECT * FROM voos
    WHERE id = id_aeronave;
END $$

DROP PROCEDURE IF EXISTS get_manutencoes_entre_intervalo $$
CREATE PROCEDURE get_manutencoes_entre_intervalo(IN data_inicial DATETIME, IN data_final DATETIME)
BEGIN
    SELECT * FROM manutencoes
    WHERE data_inicio > data_inicial AND data_inicio < data_final
    ORDER BY data_inicio ASC;
END $$

DROP PROCEDURE IF EXISTS get_manutencoes_por_aeronave $$
CREATE PROCEDURE get_manutencoes_por_aeronave(IN id INT)
BEGIN
    SELECT * FROM manutencoes
    WHERE id = id_aeronave;
END $$

DROP PROCEDURE IF EXISTS get_passagens_por_passageiro $$
CREATE PROCEDURE get_passagens_por_passageiro(IN cpf VARCHAR(11))
BEGIN
    SELECT * FROM passagens
    WHERE cpf_passageiro = cpf
    ORDER BY data_compra ASC;
END $$

DELIMITER ;