<?php
// Préparer la requête SQL pour insérer une commande
$stmtCommande = $pdo->prepare("
    INSERT INTO Commande (Client_id, typeLivraison, Adresse_idAdresse, PointRelais_idPointRelais, typePaiement, statut)
    VALUES (:Client_id, :typeLivraison, :Adresse_idAdresse, :PointRelais_idPointRelais, :typePaiement, :statut)
");

// Récupérer les IDs valides des clients, adresses et points relais
$clientIds = $pdo->query("SELECT idClient FROM Client")->fetchAll(PDO::FETCH_COLUMN);
$adresseIds = $pdo->query("SELECT idAdresse FROM Adresse")->fetchAll(PDO::FETCH_COLUMN);
$pointRelaisIds = $pdo->query("SELECT idPointRelais FROM PointRelais")->fetchAll(PDO::FETCH_COLUMN);

if (empty($clientIds)) {
    die("Erreur : Aucun client trouvé dans la table Client.");
}

if (empty($adresseIds)) {
    die("Erreur : Aucun ID d'adresse trouvé dans la table Adresse.");
}

if (empty($pointRelaisIds)) {
    die("Erreur : Aucun ID de point relais trouvé dans la table PointRelais.");
}

// Définir les options possibles
$typeLivraisonOptions = ['Domicile', 'Point Relais', 'Click & Collect'];
$typePaiementOptions = ['Carte Bancaire', 'Paypal', 'Virement', 'Chèque'];
$statutOptions = ['En cours', 'Expédiée', 'Livrée', 'Annulée'];

// Fournir un point relais par défaut
$defaultPointRelaisId = $pointRelaisIds[0]; // Premier point relais comme valeur par défaut

// Insérer 100 commandes
for ($i = 0; $i < 100; $i++) {
    $Client_id = $faker->randomElement($clientIds); // Associer un client valide
    $typeLivraison = $faker->randomElement($typeLivraisonOptions); // Type de livraison aléatoire
    $Adresse_idAdresse = $faker->randomElement($adresseIds); // Toujours associer une adresse valide
    $PointRelais_idPointRelais = ($typeLivraison === 'Point Relais') ? $faker->randomElement($pointRelaisIds) : $defaultPointRelaisId; // Point relais valide ou par défaut
    $typePaiement = $faker->randomElement($typePaiementOptions); // Type de paiement
    $statut = $faker->randomElement($statutOptions); // Statut de la commande

    // Insérer la commande
    $stmtCommande->execute([
        ':Client_id' => $Client_id,
        ':typeLivraison' => $typeLivraison,
        ':Adresse_idAdresse' => $Adresse_idAdresse,
        ':PointRelais_idPointRelais' => $PointRelais_idPointRelais,
        ':typePaiement' => $typePaiement,
        ':statut' => $statut
    ]);
}

echo "100 commandes ont été insérées avec succès.";
