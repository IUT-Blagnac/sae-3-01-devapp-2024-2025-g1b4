<?php
// Préparer la requête pour insérer des adresses
$stmtAdresse = $pdo->prepare("
    INSERT INTO Adresse (codePostal, ville, adresse)
    VALUES (:codePostal, :ville, :adresse)
");

// Insérer 20 adresses dans la table Adresse
$adresseIds = []; // Stockera les IDs des adresses insérées
for ($i = 0; $i < 20; $i++) {
    $codePostal = $faker->postcode;
    $ville = $faker->city;
    $adresse = $faker->streetAddress;

    // Insérer l'adresse
    $stmtAdresse->execute([
        ':codePostal' => $codePostal,
        ':ville' => $ville,
        ':adresse' => $adresse
    ]);

    // Ajouter l'ID de l'adresse insérée à la liste
    $adresseIds[] = $pdo->lastInsertId();
}

// Préparer la requête pour insérer des clients
$stmtClient = $pdo->prepare("
    INSERT INTO Client (email, password, role, nom, prenom, numTel, genreC, dateNaissance, Adresse_idAdresse)
    VALUES (:email, :password, :role, :nom, :prenom, :numTel, :genreC, :dateNaissance, :Adresse_idAdresse)
");

// Insérer 50 clients
for ($i = 0; $i < 50; $i++) {
    $email = $faker->unique()->email;
    $password = substr(password_hash($faker->password, PASSWORD_BCRYPT), 0, 8);
    $role = $faker->randomElement(['client', 'admin']); // Aléatoire entre 'client' et 'admin'
    $nom = $faker->lastName;
    $prenom = $faker->firstName;
    $numTel = $faker->numerify('06########'); // Numéro de téléphone français valide
    $genreC = $faker->randomElement(['M', 'F']); // Genre aléatoire
    $dateNaissance = $faker->date('Y-m-d', '2005-12-31'); // Date de naissance avant 2005
    $Adresse_idAdresse = $faker->randomElement($adresseIds); // Choisir une adresse valide aléatoire

    // Insérer le client
    $stmtClient->execute([
        ':email' => $email,
        ':password' => $password,
        ':role' => $role,
        ':nom' => $nom,
        ':prenom' => $prenom,
        ':numTel' => $numTel,
        ':genreC' => $genreC,
        ':dateNaissance' => $dateNaissance,
        ':Adresse_idAdresse' => $Adresse_idAdresse
    ]);
}

echo "20 adresses et 50 clients ont été insérés avec succès avec des mots de passe courts.";
