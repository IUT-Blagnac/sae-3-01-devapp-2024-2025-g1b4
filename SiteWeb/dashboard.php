    <?php include './components/header.php';

    if(isset($_POST["description"])){
        if (!empty($_FILES['imageProd']) AND $_FILES['imageProd']['error'] == 0) {
            // Testons si l'extension est autorisée    
            $infosfichier = pathinfo($_FILES['imageProd']['name']);
            $extension_upload = $infosfichier['extension'];
            $extensions_autorisees = array('jpg');
            if (in_array($extension_upload, $extensions_autorisees) &&  500000 > $_FILES["imageProd"]["size"]) {
                // On peut valider le fichier et le stocker définitivement
                move_uploaded_file($_FILES['imageProd']['tmp_name'], './assets/articlesImages/prod' . "1".'.jpg');
                header("Pragma: no-cache");
            }
            else {
                echo "Le fichier n'est pas du bon type ou il est trop volumineux !<br>";
            }
            // exit();
        } 
    }

    include './components/bd.php';

    // Récupérer les couleurs de la base de données
    $reqCat = $pdo->prepare("SELECT * FROM Couleur");
    $reqCat->execute();

    // Récupérer les couleurs de la base de données
    $reqTailles = $pdo->prepare("SELECT * FROM Taille");
    $reqTailles->execute();


    // Récupérer les catégories de la base de données
    $reqCategories = $pdo->prepare("SELECT * FROM Categorie");
    $reqCategories->execute();

    ?>

    <body>
        <form class="main-container" action="#" method="POST">
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


                    <input type="text" id="field3" name="prix" placeholder="Prix" required>

                    <input type="text" id="field4" name="nom" placeholder="Nom" required>

                    <div class="checkbox-container">
                        <button type="button" id="showColorsBtn">Couleurs</button>
                        <div id="checkboxDropdown" class="checkbox-dropdown" style="display: none;">
                            <?php
                            // Afficher les cases à cocher pour chaque couleur
                            foreach ($reqCat->fetchAll() as $couleur) {
                                echo '<label><input type="checkbox" name="couleur[]" value="' . htmlspecialchars($couleur['idCouleur'], ENT_QUOTES, 'UTF-8') . '"> ' . htmlspecialchars($couleur['nomCouleur'], ENT_QUOTES, 'UTF-8') . '</label><br>';
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
                                echo '<label><input type="checkbox" name="taille[]" value="' . htmlspecialchars($taille['idTaille'], ENT_QUOTES, 'UTF-8') . '"> ' . htmlspecialchars($taille['nomTaille'], ENT_QUOTES, 'UTF-8') . '</label><br>';
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


                    <script>

                        // Fonction de validation
                        function validateForm() {
                            // Vérification des catégories
                            const categoriesChecked = document.querySelectorAll('input[name="categories[]"]:checked');
                            if (categoriesChecked.length === 0) {
                                alert("Veuillez sélectionner au moins une catégorie.");
                                return false;
                            }

                            // Vérification des couleurs
                            const colorsChecked = document.querySelectorAll('input[name="couleur[]"]:checked');
                            if (colorsChecked.length === 0) {
                                alert("Veuillez sélectionner au moins une couleur.");
                                return false;
                            }

                            // Vérification des tailles
                            const sizesChecked = document.querySelectorAll('input[name="taille[]"]:checked');
                            if (sizesChecked.length === 0) {
                                alert("Veuillez sélectionner au moins une taille.");
                                return false;
                            }

                            // Vérification de l'image
                            const imageFile = document.getElementById("uploadImage").files.length;
                            if (imageFile === 0) {
                                alert("Veuillez télécharger une image.");
                                return false;
                            }

                            // Si tout est validé, retourne true
                            return true;
                        }
                    </script>
                </div>
            </div>
        </form>
    </body>

    </html>