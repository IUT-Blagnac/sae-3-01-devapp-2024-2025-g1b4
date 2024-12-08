
-- -----------------------------------------------------
-- Table .`Categorie`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Categorie` ;

CREATE TABLE IF NOT EXISTS .`Categorie` (
  `idCategorie` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `idCategorieMère` INT NULL,
  PRIMARY KEY (`idCategorie`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_Categorie_Categorie1_idx` (`idCategorieMère` ASC) VISIBLE,
  CONSTRAINT `fk_Categorie_Categorie1`
    FOREIGN KEY (`idCategorieMère`)
    REFERENCES .`Categorie` (`idCategorie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table .`Produit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Produit` ;

CREATE TABLE IF NOT EXISTS .`Produit` (
  `idProduit` INT NOT NULL AUTO_INCREMENT,
  `Categorie_idCategorie` INT NOT NULL,
  `prix` DOUBLE NULL,
  `nom` VARCHAR(45) NULL,
  `Description` VARCHAR(256) NULL,
  PRIMARY KEY (`idProduit`),
  INDEX `fk_Produit_Categorie1_idx` (`Categorie_idCategorie` ASC) VISIBLE,
  CONSTRAINT `fk_Produit_Categorie1`
    FOREIGN KEY (`Categorie_idCategorie`)
    REFERENCES .`Categorie` (`idCategorie`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Adresse`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Adresse` ;

CREATE TABLE IF NOT EXISTS .`Adresse` (
  `idAdresse` INT NOT NULL AUTO_INCREMENT,
  `codePostal` CHAR(5) NULL,
  `ville` VARCHAR(45) NULL,
  `adresse` VARCHAR(80) NULL,
  PRIMARY KEY (`idAdresse`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Client`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Client` ;

CREATE TABLE IF NOT EXISTS .`Client` (
  `idClient` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NULL,
  `password` VARCHAR(32) NOT NULL,
  `role` VARCHAR(10) NOT NULL,
  `nom` VARCHAR(45) NULL,
  `prenom` VARCHAR(45) NULL,
  `numTel` CHAR(10) NOT NULL,
  `genreC` CHAR(1) NULL,
  `dateNaissance` DATE NULL,
  `Adresse_idAdresse` INT NULL DEFAULT 0,
  PRIMARY KEY (`idClient`),
  UNIQUE INDEX `username_UNIQUE` (`idClient` ASC) VISIBLE,
  INDEX `fk_Client_Adresse1_idx` (`Adresse_idAdresse` ASC) VISIBLE,
  CONSTRAINT `fk_Client_Adresse1`
    FOREIGN KEY (`Adresse_idAdresse`)
    REFERENCES .`Adresse` (`idAdresse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table .`PointRelais`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`PointRelais` ;

CREATE TABLE IF NOT EXISTS .`PointRelais` (
  `idPointRelais` INT NOT NULL AUTO_INCREMENT,
  `nomRelais` VARCHAR(45) NULL,
  `adresseRelais` VARCHAR(45) NULL,
  `codePostalRelais` CHAR(5) NULL,
  `villeRelais` VARCHAR(45) NULL,
  PRIMARY KEY (`idPointRelais`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Commande`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Commande` ;

CREATE TABLE IF NOT EXISTS .`Commande` (
  `idCommande` INT NOT NULL AUTO_INCREMENT,
  `Client_id` INT NOT NULL,
  `typeLivraison` VARCHAR(45) NULL,
  `Adresse_idAdresse` INT NOT NULL,
  `PointRelais_idPointRelais` INT NOT NULL,
  `typePaiement` VARCHAR(16) NULL,
  `statut` VARCHAR(45) NULL DEFAULT 'En Cours',
  PRIMARY KEY (`idCommande`),
  INDEX `fk_Commande_Client1_idx` (`Client_id` ASC) VISIBLE,
  INDEX `fk_Commande_Adresse1_idx` (`Adresse_idAdresse` ASC) VISIBLE,
  INDEX `fk_Commande_PointRelais1_idx` (`PointRelais_idPointRelais` ASC) VISIBLE,
  CONSTRAINT `fk_Commande_Client1`
    FOREIGN KEY (`Client_id`)
    REFERENCES .`Client` (`idClient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Commande_Adresse1`
    FOREIGN KEY (`Adresse_idAdresse`)
    REFERENCES .`Adresse` (`idAdresse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Commande_PointRelais1`
    FOREIGN KEY (`PointRelais_idPointRelais`)
    REFERENCES .`PointRelais` (`idPointRelais`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Couleur`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Couleur` ;

CREATE TABLE IF NOT EXISTS .`Couleur` (
  `idCouleur` INT NOT NULL AUTO_INCREMENT,
  `nomCouleur` VARCHAR(64) NULL,
  PRIMARY KEY (`idCouleur`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Taille`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Taille` ;

CREATE TABLE IF NOT EXISTS .`Taille` (
  `idTaille` INT NOT NULL AUTO_INCREMENT,
  `nomTaille` VARCHAR(45) NULL,
  PRIMARY KEY (`idTaille`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`VariantArticle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`VariantArticle` ;

CREATE TABLE IF NOT EXISTS .`VariantArticle` (
  `Produit_idProduit` INT NOT NULL,
  `Couleur_idCouleur` INT NOT NULL,
  `Taille_idTaille` INT NOT NULL,
  `Stock` INT NULL,
  `SeuilMin` INT NULL,
  `SeuilMax` INT NULL,
  PRIMARY KEY (`Produit_idProduit`, `Couleur_idCouleur`, `Taille_idTaille`),
  INDEX `fk_Produit_has_Couleur_Couleur1_idx` (`Couleur_idCouleur` ASC) VISIBLE,
  INDEX `fk_Produit_has_Couleur_Produit1_idx` (`Produit_idProduit` ASC) VISIBLE,
  INDEX `fk_Produit_has_Couleur_Taille1_idx` (`Taille_idTaille` ASC) VISIBLE,
  CONSTRAINT `fk_Produit_has_Couleur_Produit1`
    FOREIGN KEY (`Produit_idProduit`)
    REFERENCES .`Produit` (`idProduit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Produit_has_Couleur_Couleur1`
    FOREIGN KEY (`Couleur_idCouleur`)
    REFERENCES .`Couleur` (`idCouleur`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Produit_has_Couleur_Taille1`
    FOREIGN KEY (`Taille_idTaille`)
    REFERENCES .`Taille` (`idTaille`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Composer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Composer` ;

CREATE TABLE IF NOT EXISTS .`Composer` (
  `Commande_idCommande` INT NOT NULL,
  `Quantité` VARCHAR(45) NULL,
  `Produit_has_Couleur_Produit_idProduit` INT NOT NULL,
  `Produit_has_Couleur_Couleur_idCouleur` INT NOT NULL,
  `Produit_has_Couleur_Taille_idTaille` INT NOT NULL,
  PRIMARY KEY (`Commande_idCommande`, `Produit_has_Couleur_Produit_idProduit`, `Produit_has_Couleur_Couleur_idCouleur`, `Produit_has_Couleur_Taille_idTaille`),
  INDEX `fk_Produit_has_Commande_Commande1_idx` (`Commande_idCommande` ASC) VISIBLE,
  INDEX `fk_Composer_Produit_has_Couleur1_idx` (`Produit_has_Couleur_Produit_idProduit` ASC, `Produit_has_Couleur_Couleur_idCouleur` ASC, `Produit_has_Couleur_Taille_idTaille` ASC) VISIBLE,
  CONSTRAINT `fk_Produit_has_Commande_Commande1`
    FOREIGN KEY (`Commande_idCommande`)
    REFERENCES .`Commande` (`idCommande`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Composer_Produit_has_Couleur1`
    FOREIGN KEY (`Produit_has_Couleur_Produit_idProduit` , `Produit_has_Couleur_Couleur_idCouleur` , `Produit_has_Couleur_Taille_idTaille`)
    REFERENCES .`VariantArticle` (`Produit_idProduit` , `Couleur_idCouleur` , `Taille_idTaille`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Panier`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Panier` ;

CREATE TABLE IF NOT EXISTS .`Panier` (
  `Client_id` INT NOT NULL,
  `quantite` VARCHAR(45) NULL,
  `ProduitTailleCouleur_Produit_idProduit` INT NOT NULL,
  `ProduitTailleCouleur_Couleur_idCouleur` INT NOT NULL,
  `ProduitTailleCouleur_Taille_idTaille` INT NOT NULL,
  PRIMARY KEY (`Client_id`, `ProduitTailleCouleur_Produit_idProduit`, `ProduitTailleCouleur_Couleur_idCouleur`, `ProduitTailleCouleur_Taille_idTaille`),
  INDEX `fk_Produit_has_Client_Client1_idx` (`Client_id` ASC) VISIBLE,
  INDEX `fk_Panier_ProduitTailleCouleur1_idx` (`ProduitTailleCouleur_Produit_idProduit` ASC, `ProduitTailleCouleur_Couleur_idCouleur` ASC, `ProduitTailleCouleur_Taille_idTaille` ASC) VISIBLE,
  CONSTRAINT `fk_Produit_has_Client_Client1`
    FOREIGN KEY (`Client_id`)
    REFERENCES .`Client` (`idClient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Panier_ProduitTailleCouleur1`
    FOREIGN KEY (`ProduitTailleCouleur_Produit_idProduit` , `ProduitTailleCouleur_Couleur_idCouleur` , `ProduitTailleCouleur_Taille_idTaille`)
    REFERENCES .`VariantArticle` (`Produit_idProduit` , `Couleur_idCouleur` , `Taille_idTaille`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Virement`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Virement` ;

CREATE TABLE IF NOT EXISTS .`Virement` (
  `numVirement` INT NOT NULL,
  `Commande_idCommande` INT NOT NULL,
  PRIMARY KEY (`numVirement`),
  INDEX `fk_Virement_Commande1_idx` (`Commande_idCommande` ASC) VISIBLE,
  CONSTRAINT `fk_Virement_Commande1`
    FOREIGN KEY (`Commande_idCommande`)
    REFERENCES .`Commande` (`idCommande`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Paypal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Paypal` ;

CREATE TABLE IF NOT EXISTS .`Paypal` (
  `numPaiement` INT NOT NULL AUTO_INCREMENT,
  `Commande_idCommande` INT NOT NULL,
  PRIMARY KEY (`numPaiement`),
  INDEX `fk_Paypal_Commande1_idx` (`Commande_idCommande` ASC) VISIBLE,
  CONSTRAINT `fk_Paypal_Commande1`
    FOREIGN KEY (`Commande_idCommande`)
    REFERENCES .`Commande` (`idCommande`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`CarteBancaire`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`CarteBancaire` ;

CREATE TABLE IF NOT EXISTS .`CarteBancaire` (
  `idCarteBancaire` INT NOT NULL,
  `numeroBancaire` CHAR(16) NULL,
  `dateExpiration` DATE NULL,
  `cvv` TINYINT(3) NULL,
  `Client_id` INT NOT NULL,
  PRIMARY KEY (`idCarteBancaire`),
  INDEX `fk_CarteBancaire_Client1_idx` (`Client_id` ASC) VISIBLE,
  CONSTRAINT `fk_CarteBancaire_Client1`
    FOREIGN KEY (`Client_id`)
    REFERENCES .`Client` (`idClient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`CarteBancaire`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`CarteBancaire` ;

CREATE TABLE IF NOT EXISTS .`CarteBancaire` (
  `idCarteBancaire` INT NOT NULL,
  `numeroBancaire` CHAR(16) NULL,
  `dateExpiration` DATE NULL,
  `cvv` TINYINT(3) NULL,
  `Client_id` INT NOT NULL,
  PRIMARY KEY (`idCarteBancaire`),
  INDEX `fk_CarteBancaire_Client1_idx` (`Client_id` ASC) VISIBLE,
  CONSTRAINT `fk_CarteBancaire_Client1`
    FOREIGN KEY (`Client_id`)
    REFERENCES .`Client` (`idClient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table .`Avis`
-- -----------------------------------------------------
DROP TABLE IF EXISTS .`Avis` ;

CREATE TABLE IF NOT EXISTS .`Avis` (
  `idAvis` INT NOT NULL AUTO_INCREMENT,
  `Description` VARCHAR(200) NULL,
  `Note` INT NULL,
  `Produit_idProduit` INT NOT NULL,
  `Client_idClient` INT NOT NULL,
  PRIMARY KEY (`idAvis`, `Produit_idProduit`, `Client_idClient`),
  INDEX `fk_Avis_Produit1_idx` (`Produit_idProduit` ASC) VISIBLE,
  INDEX `fk_Avis_Client1_idx` (`Client_idClient` ASC) VISIBLE,
  CONSTRAINT `fk_Avis_Produit1`
    FOREIGN KEY (`Produit_idProduit`)
    REFERENCES .`Produit` (`idProduit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Avis_Client1`
    FOREIGN KEY (`Client_idClient`)
    REFERENCES .`Client` (`idClient`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


