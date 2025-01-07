<?php
// Préparer la requête SQL pour insérer un avis
$stmtAvis = $pdo->prepare("
    INSERT INTO Avis (Description, Note, Produit_idProduit, Client_idClient)
    VALUES (:Description, :Note, :Produit_idProduit, :Client_idClient)
");

// Récupérer les IDs valides des clients et des produits
$clientIds = $pdo->query("SELECT idClient FROM Client")->fetchAll(PDO::FETCH_COLUMN);
$produitIds = $pdo->query("SELECT idProduit FROM Produit")->fetchAll(PDO::FETCH_COLUMN);

if (empty($clientIds)) {
    die("Erreur : Aucun client trouvé dans la table Client.");
}

if (empty($produitIds)) {
    die("Erreur : Aucun produit trouvé dans la table Produit.");
}

// Insérer 20 avis
for ($i = 0; $i < 20; $i++) {
    $Description = $faker->sentence(10); // Description aléatoire de l'avis
    $Note = $faker->numberBetween(1, 5); // Note aléatoire entre 1 et 5
    $Produit_idProduit = $faker->randomElement($produitIds); // ID produit valide
    $Client_idClient = $faker->randomElement($clientIds); // ID client valide

    // Insérer l'avis
    $stmtAvis->execute([
        ':Description' => $Description,
        ':Note' => $Note,
        ':Produit_idProduit' => $Produit_idProduit,
        ':Client_idClient' => $Client_idClient
    ]);
}

echo "20 avis ont été insérés avec succès.";
