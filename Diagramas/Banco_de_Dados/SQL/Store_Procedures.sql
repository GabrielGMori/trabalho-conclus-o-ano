DELIMITER $$

DROP PROCEDURE IF EXISTS realizarCheckInProc$$
CREATE PROCEDURE realizarCheckInProc(IN id_passagem INT)
BEGIN
    DECLARE id_check_in INT;

    SELECT id_checkin_passagem_fk
    INTO id_check_in
    FROM Passagem
    WHERE id_passagem_pk = id_passagem;

    IF id_check_in IS NULL THEN
        INSERT INTO CheckIn (data_checkin)
        VALUES (NOW());
        
        SET id_check_in = LAST_INSERT_ID();

        UPDATE Passagem
        SET id_checkin_passagem_fk = id_check_in
        WHERE id_passagem_pk = id_passagem;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'O check-in já foi realizado para esta passagem';
    END IF;
END$$

DELIMITER ;
