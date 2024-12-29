DELIMITER $$



DROP TRIGGER IF EXISTS verifica_capacidade_aeronave_ao_comprar_passagem$$
CREATE TRIGGER verifica_capacidade_aeronave_ao_comprar_passagem
BEFORE INSERT ON Passagem
FOR EACH ROW
BEGIN
    DECLARE numero_passagens INT;
    DECLARE capacidade_aeronave INT;

    SELECT COUNT(*)
    INTO numero_passagens
    FROM Passagem
    WHERE id_voo_passagem_fk = NEW.id_voo_passagem_fk;

    SELECT aeronave.capacidade_aeronave
    INTO capacidade_aeronave
    FROM Voo voo
    JOIN Aeronave aeronave ON voo.id_aeronave_voo_fk = aeronave.id_aeronave_pk
    WHERE voo.id_voo_pk = NEW.id_voo_passagem_fk;

    IF numero_passagens + 1 > capacidade_aeronave THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Capacidade máxima da aeronave atingida';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_voo_ao_comprar_passagem$$
CREATE TRIGGER verifica_voo_ao_comprar_passagem
BEFORE INSERT ON Passagem
FOR EACH ROW
BEGIN
    DECLARE status_voo_passagem VARCHAR(25);

    SELECT status_voo
    INTO status_voo_passagem
    FROM Voo
    WHERE id_voo_pk = NEW.id_voo_passagem_fk;

    IF status_voo_passagem = 'Em andamento' OR status_voo_passagem = 'Cancelado' OR status_voo_passagem = 'Finalizado' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voo não disponível para a compra de passagens';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_horarios_voo_insert$$
CREATE TRIGGER verifica_horarios_voo_insert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    IF NEW.horario_partida_voo > NEW.horario_chegada_voo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de partida do voo é maior do que o horário de chegada';
    END IF;
END$$

DROP TRIGGER IF EXISTS verifica_horarios_voo_update$$
CREATE TRIGGER verifica_horarios_voo_update
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    IF NEW.horario_partida_voo > NEW.horario_chegada_voo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de partida do voo é maior do que o horário de chegada';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_horarios_manutencao_insert$$
CREATE TRIGGER verifica_horarios_manutencao_insert
BEFORE INSERT ON Manutencao
FOR EACH ROW
BEGIN
    IF NEW.data_inicio_manutencao > NEW.data_fim_manutencao THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de início da manutenção é maior do que o horário de fim';
    END IF;
END$$

DROP TRIGGER IF EXISTS verifica_horarios_manutencao_update$$
CREATE TRIGGER verifica_horarios_manutencao_update
BEFORE UPDATE ON Manutencao
FOR EACH ROW
BEGIN
    IF NEW.data_inicio_manutencao > NEW.data_fim_manutencao THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de início da manutenção é maior do que o horário de fim';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_portao_embarque_voo_insert$$
CREATE TRIGGER verifica_portao_embarque_voo_insert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    DECLARE portao_disponivel TINYINT;

    SELECT portao.disponivel_portao
    INTO portao_disponivel
    FROM Voo voo
    JOIN Portao_Embarque portao ON voo.id_portao_voo_fk = id_portao_pk
    WHERE voo.id_voo_pk = NEW.id_voo_pk;

    IF portao_disponivel = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Portão do voo indisponível';
    END IF;
END$$

DROP TRIGGER IF EXISTS verifica_portao_embarque_voo_update$$
CREATE TRIGGER verifica_portao_embarque_voo_update
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    DECLARE portao_disponivel TINYINT;

    SELECT disponivel_portao
    INTO portao_disponivel
    FROM Portao_Embarque
    WHERE id_portao_pk = NEW.id_portao_voo_fk;

    IF portao_disponivel = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Portão de embarque do voo indisponível';
    END IF;
END$$




 
DROP TRIGGER IF EXISTS verifica_portao_embarque_em_uso_ao_atualizar_status$$
CREATE TRIGGER verifica_portao_embarque_em_uso_ao_atualizar_status
BEFORE UPDATE ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE voos_encontrados INT;

    SELECT COUNT(*)
    INTO voos_encontrados
    FROM Voo
    WHERE id_portao_voo_fk = NEW.id_portao_pk AND status_voo = 'Em andamento';

    IF voos_encontrados > 0 AND NEW.disponivel_portao = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Portão de embarque está em uso';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_manutencoes_aeronave_no_horario_do_voo_insert$$
CREATE TRIGGER verifica_manutencoes_aeronave_no_horario_do_voo_insert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    DECLARE manutencoes_encontradas INT;

    SELECT COUNT(*)
    INTO manutencoes_encontradas
    FROM Manutencao
    WHERE NEW.id_aeronave_voo_fk = id_aeronave_manutencao_fk AND data_inicio_manutencao < NEW.horario_chegada_voo AND data_fim_manutencao > NEW.horario_partida_voo AND NOT status_manutencao = 'Finalizado';

    IF manutencoes_encontradas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Uma manutenção já está agendada para a aeronave no horário do voo';
    END IF;
END$$

DROP TRIGGER IF EXISTS verifica_manutencoes_aeronave_no_horario_do_voo_update$$
CREATE TRIGGER verifica_manutencoes_aeronave_no_horario_do_voo_update
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    DECLARE manutencoes_encontradas INT;

    SELECT COUNT(*)
    INTO manutencoes_encontradas
    FROM Manutencao
    WHERE NEW.id_aeronave_voo_fk = id_aeronave_manutencao_fk AND data_inicio_manutencao < NEW.horario_chegada_voo AND data_fim_manutencao > NEW.horario_partida_voo AND NOT status_manutencao = 'Finalizado';

    IF manutencoes_encontradas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Uma manutenção já está agendada para a aeronave no horário do voo';
    END IF;
END$$





DROP TRIGGER IF EXISTS verifica_voos_aeronave_no_horario_da_manutencao_insert$$
CREATE TRIGGER verifica_voos_aeronave_no_horario_da_manutencao_insert
BEFORE INSERT ON Manutencao
FOR EACH ROW
BEGIN
    DECLARE voos_encontrados INT;

    SELECT COUNT(*)
    INTO voos_encontrados
    FROM Voo
    WHERE NEW.id_aeronave_manutencao_fk = id_aeronave_voo_fk AND NEW.data_inicio_manutencao < horario_chegada_voo AND NEW.data_fim_manutencao > horario_partida_voo AND NOT status_voo = 'Finalizado';

    IF voos_encontrados > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Um voo que utiliza a aeronave já está agendado no horário da manutenção';
    END IF;
END$$

DROP TRIGGER IF EXISTS verifica_voos_aeronave_no_horario_da_manutencao_update$$
CREATE TRIGGER verifica_voos_aeronave_no_horario_da_manutencao_update
BEFORE UPDATE ON Manutencao
FOR EACH ROW
BEGIN
    DECLARE voos_encontrados INT;

    SELECT COUNT(*)
    INTO voos_encontrados
    FROM Voo
    WHERE NEW.id_aeronave_manutencao_fk = id_aeronave_voo_fk AND NEW.data_inicio_manutencao < horario_chegada_voo AND NEW.data_fim_manutencao > horario_partida_voo AND NOT status_voo = 'Finalizado';

    IF voos_encontrados > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Um voo que utiliza a aeronave já está agendado no horário da manutenção';
    END IF;
END$$


DELIMITER ;