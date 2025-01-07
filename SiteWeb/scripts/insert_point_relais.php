<?php
// Préparer la requête SQL pour insérer un point relais
$stmtPointRelais = $pdo->prepare("
    INSERT INTO PointRelais (nomRelais, adresseRelais, codePostalRelais, villeRelais)
    VALUES (:nomRelais, :adresseRelais, :codePostalRelais, :villeRelais)
");

// Insérer 100 points relais
$idsInseres = [];
for ($i = 1; $i <= 100; $i++) {
    $stmtPointRelais->execute([
        ':nomRelais' => 'Point Relais ' . $i,
        ':adresseRelais' => $faker->streetAddress,
        ':codePostalRelais' => $faker->postcode,
        ':villeRelais' => $faker->city
    ]);

    // Récupérer l'ID généré
    $idsInseres[] = $pdo->lastInsertId();
}

// Afficher les IDs insérés pour vérification
echo "100 points relais ont été insérés avec succès. IDs générés : " . implode(', ', $idsInseres);
