<?php
include './components/header.php';
include './components/bd.php';

// Récupérer les produits avec leurs images
$reqProduits = $pdo->prepare("SELECT idProduit, imagePath FROM Produit");
$reqProduits->execute();
$produits = $reqProduits->fetchAll();

$uploadDir = './uploads/'; // Répertoire des images
$firstLineProducts = 6;    // Nombre de produits dans la première ligne
$productsPerRow = 7;       // Nombre de produits par ligne à partir de la deuxième
?>

<body>
    <div class="square-container">
        <div class="square-line">
            <!-- Carré d'ajout dans la première ligne -->
            <div class="squareAdd">
                <a href="dashboard.php"><img src="./assets/admin/plus.png" alt="Ajouter un produit" class="squareAdd-img"></a>
            </div>

            <?php
            $productIndex = 0;

            foreach ($produits as $index => $produit) {
                $imagePath = htmlspecialchars($produit['imagePath'], ENT_QUOTES, 'UTF-8');

                // Vérifier si le fichier existe
                $relativePath = $uploadDir . basename($imagePath);
                if (!file_exists($relativePath)) {
                    $relativePath = './assets/default-image.png'; // Image par défaut si le fichier est manquant
                }

                // Nouvelle ligne si nécessaire
                if ($productIndex == $firstLineProducts) {
                    echo '</div><div class="square-line">';
                } elseif ($productIndex > $firstLineProducts && ($productIndex - $firstLineProducts) % $productsPerRow == 0) {
                    echo '</div><div class="square-line">';
                }

                // Affichage du carré produit
                echo '<div class="square">';
                echo '<a href="modif.php?id=' . $produit['idProduit'] . '">';
                echo '<img src="' . $relativePath . '" alt="Image produit" class="square-img">';
                echo '</a>';
                echo '</div>';


                $productIndex++;
            }
            ?>
        </div>
    </div>
</body>

</html>