<?php
include './components/header.php';
include './components/bd.php';

// Récupérer l'ID du produit depuis l'URL
$idProduit = isset($_GET['id']) ? (int)$_GET['id'] : 0;

if ($idProduit > 0) {
    // Récupérer les informations du produit
    $stmt = $pdo->prepare("SELECT * FROM Produit WHERE idProduit = ?");
    $stmt->execute([$idProduit]);
    $produit = $stmt->fetch();

    // Récupérer les couleurs et tailles associées
    $stmtCouleurs = $pdo->prepare("SELECT Couleur_idCouleur FROM VariantArticle WHERE Produit_idProduit = ?");
    $stmtCouleurs->execute([$idProduit]);
    $couleursAssociees = $stmtCouleurs->fetchAll(PDO::FETCH_COLUMN);

    $stmtTailles = $pdo->prepare("SELECT Taille_idTaille FROM VariantArticle WHERE Produit_idProduit = ?");
    $stmtTailles->execute([$idProduit]);
    $taillesAssociees = $stmtTailles->fetchAll(PDO::FETCH_COLUMN);

    // Récupérer toutes les catégories, couleurs et tailles
    $reqCategories = $pdo->prepare("SELECT * FROM Categorie");
    $reqCategories->execute();
    $categories = $reqCategories->fetchAll();

    $reqCouleurs = $pdo->prepare("SELECT * FROM Couleur");
    $reqCouleurs->execute();
    $couleurs = $reqCouleurs->fetchAll();

    $reqTailles = $pdo->prepare("SELECT * FROM Taille");
    $reqTailles->execute();
    $tailles = $reqTailles->fetchAll();
}

// Vérifier si le formulaire a été soumis
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $prix = htmlspecialchars($_POST['prix']);
    $nom = htmlspecialchars($_POST['nom']);
    $description = htmlspecialchars($_POST['description']);
    $categorie = $_POST['categories'][0] ?? null;

    // Gérer l'image si elle est remplacée
    $imagePath = $produit['imagePath'];
    if (isset($_FILES['imageProd']) && $_FILES['imageProd']['error'] === UPLOAD_ERR_OK) {
        $uploadDir = __DIR__ . '/uploads/';
        $uniqueFileName = uniqid('prod_', true) . '.' . pathinfo($_FILES['imageProd']['name'], PATHINFO_EXTENSION);
        $destPath = $uploadDir . $uniqueFileName;

        if (move_uploaded_file($_FILES['imageProd']['tmp_name'], $destPath)) {
            $imagePath = $destPath;
        }
    }

    // Mettre à jour le produit
    $stmtUpdate = $pdo->prepare("UPDATE Produit SET Categorie_idCategorie = ?, prix = ?, nom = ?, description = ?, imagePath = ? WHERE idProduit = ?");
    $stmtUpdate->execute([$categorie, $prix, $nom, $description, $imagePath, $idProduit]);

    // Mettre à jour les variantes
    $pdo->prepare("DELETE FROM VariantArticle WHERE Produit_idProduit = ?")->execute([$idProduit]);

    foreach ($_POST['couleur'] as $couleur) {
        foreach ($_POST['taille'] as $taille) {
            $stmtVariant = $pdo->prepare("INSERT INTO VariantArticle (Produit_idProduit, Couleur_idCouleur, Taille_idTaille) VALUES (?, ?, ?)");
            $stmtVariant->execute([$idProduit, $couleur, $taille]);
        }
    }

    echo  "<script>
    window.onload = function() {
        showNotification('Produit mis à jour avec succès! <a href=\"admin.php\">Retour à la liste des produits</a>');
    };
    </script>";

    
}

