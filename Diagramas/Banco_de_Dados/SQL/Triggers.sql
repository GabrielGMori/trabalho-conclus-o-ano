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
    IF verificarDisponibilidadeAeronaveFunc(NEW.id_aeronave_manutencao_fk, NEW.data_inicio_manutencao, NEW.data_fim_manutencao) = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período da manutenção';
    END IF;
END$$


DROP TRIGGER IF EXISTS aeronaveVooTrigInsert$$
CREATE TRIGGER aeronaveVooTrigInsert
BEFORE INSERT ON Voo
FOR EACH ROW
BEGIN
    IF verificarDisponibilidadeAeronaveFunc(NEW.id_aeronave_voo_fk, NEW.horario_embarque_voo, NEW.horario_desembarque_voo) = FALSE THEN
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
    DECLARE portao_disponivel BOOLEAN;

    SELECT disponivel_portao
    INTO portao_disponivel
    FROM Portao_Embarque
    WHERE id_portao_pk = NEW.id_portao_embarque_voo_fk;

    IF verificarPortaoEmbarqueEmUsoFunc(NEW.id_portao_embarque_voo_fk, NEW.horario_embarque_voo, NEW.horario_desembarque_voo) = TRUE OR portao_disponivel = FALSE THEN
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
    IF (NOT NEW.id_aeronave_manutencao_fk = OLD.id_aeronave_manutencao_fk OR NOT NEW.data_inicio_manutencao = OLD.data_inicio_manutencao OR NOT NEW.data_fim_manutencao = OLD.data_fim_manutencao
    OR (NOT NEW.status_manutencao = OLD.status_manutencao AND NOT (NEW.status_manutencao = "Cancelada" OR NEW.status_manutencao = "Finalizada")))
    AND verificarDisponibilidadeAeronaveFunc(NEW.id_aeronave_manutencao_fk, NEW.data_inicio_manutencao, NEW.data_fim_manutencao) = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Aeronave não disponível durante o período da manutenção';
    END IF;
END$$


DROP TRIGGER IF EXISTS aeronaveVooTrigUpdate$$
CREATE TRIGGER aeronaveVooTrigUpdate
BEFORE UPDATE ON Voo
FOR EACH ROW
BEGIN
    IF (NOT NEW.id_aeronave_voo_fk = OLD.id_aeronave_voo_fk OR NOT NEW.horario_embarque_voo = OLD.horario_embarque_voo OR NOT NEW.horario_desembarque_voo = OLD.horario_desembarque_voo 
    OR (NOT NEW.status_voo = OLD.status_voo AND NOT (NEW.status_voo = "Cancelado" OR NEW.status_voo = "Finalizado")))
    AND verificarDisponibilidadeAeronaveFunc(NEW.id_aeronave_voo_fk, NEW.horario_embarque_voo, NEW.horario_desembarque_voo) = FALSE THEN
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
    DECLARE portao_disponivel BOOLEAN;

    SELECT disponivel_portao
    INTO portao_disponivel
    FROM Portao_Embarque
    WHERE id_portao_pk = NEW.id_portao_embarque_voo_fk;

    IF (NOT NEW.id_portao_embarque_voo_fk = OLD.id_portao_embarque_voo_fk OR NOT NEW.horario_embarque_voo = OLD.horario_embarque_voo OR NOT NEW.horario_desembarque_voo = OLD.horario_desembarque_voo
    OR (NOT NEW.status_voo = OLD.status_voo AND NOT (NEW.status_voo = "Cancelado" OR NEW.status_voo = "Finalizado")))
    AND verificarPortaoEmbarqueEmUsoFunc(NEW.id_portao_embarque_voo_fk, NEW.horario_embarque_voo, NEW.horario_desembarque_voo) = TRUE OR portao_disponivel = FALSE THEN
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