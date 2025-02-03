DELIMITER $$ -- INSERTS -----------------------------------------------------


DROP TRIGGER IF EXISTS emailPassageiroTrigInsert$$
CREATE TRIGGER emailPassageiroTrigInsert
BEFORE INSERT ON Passageiro
FOR EACH ROW
BEGIN
    DECLARE passageiros INT;

    SELECT COUNT(*)
    INTO passageiros
    FROM Passageiro
    WHERE email_passageiro = NEW.email_passageiro;

    IF passageiros > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'E-mail já registrado em outra conta';
    END IF;
END$$


DROP TRIGGER IF EXISTS datasManutencaoTrigInsert$$
CREATE TRIGGER datasManutencaoTrigInsert
BEFORE INSERT ON Manutencao
FOR EACH ROW
BEGIN
    IF NEW.data_inicio_manutencao > NEW.data_fim_manutencao THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data de início da manutenção é maior que a data de fim';
    END IF;
END$$


DROP TRIGGER IF EXISTS datasVooTrigInsert$$
CREATE TRIGGER datasVooTrigInsert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    IF NEW.horario_embarque_voo > NEW.horario_desembarque_voo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de embarque do voo é maior que o horário de desembarque';
    END IF;
END$$


DROP TRIGGER IF EXISTS aeronaveManutencaoTrigInsert$$
CREATE TRIGGER aeronaveManutencaoTrigInsert
BEFORE INSERT ON Manutencao
FOR EACH ROW
BEGIN
    DECLARE manutencoes INT;
    DECLARE voos INT;

    IF (NEW.data_inicio_manutencao > NEW.data_fim_manutencao) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO manutencoes
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = NEW.id_aeronave_manutencao_fk AND data_inicio_manutencao <= NEW.data_fim_manutencao AND data_fim_manutencao >= NEW.data_inicio_manutencao 
    AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada") AND NOT id_manutencao_pk = NEW.id_manutencao_pk;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = NEW.id_aeronave_manutencao_fk AND horario_embarque_voo <= NEW.data_fim_manutencao AND horario_desembarque_voo >= NEW.data_inicio_manutencao 
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF manutencoes > 0 OR voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período da manutenção';
    END IF;
END$$


