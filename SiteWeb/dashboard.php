<?php
include './components/header.php';
include './components/bd.php';

// Récupérer les données nécessaires
$reqCat = $pdo->prepare("SELECT * FROM Couleur");
$reqCat->execute();

$reqTailles = $pdo->prepare("SELECT * FROM Taille");
$reqTailles->execute();

$reqCategories = $pdo->prepare("SELECT * FROM Categorie");
$reqCategories->execute();

// Vérifier si le formulaire a été soumis
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Récupérer les valeurs des champs du formulaire
    $prix = htmlspecialchars($_POST['prix']);
    $nom = htmlspecialchars($_POST['nom']);
    $description = htmlspecialchars($_POST['description']);

    if (isset($_FILES['imageProd']) && $_FILES['imageProd']['error'] === UPLOAD_ERR_OK) {
        $fileTmpPath = $_FILES['imageProd']['tmp_name'];
        $fileName = $_FILES['imageProd']['name'];
        $fileSize = $_FILES['imageProd']['size'];
        $fileType = $_FILES['imageProd']['type'];

        // Définir le dossier de destination    
        $uploadDir = __DIR__ . '/uploads/';

        if (!is_dir($uploadDir)) {
            echo "Le dossier de destination n'existe pas.";
        }

        // Générer un nom unique pour le fichier
        $uniqueFileName = uniqid('prod_', true) . '.' . pathinfo($fileName, PATHINFO_EXTENSION);

        // Chemin complet pour enregistrer l'image
        $destPath = $uploadDir . $uniqueFileName;

        // Déplacer le fichier téléchargé vers le dossier cible
        if (move_uploaded_file($fileTmpPath, $destPath)) {
            // Stocker le chemin de l'image dans la base de données
            $imagePath = $destPath;
        } else {
            echo "Erreur lors de l'enregistrement de l'image.";
            $imagePath = null; // Pas d'image enregistrée
        }
    } else {
        $imagePath = null; // Pas d'image fournie
    }

    // Récupérer la catégorie (prendre seulement la première sélectionnée si nécessaire)
    $categorie = $_POST['categories'][0] ?? null;

    // Vérifier si une catégorie a été sélectionnée
    if ($categorie && is_numeric($categorie)) {
        // Insérer le produit dans la base de données
        $stmt = $pdo->prepare("INSERT INTO Produit (Categorie_idCategorie, prix, nom, description, imagePath) VALUES (?, ?, ?, ?, ?)");
        $stmt->execute([$categorie, $prix, $nom, $description, $imagePath]);

        // Récupérer l'ID du produit inséré
        $idProduit = $pdo->lastInsertId();


        if (!empty($_POST['couleur']) && !empty($_POST['taille'])) {
            foreach ($_POST['couleur'] as $couleur) {
                foreach ($_POST['taille'] as $taille) {
                    if (is_numeric($couleur) && is_numeric($taille)) {
                        $stmt = $pdo->prepare("INSERT INTO VariantArticle (Produit_idProduit, Couleur_idCouleur, Taille_idTaille) VALUES (?, ?, ?)");
                        $stmt->execute([$idProduit, $couleur, $taille]);
                    }
                }
            }
        }

        echo "
        <script>
        window.onload = function() {
            showNotification('Produit ajouté avec succès! <a href=\"admin.php\">Retour à la liste des produits</a>');
        };
        </script>";
    } else {
        echo "Erreur : aucune catégorie valide sélectionnée.";
    }
}
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
            <div>
                <div class="checkbox-container">
                    <button type="button" id="showCategoriesBtn">Catégories</button>
                    <div id="checkboxDropdownCategories" class="checkbox-dropdown" style="display: none;">
                        <?php
                        // Afficher les cases à cocher pour chaque catégorie
                        foreach ($reqCategories->fetchAll() as $categorie) {
                            echo '<label><input type="checkbox" name="categories[]" value="' . htmlspecialchars($categorie['idCategorie'], ENT_QUOTES, 'UTF-8') . '"> ' . htmlspecialchars($categorie['name'], ENT_QUOTES, 'UTF-8') . '</label><br>';
                        }
                        $reqCategories->closeCursor();
                        ?>
                    </div>
                </div>

                <script>
                    document.getElementById("showCategoriesBtn").addEventListener("click", function() {
                        var checkboxDropdownCategories = document.getElementById("checkboxDropdownCategories");

                        // Alterne l'affichage du conteneur
                        if (checkboxDropdownCategories.style.display === "none") {
                            checkboxDropdownCategories.style.display = "block";
                        } else {
                            checkboxDropdownCategories.style.display = "none";
                        }
                    });
                </script>


                <input type="text" id="prix" name="prix" placeholder="Prix" required>

                <input type="text" id="nom" name="nom" placeholder="Nom" required>

                <div class="checkbox-container">
                    <button type="button" id="showColorsBtn">Couleurs</button>
                    <div id="checkboxDropdown" class="checkbox-dropdown" style="display: none;">
                        <?php
                        // Afficher les cases à cocher pour chaque couleur
                        foreach ($reqCat->fetchAll() as $couleur) {
                            echo '<label><input type="checkbox" name="couleur[]" value="' . htmlspecialchars($couleur['idCouleur'], ENT_QUOTES, 'UTF-8') . '"> '
                                . htmlspecialchars($couleur['nomCouleur'], ENT_QUOTES, 'UTF-8') . '</label><br>';
                        }
                        $reqCat->closeCursor();
                        ?>
                    </div>
                </div>

                <script>
                    document.getElementById("showColorsBtn").addEventListener("click", function() {
                        var checkboxDropdown = document.getElementById("checkboxDropdown");

                        // Alterne l'affichage du conteneur
                        if (checkboxDropdown.style.display === "none") {
                            checkboxDropdown.style.display = "block";
                        } else {
                            checkboxDropdown.style.display = "none";
                        }
                    });
                </script>

                <div class="checkbox-container">
                    <button type="button" id="showSizesBtn">Tailles</button>
                    <div id="checkboxDropdownSizes" class="checkbox-dropdown" style="display: none;">
                        <?php
                        // Afficher les cases à cocher pour chaque taille
                        foreach ($reqTailles->fetchAll() as $taille) {
                            echo '<label><input type="checkbox" name="taille[]" value="' . htmlspecialchars($taille['idTaille'], ENT_QUOTES, 'UTF-8') . '"> '
                                . htmlspecialchars($taille['nomTaille'], ENT_QUOTES, 'UTF-8') . '</label><br>';
                        }
                        $reqTailles->closeCursor();
                        ?>
                    </div>
                </div>

                <script>
                    document.getElementById("showSizesBtn").addEventListener("click", function() {
                        var checkboxDropdownSizes = document.getElementById("checkboxDropdownSizes");

                        // Alterne l'affichage du conteneur
                        if (checkboxDropdownSizes.style.display === "none") {
                            checkboxDropdownSizes.style.display = "block";
                        } else {
                            checkboxDropdownSizes.style.display = "none";
                        }
                    });
                </script>

                <textarea id="description" name="description" placeholder="Description" required></textarea>

                <input class="ajouter" type="submit" value="Ajouter"></input>

            </div>
        </div>
    </form>
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
</html>