// Vérifier si le bouton "Supprimer" a été cliqué
if (isset($_POST['supprimer'])) {
    // Supprimer les variantes associées au produit
    $stmtDeleteVariants = $pdo->prepare("DELETE FROM VariantArticle WHERE Produit_idProduit = ?");
    $stmtDeleteVariants->execute([$idProduit]);

    // Supprimer le produit lui-même
    $stmtDeleteProduct = $pdo->prepare("DELETE FROM Produit WHERE idProduit = ?");
    $stmtDeleteProduct->execute([$idProduit]);

    // Rediriger vers la page admin après la suppression
    echo "<script>
        window.onload = function() {
            showNotification('Produit supprimé avec succès! <a href=\"admin.php\">Retour à la liste des produits</a>');
        };
    </script>";}

?>

<body>
    <form class="main-container" action="#" method="POST" enctype="multipart/form-data">
        <div class="square-container">
            <div class="squareAdd">
                <!-- Bouton pour choisir une image -->
                <button type="button" id="chooseImageBtn">Choisir une image</button>

                <!-- Input pour sélectionner un fichier, masqué -->
                <input type="file" name="imageProd" id="uploadImage" accept="image/*" style="display: none;">

                <!-- Image de prévisualisation (masquée par défaut) -->
                <img id="previewImage" src="" alt="Prévisualisation de l'image" style="display: none;" class="preview-img">
            </div>


            <script>
                // Références des éléments
                const chooseImageBtn = document.getElementById('chooseImageBtn');
                const uploadImage = document.getElementById('uploadImage');
                const previewImage = document.getElementById('previewImage');

                // Lorsqu'on clique sur le bouton, on déclenche l'input file
                chooseImageBtn.addEventListener('click', function() {
                    uploadImage.click();
                });

                // Lorsque l'utilisateur sélectionne une image
                uploadImage.addEventListener('change', function(event) {
                    const file = event.target.files[0];
                    if (file) {
                        const reader = new FileReader();

                        // Affiche l'image sélectionnée dans le conteneur
                        reader.onload = function(e) {
                            previewImage.src = e.target.result; // Définit l'image comme source
                            previewImage.style.display = 'block'; // Affiche l'image de prévisualisation

                            // Déplace le bouton au-dessus de l'image
                            chooseImageBtn.style.position = 'absolute';
                            chooseImageBtn.style.top = '10px'; // Décalage du bouton au-dessus
                            chooseImageBtn.style.zIndex = '20'; // Met le bouton au-dessus de l'image
                        }

                        // Lit le fichier comme URL de données
                        reader.readAsDataURL(file);
                    }
                });
            </script>

        </div>

        <div class="form-container">

            <div class="checkbox-container">
                <button type="button" id="showCategoriesBtn">Catégories</button>
                <div id="checkboxDropdownCategories" class="checkbox-dropdown">
                    <?php foreach ($categories as $categorie) : ?>
                        <label>
                            <input type="checkbox" name="categories[]" value="<?= $categorie['idCategorie'] ?>" <?= $produit['Categorie_idCategorie'] == $categorie['idCategorie'] ? 'checked' : '' ?>>
                            <?= htmlspecialchars($categorie['name']) ?>
                        </label>
                    <?php endforeach; ?>
                </div>
            </div>

            <input type="text" name="prix" value="<?= htmlspecialchars($produit['prix']) ?>" placeholder="Prix" required>
            <input type="text" name="nom" value="<?= htmlspecialchars($produit['nom']) ?>" placeholder="Nom" required>

            <div class="checkbox-container">
                <button type="button" id="showColorsBtn">Couleurs</button>
                <div id="checkboxDropdown" class="checkbox-dropdown">
                    <?php foreach ($couleurs as $couleur) : ?>
                        <label>
                            <input type="checkbox" name="couleur[]" value="<?= $couleur['idCouleur'] ?>" <?= in_array($couleur['idCouleur'], $couleursAssociees) ? 'checked' : '' ?>>
                            <?= htmlspecialchars($couleur['nomCouleur']) ?>
                        </label>
                    <?php endforeach; ?>
                </div>
            </div>



            <div class="checkbox-container">
                <button type="button" id="showSizesBtn">Tailles</button>
                <div id="checkboxDropdownSizes" class="checkbox-dropdown">
                    <?php foreach ($tailles as $taille) : ?>
                        <label>
                            <input type="checkbox" name="taille[]" value="<?= $taille['idTaille'] ?>" <?= in_array($taille['idTaille'], $taillesAssociees) ? 'checked' : '' ?>>
                            <?= htmlspecialchars($taille['nomTaille']) ?>
                        </label>
                    <?php endforeach; ?>
                </div>
            </div>

            <textarea name="description" placeholder="Description" required><?= htmlspecialchars($produit['description']) ?></textarea>

            <input class="modifier" type="submit" value="Mettre à jour"></input>

            <input class="supprimer" type="submit" value="Supprimer" name="supprimer">

        </div>
    </form>

    <script>
        // Gestion de l'aperçu de l'image lors de la sélection d'un fichier
        document.getElementById('uploadImage').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('previewImage').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });

        // Gestion des boutons pour afficher/masquer les listes déroulantes
        document.getElementById('showCategoriesBtn').addEventListener('click', function() {
            const dropdown = document.getElementById('checkboxDropdownCategories');
            dropdown.style.display = dropdown.style.display === 'none' || !dropdown.style.display ? 'block' : 'none';
        });

        document.getElementById('showColorsBtn').addEventListener('click', function() {
            const dropdown = document.getElementById('checkboxDropdown');
            dropdown.style.display = dropdown.style.display === 'none' || !dropdown.style.display ? 'block' : 'none';
        });

        document.getElementById('showSizesBtn').addEventListener('click', function() {
            const dropdown = document.getElementById('checkboxDropdownSizes');
            dropdown.style.display = dropdown.style.display === 'none' || !dropdown.style.display ? 'block' : 'none';
        });
    </script>

    <script>
        function showNotification(message) {
            const notification = document.createElement('div');
            notification.classList.add('notification');
            notification.innerHTML = message;

            document.body.appendChild(notification);


            // Ajouter la classe pour afficher la notification
            setTimeout(function() {
                notification.classList.add('show');
            }, 100);

            // Masquer la notification après 5 secondes
            setTimeout(function() {
                notification.classList.remove('show');
                setTimeout(function() {
                    notification.remove();
                }, 300); // Attendre la fin de l'animation avant de supprimer l'élément
            }, 5000);
        }
    </script>
</body>