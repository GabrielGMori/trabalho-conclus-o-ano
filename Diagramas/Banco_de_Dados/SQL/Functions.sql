DELIMITER $$

DROP FUNCTION IF EXISTS verificar_disponibilidade_assento $$
CREATE FUNCTION verificar_disponibilidade_assento (id_voo INT, assento VARCHAR(10)) 
RETURNS BOOLEAN
BEGIN
    DECLARE reservas_encontradas INT;
    DECLARE disponivel BOOLEAN;
    
    SELECT COUNT(*) 
    INTO reservas_encontradas
    FROM Passagem
    WHERE id_voo_passagem_fk = id_voo AND assento_passagem = assento;

    IF reservas_encontradas = 0 THEN
        SET disponivel = TRUE;
    ELSE
        SET disponivel = FALSE;
    END IF;
    RETURN disponivel;
END $$

DROP FUNCTION IF EXISTS verificar_disponibilidade_aeronave_entre $$
CREATE FUNCTION verificar_disponibilidade_aeronave_entre (id_aeronave INT, data_inicial DATETIME, data_final DATETIME) 
RETURNS BOOLEAN
BEGIN
    DECLARE manutencoes_encontradas INT;
    DECLARE voos_encontrados INT;
    DECLARE disponivel BOOLEAN;
    
    SELECT COUNT(*) 
    INTO manutencoes_encontradas
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = id_aeronave AND data_inicio_manutencao < data_final AND data_fim_manutencao > data_inicial  AND NOT status_manutencao = 'Cancelado';

    SELECT COUNT(*) 
    INTO voos_encontrados
    FROM Voo
    WHERE id_aeronave_voo_fk = id_aeronave AND horario_partida_voo < data_final AND horario_chegada_voo > data_inicial AND NOT status_voo = 'Cancelado';

    IF manutencoes_encontradas > 0 THEN
        SET disponivel = FALSE;
    ELSEIF voos_encontrados > 0 THEN
        SET disponivel = FALSE;
    ELSE
        SET disponivel = TRUE;
    END IF;
    
    RETURN disponivel;
END $$

DELIMITER ;
