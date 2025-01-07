<?php

session_start();
if (!isset($_GET["idProduit"])) {
    header("Location: " . ($_GET["origin"] ?? "articles.php"));
    exit();
}


$pathToImg = "./assets/accueil/velo_demo.png";

$imgs = "./assets/" . basename($_SERVER["PHP_SELF"], '.php') . "/";

<<<<<<< Updated upstream
if (!isset($_GET["idProduit"]))
    header("Location: " . ($_GET["origin"] ?? "index.php"));
=======
>>>>>>> Stashed changes

require_once './components/bd.php';

try {
    if ($_GET["idProduit"] <= 0) {
        throw new Exception('Identifiant du produit invalide.');
    }


    $req = $pdo->prepare("SELECT * FROM Produit WHERE idProduit=:idProd");
    $req->execute(["idProd" => $_GET["idProduit"]]);

    $prod = $req->fetch();

    if (!$prod) {
        throw new Exception("Le Produit demandé est introuvable !");
    }

<<<<<<< Updated upstream
$variants=$req->fetchAll();
=======
    // Préparer la requête pour appeler la procédure stockée
    $statement = $pdo->prepare('CALL getAvgAvis(:idProd, @avg, @count)');
>>>>>>> Stashed changes

    // Lier le paramètre :idProd à la valeur transmise dans $_GET["idProduit"]
    $idProduit = isset($_GET["idProduit"]) ? (int)$_GET["idProduit"] : 0;
    $statement->bindParam(':idProd', $idProduit, PDO::PARAM_INT);

    // Exécuter la requête
    $statement->execute();

    // Fermer le curseur avant de récupérer les résultats des variables de sortie
    $statement->closeCursor();

    // Récupérer les valeurs des variables MySQL
    $result = $pdo->query('SELECT @avg AS avg, @count AS count');

    $avisProd = $result->fetch();

    if (!$avisProd) {
        throw new Exception("Impossible de récupérer les résultats de la procédure stockée.");
    }

    $avisProd["avg"] = round($avisProd["avg"] * 2) / 2;
} catch (PDOException $e) {
    // Gérer les erreurs liées à PDO
    header("Location: index.php?msg=" . urlencode("Erreur PDO : ") . $e->getMessage());
} catch (Exception $e) {
    // Gérer les autres types d'erreurs
    header("Location: index.php?msg=" . urlencode("Erreur : ") . $e->getMessage());
}

try {

    $req = $pdo->prepare("SELECT * FROM VariantArticle WHERE Produit_idProduit=:idProd");
    $req->execute(["idProd" => $_GET["idProduit"]]);

    $variants = $req->fetchAll();

    if (!$variants)
        throw new Exception("Les tailles et couleurs disponibles n'ont pu être chargées de la base de données!");


    $req = $pdo->prepare("SELECT DISTINCT Couleur_idCouleur, nomCouleur FROM VariantArticle, Couleur WHERE Produit_idProduit=:idProd AND Couleur_idCouleur=idCouleur");
    $req->execute(["idProd" => $_GET["idProduit"]]);

    $colors = $req->fetchAll();

    if (!$colors)
        throw new Exception("Les couleurs disponibles n'ont pu être chargées de la base de données!");

    $req = $pdo->prepare("SELECT DISTINCT Taille_idTaille, nomTaille FROM VariantArticle, Taille WHERE Produit_idProduit=:idProd AND Taille_idTaille=idTaille");
    $req->execute(["idProd" => $_GET['idProduit']]);

    $tailles = $req->fetchAll();

    if (!$colors)
        throw new Exception("Les tailles disponibles n'ont pu être chargées de la base de données!");
} catch (PDOException $e) {
    echo '<script>alert(" Attention ! Erreur PDO ! Les tailles et couleurs disponibles n\'ont pu être correctement chargées")</script>';
} catch (Exception $e) {
    echo '<script>alert(" Attention ! ' . $e->getMessage() . '")</script>';
}

try {
    if (!isset($_SESSION["client_email"]))
        throw new Exception("Client non connecté!");

    $req = $pdo->prepare("SELECT role FROM Client WHERE email=:email");

    $req->execute(["email" => $_SESSION["client_email"]]);

    $role = $req->fetch();

    if (!$role)
        throw new Exception('Attention ! Rôle introuvable ! ');
} catch (PDOException $e) {
    echo '<script>alert(" Attention ! ' . $e->getMessage() . '")</script>';
} catch (Exception $e) {
    $msg = $e->getMessage();

    if (str_starts_with($msg, "Attention"))
        echo '<script>alert("' . $e->getMessage() . '")</script>';

    $role = "";
}

include './components/header.php';

?>


<body>

<<<<<<< Updated upstream
=======

    <div class="stockAlert">
        <h1 id="rupture">Rupture de Stock!!<h1>
        <h1 id="stockBas">Stock très faible! Moins de 5 exemplaires restants!</h1>
    </div>

