<?php
// Préparer la requête SQL pour insérer un produit
$stmtProduit = $pdo->prepare("
    INSERT INTO Produit (Categorie_idCategorie, prix, nom, Description)
    VALUES (:Categorie_idCategorie, :prix, :nom, :Description)
");

// Récupérer les IDs valides des catégories
$categorieIds = $pdo->query("SELECT idCategorie FROM Categorie")->fetchAll(PDO::FETCH_COLUMN);

if (empty($categorieIds)) {
    die("Erreur : Aucun ID de catégorie trouvé dans la table Categorie.");
}

// Insérer 50 produits
$produitIds = [];
for ($i = 0; $i < 50; $i++) {
    $Categorie_idCategorie = $faker->randomElement($categorieIds); // ID de catégorie valide
    $prix = $faker->randomFloat(2, 10, 500); // Prix aléatoire entre 10 et 500
    $nom = $faker->word . ' ' . $faker->word; // Nom du produit
    $Description = $faker->sentence(6); // Description aléatoire

    // Insérer le produit
    $stmtProduit->execute([
        ':Categorie_idCategorie' => $Categorie_idCategorie,
        ':prix' => $prix,
        ':nom' => ucfirst($nom),
        ':Description' => $Description
    ]);

    // Ajouter l'ID du produit inséré à la liste
    $produitIds[] = $pdo->lastInsertId();
}

// Ajouter 8 produits composés (dans une catégorie spécifique)
for ($i = 0; $i < 8; $i++) {
    $Categorie_idCategorie = $faker->randomElement($categorieIds); // ID de catégorie valide
    $prix = $faker->randomFloat(2, 100, 1000); // Prix plus élevé pour les produits composés
    $nom = 'Composé ' . $faker->word;
    $Description = "Produit composé : " . $faker->sentence(6);

    // Insérer le produit composé
    $stmtProduit->execute([
        ':Categorie_idCategorie' => $Categorie_idCategorie,
        ':prix' => $prix,
        ':nom' => ucfirst($nom),
        ':Description' => $Description
    ]);

    // Ajouter l'ID du produit composé à la liste
    $produitIds[] = $pdo->lastInsertId();
}

// Créer des descriptions pour les produits apparentés
for ($i = 0; $i < 25; $i++) {
    // Choisir deux produits aléatoires pour les apparenter
    $produit1 = $faker->randomElement($produitIds);
    $produit2 = $faker->randomElement($produitIds);

    // S'assurer que les deux produits ne sont pas identiques
    while ($produit1 === $produit2) {
        $produit2 = $faker->randomElement($produitIds);
    }

    echo "Produits apparentés : Produit $produit1 est apparenté au Produit $produit2<br>";
    // Si nécessaire, tu peux ajouter un lien logique via des catégories ou des descriptions ici
}

echo "50 produits (dont 8 composés) ont été insérés avec succès.";
