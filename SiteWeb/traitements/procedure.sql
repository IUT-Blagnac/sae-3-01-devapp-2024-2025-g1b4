DELIMITER $$

CREATE PROCEDURE insertClient(
    IN p_codePostal VARCHAR(10),
    IN p_ville VARCHAR(255),
    IN p_adresse VARCHAR(255),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_last_name VARCHAR(45),
    IN p_first_name VARCHAR(45),
    IN p_tel VARCHAR(10),
    IN p_sex CHAR(1),
    IN p_birth_day INT,
    IN p_birth_month INT,
    IN p_birth_year INT
)
BEGIN
   
    DECLARE lastInsertId INT;
    DECLARE addressExists INT;

    -- Gestion des erreurs
    DECLARE exit handler for SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Erreur lors de l\'insertion';
    END;

    -- Début de la transaction
    START TRANSACTION;

    -- Vérifier si l'adresse existe déjà
    SELECT COUNT(*) INTO addressExists
    FROM Adresse
    WHERE codePostal = p_codePostal
      AND ville = p_ville
      AND adresse = p_adresse;

    -- Si l'adresse existe, utiliser son ID, sinon l'insérer
    IF addressExists > 0 THEN
        SELECT idAdresse INTO lastInsertId
        FROM Adresse
        WHERE codePostal = p_codePostal
          AND ville = p_ville
          AND adresse = p_adresse
        LIMIT 1;
    ELSE
        INSERT INTO Adresse (codePostal, ville, adresse)
        VALUES (p_codePostal, p_ville, p_adresse);
        
        -- Récupérer l'ID de la nouvelle adresse insérée
        SET lastInsertId = LAST_INSERT_ID();
    END IF;

    -- Insertion dans la table Client
    INSERT INTO Client (
        email, password, role, nom, prenom, numTel, genreC, dateNaissance, Adresse_idAdresse
    ) 
    VALUES (
        p_email,
        p_password,
        'client',
        p_last_name,
        p_first_name,
        p_tel,
        p_sex,
        CONCAT(p_birth_year, '-', LPAD(p_birth_month, 2, '0'), '-', LPAD(p_birth_day, 2, '0')), -- Format de date
        lastInsertId
    );

    COMMIT;
    
END$$

DELIMITER ;