>>>>>>> Stashed changes
    <div class="main">

        <div class="infoSpace">
            <div class="img" id="img">
                <img src="<?= $pathToImg ?>" alt="">
            </div>

            <div class="info">
                <div class="infoDiv" id="title">
                    <h1><?= $prod['nom'] ?></h1>
                </div>
                <div class="infoDiv" id="avis">
                    <?php
                    for ($i = 0; $i < floor($avisProd["avg"]); $i++) {
                    ?>
                        <img src="<?= $imgs ?>fullStar.png" alt="fullStar" class="star">
                    <?php
                    }
                    echo str_ends_with(strval($avisProd["avg"]), ".5") ?
                        '<img src="' . $imgs . 'halfStar.png" alt="halfStar" class="star">' : (floor($avisProd["avg"]) != 5 ? '<img src="' . $imgs . 'emptyStar.png" alt="emptyStar" class="star">' : "");
                    for ($i = 0; $i < 5 - floor($avisProd["avg"]) - 1; $i++) {
                    ?>
                        <img src="<?= $imgs ?>emptyStar.png" alt="emptyStar" class="star">
                    <?php
                    }
                    ?>
                    <div class="suppAvis">
                        <div id="note"><?= $avisProd["count"] == 0 ?  "~" : $avisProd["avg"] ?>/5</div>
                        <div id="nombre">(<?= $avisProd["count"] == 0 ? "aucun" : $avisProd["count"] ?> avis)</div>
                    </div>
                </div>
                <div class="infoDiv" id="prix"><?= $prod['prix'] ?>€</div>
                <div class="infoDiv" id="description"><?= $prod['description'] ?></div>

                <form action="traitements/traitement_ajoutPanier.php" method="get">
                    <input type="hidden" name="idProd" value="<?= $_GET['idProduit'] ?>" />
                    <select name="couleur" id="color" onChange="updateStock()">
                        <?php
                        foreach ($colors as $color) {
                        ?>
                            <option value="<?= $color["Couleur_idCouleur"] ?>"><?= $color["nomCouleur"] ?></option>
                        <?php
                        }
                        ?>
                    </select>
                    <select name="taille" id="taille" onChange="updateStock()">

                        <?php
                        foreach ($tailles as $taille) {
                        ?>
                            <option value="<?= $taille["Taille_idTaille"] ?>"><?= $taille["nomTaille"] ?></option>
                        <?php
                        }
                        ?>
                    </select>
                    </br>
                    </br>
                    <div class="ajout">
                        <input type="number" name="qte" id="qte" min="1" value="1" />
                        <input id="ajout" class="ajouter" type="submit" value="Ajouter au panier" />
                    </div>
                </form>
            </div>

            <?php
            if ($role == "admin") {
            ?>
                <span class="adminLinks">
                    <a href="./traitements/traitement_supprProduit.php?idProduit=<?= htmlspecialchars($_GET["idProduit"]) ?>" onclick="return confirm('Voulez-vous vraiment supprimer ce produit ?')" class="supprButt">
                        <img src="./assets/consultProd/delete.png" alt="supprimer" class="supprImg">
                    </a>
                    <a href="./modif.php?idProduit=<?= htmlspecialchars($_GET["idProduit"]) ?>" class="modifyButt">
                        <img src="./assets/consultProd/stylo.png" alt="modifier" class="modifyImg">
                    </a>
                </span>
            <?php
            }
            ?>
        </div>
    </div>

    <script>
        var variant=<?= $variants?>;

        function updateStock(){
            var e = document.getElementById("taille");
            var taille = e.options[e.selectedIndex].value;

<<<<<<< Updated upstream
            var e = document.getElementById("color");
            var couleur = e.options[e.selectedIndex].value;
            
            
=======
        function updateStock() {
            try {
                var eTaille = document.getElementById("taille");
                var taille = eTaille.options[eTaille.selectedIndex].value;
                var eCouleur = document.getElementById("color");
                var couleur = eCouleur.options[eCouleur.selectedIndex].value;

                var stockTrouve = false; // Indicateur si on trouve le stock

                for (var i = 0; i < variant.length; i++) {
                    if (variant[i]["Couleur_idCouleur"] == couleur &&
                        variant[i]["Taille_idTaille"] == taille) {

                        document.getElementById("stockDisplay").innerText =
                            "Stock disponible : " + variant[i]["stock"];


                        variant[i]["stock"] = variant[i]["stock"] == null ? 0 : variant[i]["stock"];

                        if (variant[i]['stock'] == 0) {
                            document.getElementById("rupture").hidden = false;
                            document.getElementById("stockBas").hidden = true;
                            document.getElementById("ajout").disabled=true;

                        } else {
                            document.getElementById("ajout").disabled=false;

                            if (variant[i]["stock"] < 5)
                                document.getElementById("stockBas").hidden = false;
                            else
                                document.getElementById("stockBas").hidden = true;

                            document.getElementById("rupture").hidden = true;
                        }
                        stockTrouve = true;
                        break;
                    }
                }

                if (!stockTrouve) {
                    throw new Exception("Stock non Disponible !");
                }

            } catch (e) {
                document.getElementById("stockDisplay").innerText = "Stock non disponible";
                document.getElementById("stockBas").hidden = true;
                document.getElementById("rupture").hidden = true;

                document.getElementById("ajout").disabled=true;
                return;
            }



>>>>>>> Stashed changes
        }
    </script>


</body>