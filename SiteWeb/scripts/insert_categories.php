<?php
// Insertion des catégories principales
$categories = [
    'Vélos', 'Vélos électriques', 'BMX', 'Accessoires', 'Composants', 'Vêtements', 'Chaussures'
];

foreach ($categories as $categorie) {
    // Vérifie si la catégorie principale existe déjà
    $stmt = $pdo->prepare("SELECT idCategorie FROM Categorie WHERE name = :name AND idCategorieMère IS NULL");
    $stmt->execute([':name' => $categorie]);
    $idCategorie = $stmt->fetchColumn();

    if (!$idCategorie) {
        // Insère la catégorie principale si elle n'existe pas
        $pdo->exec("INSERT INTO Categorie (name, idCategorieMère) VALUES ('$categorie', NULL)");
        $idCategorie = $pdo->lastInsertId(); // Récupère l'id de la catégorie principale
    }

    // Insertion des sous-catégories pour chaque catégorie principale
    switch ($categorie) {
        case 'Vélos':
            $subcategories = ['Vélo pour enfant', 'Vélo de ville', 'Vélo de route', 'Gravel', 'VTT', 'VTC'];
            break;
        case 'Vélos électriques':
            $subcategories = ['Vélo de ville électrique', 'Vélo de route électrique', 'Gravel électrique', 'VTT électrique', 'VTC électrique'];
            break;
        case 'BMX':
            $subcategories = ['BMX Race', 'BMX Freestyle', 'BMX Street', 'BMX Park'];
            break;
        case 'Accessoires':
            $subcategories = [
                'Casques', 'Équipement de protection', 'Feux et radars', 'Compteurs', 'Bidons et porte-bidons', 
                'Sacs et sacoches', 'Paniers', 'Pompes', 'Porte-bagages pour vélo', 'Garde-boue', 'Antivols',
                'Sonnettes et avertisseurs', 'Lunettes de vélo', 'Outils et entretien', 'Peinture de retouche',
                'Remorques', 'Accessoires pour téléphone et tablette', 'Range vélo'
            ];
            break;
        case 'Composants':
            $subcategories = [
                'Roues', 'Pneus', 'Feux et radars', 'Selles', 'Pédales', 'Cintres', 'Poignées et ruban de guidon',
                'Accessoires de cintre', 'Potences', 'Tiges de selle', 'Tubes et accessoires tubeless', 
                'Freins et leviers', 'Béquilles', 'Roues stabilisatrices', 'Transmission'
            ];
            break;
        case 'Vêtements':
            $subcategories = [
                'Maillots', 'Shorts et cuissards', 'Gants', 'Vestes et gilets', 'Collants et pantalons',
                'Chaussettes', 'Tenue de l\'équipe pro', 'Vêtements décontractés', 'T-shirts'
            ];
            break;
        case 'Chaussures':
            $subcategories = ['Chaussures Homme', 'Chaussures Femme', 'Chaussures vélo route', 'Chaussures de VTT'];
            break;
        default:
            $subcategories = [];
    }

    foreach ($subcategories as $sub) {
        // Vérifie si la sous-catégorie existe déjà
        $stmt = $pdo->prepare("SELECT idCategorie FROM Categorie WHERE name = :name AND idCategorieMère = :idCategorie");
        $stmt->execute([':name' => $sub, ':idCategorie' => $idCategorie]);

        if (!$stmt->fetchColumn()) {
            // Insère la sous-catégorie
            $pdo->exec("INSERT INTO Categorie (name, idCategorieMère) VALUES ('$sub', $idCategorie)");
        } else {
            echo "La sous-catégorie '$sub' existe déjà pour la catégorie '$categorie'. Skipping insertion.<br>";
        }
    }
}
?>
