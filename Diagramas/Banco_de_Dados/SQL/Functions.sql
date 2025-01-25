DELIMITER $$


DROP FUNCTION IF EXISTS verificarDisponibilidadeAeronaveFunc$$
CREATE FUNCTION verificarDisponibilidadeAeronaveFunc(id_aeronave INT, data_inicial DATETIME, data_final DATETIME) 
RETURNS BOOLEAN
BEGIN
    DECLARE manutencoes INT;
    DECLARE voos INT;

    IF (data_inicial > data_final) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO manutencoes
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = id_aeronave AND data_inicio_manutencao <= data_final AND data_fim_manutencao >= data_inicial 
    AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada");

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = id_aeronave AND horario_embarque_voo <= data_final AND horario_desembarque_voo >= data_inicial 
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF manutencoes = 0 AND voos = 0 THEN
        RETURN TRUE;
    END IF;
    RETURN FALSE;
END$$


DROP FUNCTION IF EXISTS verificarPortaoEmbarqueEmUsoFunc$$
CREATE FUNCTION verificarPortaoEmbarqueEmUsoFunc(id_portao INT, data_inicial DATETIME, data_final DATETIME) 
RETURNS BOOLEAN
BEGIN
    DECLARE voos INT;

    IF (data_inicial > data_final) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_portao_embarque_voo_fk = id_portao AND horario_embarque_voo <= data_final AND horario_desembarque_voo >= data_inicial
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF voos = 0 THEN
        RETURN FALSE;
    END IF;
    RETURN TRUE;
END$$


DROP FUNCTION IF EXISTS verificarCompanhiaDisponibilidadeAeronavesFunc$$
CREATE FUNCTION verificarCompanhiaDisponibilidadeAeronavesFunc(id_companhia INT)
RETURNS BOOLEAN
BEGIN
    DECLARE id_aeronave INT;
    DECLARE companhia_disponivel BOOLEAN DEFAULT TRUE;
    DECLARE done INT DEFAULT 0;

    DECLARE manutencoes INT;
    DECLARE voos INT;

    DECLARE cursor_aeronaves CURSOR FOR
    SELECT id_aeronave_pk
    FROM Aeronave
    WHERE id_companhia_aeronave_fk = id_companhia;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cursor_aeronaves;

    read_loop: LOOP
        FETCH cursor_aeronaves INTO id_aeronave;

        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT COUNT(*)
        INTO manutencoes
        FROM Manutencao
        WHERE id_aeronave_manutencao_fk = id_aeronave AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada");

        SELECT COUNT(*)
        INTO voos
        FROM Voo
        WHERE id_aeronave_voo_fk = id_aeronave AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

        IF voos > 0 OR manutencoes > 0 THEN
            SET companhia_disponivel = FALSE;
            LEAVE read_loop;
        END IF;
    END LOOP;

    CLOSE cursor_aeronaves;

    RETURN companhia_disponivel;
END$$


DROP FUNCTION IF EXISTS verificarAeroportoPortoesEmUsoFunc$$
CREATE FUNCTION verificarAeroportoPortoesEmUsoFunc(id_aeroporto INT)
RETURNS BOOLEAN
BEGIN
    DECLARE id_portao INT;
    DECLARE aeroporto_em_uso BOOLEAN DEFAULT FALSE;
    DECLARE done INT DEFAULT 0;

    DECLARE voos INT;

    DECLARE cursor_portoes CURSOR FOR
    SELECT id_portao_pk
    FROM Portao_Embarque
    WHERE id_aeroporto_portao_fk = id_aeroporto;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cursor_portoes;

    read_loop: LOOP
        FETCH cursor_portoes INTO id_portao;

        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT COUNT(*)
        INTO voos
        FROM Voo
        WHERE id_portao_embarque_voo_fk = id_portao AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

        IF voos > 0 THEN
            SET aeroporto_em_uso = TRUE;
            LEAVE read_loop;
        END IF;
    END LOOP;

    CLOSE cursor_portoes;

    RETURN aeroporto_em_uso;
END$$


DROP FUNCTION IF EXISTS verificarDisponibilidadeAssentoFunc$$
CREATE FUNCTION verificarDisponibilidadeAssentoFunc(id_voo INT, assento VARCHAR(10))
RETURNS BOOLEAN
BEGIN
    DECLARE passagens INT;

    SELECT COUNT(*)
    INTO passagens
    FROM Passagem
    WHERE id_voo_passagem_fk = id_voo AND assento_passagem = assento;

    IF passagens = 0 THEN
        RETURN TRUE;
    END IF;
    RETURN FALSE;
END$$


DROP FUNCTION IF EXISTS verificarVooLotado$$
CREATE FUNCTION verificarVooLotado(id_voo INT)
RETURNS BOOLEAN
BEGIN
    DECLARE passagens INT;
    DECLARE capacidade INT;

    SELECT COUNT(*) INTO passagens
    FROM Passagem
    WHERE id_voo_passagem_fk = id_voo;

    SELECT capacidade_voo INTO capacidade
    FROM Voo
    WHERE id_voo_pk = id_voo;

    IF passagens < capacidade THEN
        RETURN FALSE;
    END IF;
    RETURN TRUE;
END$$


DELIMITER ;