DROP TRIGGER IF EXISTS aeronaveVooTrigInsert$$
CREATE TRIGGER aeronaveVooTrigInsert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    DECLARE manutencoes INT;
    DECLARE voos INT;

    IF (NEW.horario_embarque_voo > NEW.horario_desembarque_voo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO manutencoes
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = NEW.id_aeronave_voo_fk AND data_inicio_manutencao <= NEW.horario_desembarque_voo AND data_fim_manutencao >= NEW.horario_embarque_voo 
    AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada");

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = NEW.id_aeronave_voo_fk AND horario_embarque_voo <= NEW.horario_desembarque_voo AND horario_desembarque_voo >= NEW.horario_embarque_voo 
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado") AND NOT id_voo_pk = NEW.id_voo_pk;

    IF manutencoes > 0 OR voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período do voo';
    END IF;
END$$


DROP TRIGGER IF EXISTS codigoPortaoEmbarqueTrigInsert$$
CREATE TRIGGER codigoPortaoEmbarqueTrigInsert
BEFORE INSERT ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE portoes INT;

    SELECT COUNT(*)
    INTO portoes
    FROM Portao_Embarque
    WHERE codigo_portao = NEW.codigo_portao AND id_aeroporto_portao_fk = NEW.id_aeroporto_portao_fk;

    IF portoes > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Outro portão de embarque com o mesmo código já existe nesse aeroporto';
    END IF;
END$$


DROP TRIGGER IF EXISTS portaoVooTrigInsert$$
CREATE TRIGGER portaoVooTrigInsert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    DECLARE voos INT;
    DECLARE portao_disponivel BOOLEAN;

    IF (NEW.horario_embarque_voo > NEW.horario_desembarque_voo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_portao_embarque_voo_fk = NEW.id_portao_embarque_voo_fk AND (NEW.horario_desembarque_voo IS NULL OR horario_embarque_voo <= NEW.horario_desembarque_voo) AND (NEW.horario_embarque_voo IS NULL OR horario_desembarque_voo >= NEW.horario_embarque_voo)
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    SELECT disponivel_portao
    INTO portao_disponivel
    FROM Portao_Embarque
    WHERE id_portao_pk = NEW.id_portao_embarque_voo_fk;

    IF voos > 0 OR portao_disponivel = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Portão de embarque não disponível durante o período do voo';
    END IF;
END$$


DROP TRIGGER IF EXISTS codigoPortaoEmbarqueTrigInsert$$
CREATE TRIGGER codigoPortaoEmbarqueTrigInsert
BEFORE INSERT ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE portoes INT;

    SELECT COUNT(*)
    INTO portoes
    FROM Portao_Embarque
    WHERE codigo_portao = NEW.codigo_portao AND id_aeroporto_portao_fk = NEW.id_aeroporto_portao_fk;

    IF portoes > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Outro portão de embarque com o mesmo código já existe nesse aeroporto';
    END IF;
END$$


DELIMITER ;
DELIMITER $$ -- UPDATES -----------------------------------------------------


DROP TRIGGER IF EXISTS disponibilidadePortaoTrigUpdate$$
CREATE TRIGGER disponibilidadePortaoTrigUpdate
BEFORE UPDATE ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE voos INT;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_portao_embarque_voo_fk = NEW.id_portao_pk
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF voos > 0 AND NEW.disponivel_portao = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'O portão de embarque será usado em um voo agendado';
    END IF;
END$$


DROP TRIGGER IF EXISTS emailPassageiroTrigUpdate$$
CREATE TRIGGER emailPassageiroTrigUpdate
BEFORE UPDATE ON Passageiro
FOR EACH ROW
BEGIN
    DECLARE passageiros INT;

    SELECT COUNT(*)
    INTO passageiros
    FROM Passageiro
    WHERE email_passageiro = NEW.email_passageiro;

    IF NOT NEW.email_passageiro = OLD.email_passageiro AND passageiros > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'E-mail já registrado em outra conta';
    END IF;
END$$


DROP TRIGGER IF EXISTS datasManutencaoTrigUpdate$$
CREATE TRIGGER datasManutencaoTrigUpdate
BEFORE UPDATE ON Manutencao
FOR EACH ROW
BEGIN
    IF NEW.data_inicio_manutencao > NEW.data_fim_manutencao THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data de início da manutenção é maior que a data de fim';
    END IF;
END$$


DROP TRIGGER IF EXISTS datasVooTrigUpdate$$
CREATE TRIGGER datasVooTrigUpdate
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    IF NEW.horario_embarque_voo > NEW.horario_desembarque_voo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário de embarque do voo é maior que o horário de desembarque';
    END IF;
END$$


DROP TRIGGER IF EXISTS aeronaveManutencaoTrigUpdate$$
CREATE TRIGGER aeronaveManutencaoTrigUpdate
BEFORE UPDATE ON Manutencao
FOR EACH ROW
BEGIN
    DECLARE manutencoes INT;
    DECLARE voos INT;

    IF (NEW.data_inicio_manutencao > NEW.data_fim_manutencao) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO manutencoes
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = NEW.id_aeronave_manutencao_fk AND data_inicio_manutencao <= NEW.data_fim_manutencao AND data_fim_manutencao >= NEW.data_inicio_manutencao 
    AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada") AND NOT id_manutencao_pk = NEW.id_manutencao_pk;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = NEW.id_aeronave_manutencao_fk AND horario_embarque_voo <= NEW.data_fim_manutencao AND horario_desembarque_voo >= NEW.data_inicio_manutencao 
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF manutencoes > 0 OR voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período da manutenção';
    END IF;
END$$

DROP TRIGGER IF EXISTS aeronaveVooTrigUpdate$$
CREATE TRIGGER aeronaveVooTrigUpdate
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    DECLARE manutencoes INT;
    DECLARE voos INT;

    IF (NEW.horario_embarque_voo > NEW.horario_desembarque_voo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO manutencoes
    FROM Manutencao
    WHERE id_aeronave_manutencao_fk = NEW.id_aeronave_voo_fk AND data_inicio_manutencao <= NEW.horario_desembarque_voo AND data_fim_manutencao >= NEW.horario_embarque_voo 
    AND NOT (status_manutencao = "Cancelada" OR status_manutencao = "Finalizada");

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = NEW.id_aeronave_voo_fk AND horario_embarque_voo <= NEW.horario_desembarque_voo AND horario_desembarque_voo >= NEW.horario_embarque_voo 
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado") AND NOT id_voo_pk = NEW.id_voo_pk;

    IF manutencoes > 0 OR voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período do voo';
    END IF;
END$$


DROP TRIGGER IF EXISTS codigoPortaoEmbarqueTrigUpdate$$
CREATE TRIGGER codigoPortaoEmbarqueTrigUpdate
BEFORE UPDATE ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE portoes INT;

    SELECT COUNT(*)
    INTO portoes
    FROM Portao_Embarque
    WHERE codigo_portao = NEW.codigo_portao AND id_aeroporto_portao_fk = NEW.id_aeroporto_portao_fk;

    IF NOT NEW.codigo_portao = OLD.codigo_portao AND portoes > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Outro portão de embarque com o mesmo código já existe nesse aeroporto';
    END IF;
END$$


DROP TRIGGER IF EXISTS portaoVooTrigUpdate$$
CREATE TRIGGER portaoVooTrigUpdate
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    DECLARE voos INT;
    DECLARE portao_disponivel BOOLEAN;

    IF (NEW.horario_embarque_voo > NEW.horario_desembarque_voo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Data inicial do intervalo é maior do que a data final do intervalo';
    END IF;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_portao_embarque_voo_fk = NEW.id_portao_embarque_voo_fk AND (NEW.horario_desembarque_voo IS NULL OR horario_embarque_voo <= NEW.horario_desembarque_voo) AND (NEW.horario_embarque_voo IS NULL OR horario_desembarque_voo >= NEW.horario_embarque_voo)
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    SELECT disponivel_portao
    INTO portao_disponivel
    FROM Portao_Embarque
    WHERE id_portao_pk = NEW.id_portao_embarque_voo_fk;

    IF voos > 0 OR portao_disponivel = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Portão de embarque não disponível durante o período do voo';
    END IF;
END$$


DELIMITER ;
DELIMITER $$ -- DELETE -----------------------------------------------------


DROP TRIGGER IF EXISTS remocaoAeroportoTrigDelete$$
CREATE TRIGGER remocaoAeroportoTrigDelete
BEFORE DELETE ON Aeroporto
FOR EACH ROW
BEGIN
    IF verificarAeroportoPortoesEmUsoFunc(OLD.id_aeroporto_pk) = TRUE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ao menos um portão de embarque do aeroporto está em uso';
    END IF;
END$$


DROP TRIGGER IF EXISTS remocaoCompanhiaTrigDelete$$
CREATE TRIGGER remocaoCompanhiaTrigDelete
BEFORE DELETE ON Companhia_Aerea
FOR EACH ROW
BEGIN
    IF verificarCompanhiaDisponibilidadeAeronavesFunc(OLD.id_companhia_pk) = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ao menos uma aeronave da companhia aérea não está disponível';
    END IF;
END$$

DROP TRIGGER IF EXISTS remocaoAeronaveTrigDelete$$
CREATE TRIGGER remocaoAeronaveTrigDelete
BEFORE DELETE ON Aeronave
FOR EACH ROW
BEGIN
    DECLARE voos INT;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_aeronave_voo_fk = OLD.id_aeronave_pk
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A aeronave será usada em um voo agendado';
    END IF;
END$$

DROP TRIGGER IF EXISTS remocaoPortaoTrigDelete$$
CREATE TRIGGER remocaoPortaoTrigDelete
BEFORE DELETE ON Portao_Embarque
FOR EACH ROW
BEGIN
    DECLARE voos INT;

    SELECT COUNT(*)
    INTO voos
    FROM Voo
    WHERE id_portao_embarque_voo_fk = OLD.id_portao_pk
    AND NOT (status_voo = "Cancelado" OR status_voo = "Finalizado");

    IF voos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'O portão de embarque será usado em um voo agendado';
    END IF;
END$$

DELIMITER ;