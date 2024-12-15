<?php
include './components/header.php';

$pathToImg = "./assets/accueil/velo_demo.png";

$imgs = "./assets/" . basename($_SERVER["PHP_SELF"], '.php') . "/";

if (!isset($_GET["idProduit"]))
    header("Location: " . ($_GET["origin"] ?? "index.php"));

require_once './components/bd.php';

$req = $pdo->prepare("SELECT * FROM Produit WHERE idProduit=:idProd");
$req->execute(["idProd" => $_GET["idProduit"]]);

$prod = $req->fetch();

$req = $pdo->prepare("SELECT DISTINCT Couleur_idCouleur, nomCouleur FROM VariantArticle, Couleur WHERE Produit_idProduit=:idProd AND Couleur_idCouleur=idCouleur");
$req->execute(["idProd" => $_GET["idProduit"]]);

$colors = $req->fetchAll();


$req = $pdo->prepare("SELECT DISTINCT Taille_idTaille, nomTaille FROM VariantArticle, Taille WHERE Produit_idProduit=:idProd AND Taille_idTaille=idTaille");
$req->execute(["idProd" => $_GET['idProduit']]);

$tailles = $req->fetchAll();

$req = $pdo->prepare("SELECT * FROM VariantArticle WHERE Produit_idProduit=:idProd");
$req->execute(["idProd" => $_GET["idProduit"]]);

$variants = $req->fetchAll();

// on prépare la requête
$statement = $pdo->prepare('CALL getAvgAvis(:idProd, @avg, @count)');
// on donne la valeur de $pIdGrp au paramètre :idGrp de la requete préparée, en précisant que c'est de l'INTeger.
$statement->bindParam(':idProd', $_GET["idProduit"], PDO::PARAM_INT);
// on exécute la requete, donc la procédure stockée
$statement->execute();
// il faut fermer le cursor sinon on ne peut pas récupérer la valeur retournée par la procédure stockée
$statement->closeCursor();

// on prépare la requête de récupération du paramètre OUT de la procédure stockée
$resultat = $pdo->prepare("select @avg as avg,@count as count");
// on execute
$resultat->execute();
// on récupere le résultat de cette requete
$avisProd = $resultat->fetch();

$avisProd["avg"] = round($avisProd["avg"] * 2) / 2;

?>

<body>

    
<div class="stockAlert">
    <h1 id="rupture">Rupture de Stock!!<h1>
    <h1 id="stockBas">Stock très faible! Moins de 5 exemplaires restants!</h1>
</div>

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
                    <input type="hidden" name="isAvailable" id="isAvailable" value="oui"/>
                    <select name="couleur" id="color" onChange="updateStock()">
                        <?php
                        foreach ($colors as $color) {
                        ?>
                            <option value="<?= $color["Couleur_idCouleur"] ?>"><?= $color["nomCouleur"] ?></option>
                        <?php
                        }
                        ?>
                    </select>
                    <select name="taille" id="taille" onChange='updateStock()'>

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
                        <input class="ajouter" type="submit" value="Ajouter au panier" />
                    </div>
                    <br><br>
                    <div id="stockDisplay">Stock disponible : --</div>
                </form>
            </div>
        </div>
    </div>


    <script>
        <?php
        echo 'var variant=' . json_encode($variants) . ';';
        ?>


        function updateStock() {

            var eTaille = document.getElementById("taille");
            var taille = eTaille.options[eTaille.selectedIndex].value;

            var eCouleur = document.getElementById("color");
            var couleur = eCouleur.options[eCouleur.selectedIndex].value;

            // Parcourir le tableau des variantes pour trouver la correspondance
            var stockTrouve = false; // Indicateur si on trouve le stock
            for (var i = 0; i < variant.length; i++) {
                if (
                    variant[i]["Couleur_idCouleur"] == couleur &&
                    variant[i]["Taille_idTaille"] == taille
                ) {
                    // Exemple : mise à jour d'un champ HTML
                    document.getElementById("stockDisplay").innerText =
                        "Stock disponible : " + variant[i]["stock"];



                    if(variant[i]['stock']==0){
                        document.getElementById("isAvailable").value="non";
                        document.getElementById("rupture").hidden=false;
                        document.getElementById("stockBas").hidden=true;
                    }else{
                        if(variant[i]["stock"]<5)
                            document.getElementById("stockBas").hidden=false;
                        else
                            document.getElementById("stockBas").hidden=true;

                        document.getElementById("isAvailable").value="oui";
                        document.getElementById("rupture").hidden=true;
                    }
                    stockTrouve = true;
                    break; 
                }
            }

            if (!stockTrouve) {
                document.getElementById("stockDisplay").innerText = "Stock non disponible";
            }
        }

        updateStock();
    </script>


</body>