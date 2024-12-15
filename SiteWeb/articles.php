<?php
require_once 'components/bd.php'; // Connexion à la base de données
require_once 'components/header.php'; // Inclusion du header

// Configuration pour la pagination
$articles_par_page = 30;
$page = isset($_GET['page']) && $_GET['page'] > 0 ? (int)$_GET['page'] : 1;
$offset = ($page - 1) * $articles_par_page;

// Récupération des filtres appliqués
$categorie_ids = isset($_GET['categories']) ? $_GET['categories'] : [];
$prix_min = isset($_GET['prix_min']) ? (float)$_GET['prix_min'] : 0;
$prix_max = isset($_GET['prix_max']) ? (float)$_GET['prix_max'] : PHP_INT_MAX;

// Récupération des catégories et sous-catégories
$categories_stmt = $pdo->query("
    SELECT idCategorie, name, idCategorieMère 
    FROM Categorie
    ORDER BY idCategorieMère ASC, name ASC
");
$categories = $categories_stmt->fetchAll(PDO::FETCH_ASSOC);

// Construction de la requête SQL pour les produits
$query = "
    SELECT 
        Produit.*, 
        CASE 
            WHEN Categorie.idCategorieMère IS NOT NULL THEN CatParent.name
            ELSE Categorie.name
        END AS categorie_name
    FROM Produit
    LEFT JOIN Categorie ON Produit.Categorie_idCategorie = Categorie.idCategorie
    LEFT JOIN Categorie AS CatParent ON Categorie.idCategorieMère = CatParent.idCategorie
    WHERE Produit.prix BETWEEN :prix_min AND :prix_max
";

if (!empty($categorie_ids)) {
    $placeholders = implode(',', array_fill(0, count($categorie_ids), '?'));
    $query .= " AND (Produit.Categorie_idCategorie IN ($placeholders) OR Categorie.idCategorieMère IN ($placeholders))";
}
$query .= " LIMIT :offset, :limit";

$stmt = $pdo->prepare($query);
$stmt->bindValue(':prix_min', $prix_min, PDO::PARAM_INT);
$stmt->bindValue(':prix_max', $prix_max, PDO::PARAM_INT);

if (!empty($categorie_ids)) {
    foreach ($categorie_ids as $index => $id) {
        $stmt->bindValue($index + 1, $id, PDO::PARAM_INT);
        $stmt->bindValue($index + 1 + count($categorie_ids), $id, PDO::PARAM_INT);
    }
}
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->bindValue(':limit', $articles_par_page, PDO::PARAM_INT);
$stmt->execute();
$produits = $stmt->fetchAll();

// Récupération du nombre total de produits pour la pagination
$total_query = "
    SELECT COUNT(*) 
    FROM Produit
    LEFT JOIN Categorie ON Produit.Categorie_idCategorie = Categorie.idCategorie
    WHERE Produit.prix BETWEEN :prix_min AND :prix_max
";

if (!empty($categorie_ids)) {
    $total_query .= " AND (Produit.Categorie_idCategorie IN ($placeholders) OR Categorie.idCategorieMère IN ($placeholders))";
}

$total_stmt = $pdo->prepare($total_query);
$total_stmt->bindValue(':prix_min', $prix_min, PDO::PARAM_INT);
$total_stmt->bindValue(':prix_max', $prix_max, PDO::PARAM_INT);
if (!empty($categorie_ids)) {
    foreach ($categorie_ids as $index => $id) {
        $total_stmt->bindValue($index + 1, $id, PDO::PARAM_INT);
        $total_stmt->bindValue($index + 1 + count($categorie_ids), $id, PDO::PARAM_INT);
    }
}
$total_stmt->execute();
$total_articles = $total_stmt->fetchColumn();
$total_pages = ceil($total_articles / $articles_par_page);

// Tableau associant les catégories à leurs images
$images_categorie = [
    'Vélos' => 'assets/articles/velo.png',
    'BMX' => 'assets/articles/BMX.png',
    'Accessoires' => 'assets/articles/accessoire.png',
    'Vélos électriques' => 'assets/articles/electrique.png',
    'Composants' => 'assets/articles/roue.png',
];
?>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des produits</title>
    <link rel="stylesheet" href="style/header/style.css"> <!-- Style pour le header -->
    <link rel="stylesheet" href="style/articles/style.css">
</head>
<body>
    <div class="container">

        <!-- Barre latérale des filtres -->
        <aside class="filters">
            <h3>Filtres</h3>
            <form action="" method="GET">
                <h4>Catégories</h4>
                <ul>
                    <?php foreach ($categories as $categorie): ?>
                        <?php if (!$categorie['idCategorieMère']): ?>
                            <li><strong><?= htmlspecialchars($categorie['name']) ?></strong></li>
                            <!-- Sous-catégories -->
                            <ul>
                                <?php foreach ($categories as $subcat): ?>
                                    <?php if ($subcat['idCategorieMère'] == $categorie['idCategorie']): ?>
                                        <li>
                                            <input type="checkbox" name="categories[]" value="<?= $subcat['idCategorie'] ?>" <?= in_array($subcat['idCategorie'], $categorie_ids) ? 'checked' : '' ?>>
                                            <?= htmlspecialchars($subcat['name']) ?>
                                        </li>
                                    <?php endif; ?>
                                <?php endforeach; ?>
                            </ul>
                        <?php endif; ?>
                    <?php endforeach; ?>
                </ul>
                <h4>Prix</h4>
                <div class="price-range">
                    <input type="number" name="prix_min" placeholder="Min (€)" value="<?= $prix_min ?>" min="0">
                    <input type="number" name="prix_max" placeholder="Max (€)" value="<?= $prix_max ?>" min="0">
                </div>
                <button type="submit">Appliquer</button>
            </form>
        </aside>


        <!-- Section principale des produits -->
        <main class="products">
            <div class="grid">
                <?php if (count($produits) > 0): ?>
                    <?php foreach ($produits as $produit): ?>
                        <div class="product-card">
                            <?php
                            $image = isset($images_categorie[$produit['categorie_name']])
                                ? $images_categorie[$produit['categorie_name']]
                                : 'assets/articles/default.png';
                            ?>
                            <img src="<?= htmlspecialchars($image) ?>" alt="<?= htmlspecialchars($produit['categorie_name']) ?>" class="product-image">
                            <h4><?= htmlspecialchars($produit['nom']) ?></h4>
                            <p class="price"><?= number_format($produit['prix'], 2, ',', ' ') ?> €</p>
                            <p class="description"><?= htmlspecialchars($produit['description']) ?></p>
                        </div>
                    <?php endforeach; ?>
                <?php else: ?>
                    <p>Aucun produit trouvé.</p>
                <?php endif; ?>
            </div>
            <div class="pagination">
            <?php if ($total_pages > 1): ?>
                <?php if ($page > 1): ?>
                    <a href="?page=<?= $page - 1 ?>">&laquo; Précédent</a>
                <?php endif; ?>
                
                <?php for ($i = 1; $i <= $total_pages; $i++): ?>
                    <a href="?page=<?= $i ?>" class="<?= $i == $page ? 'active' : '' ?>"><?= $i ?></a>
                <?php endfor; ?>
                
                <?php if ($page < $total_pages): ?>
                    <a href="?page=<?= $page + 1 ?>">Suivant &raquo;</a>
                <?php endif; ?>
            <?php endif; ?>
        </div>
        </main>
    </div>
</body>
</html>
