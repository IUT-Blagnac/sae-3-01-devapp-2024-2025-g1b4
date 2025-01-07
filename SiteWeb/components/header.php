<?php
<<<<<<< Updated upstream
    session_start();

    $path = "./login.php"; 

    if(isset($_GET["msg"])){
        echo '<script>alert("'.$_GET["msg"].'")</script>';
    }

    if (isset($_SESSION['client_email'])) {
        $path = "./profil.php";
    }
?>
<!DOCTYPE html>
<html>
	<head>
		<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
  		<link rel="stylesheet" href="./style/header/style.css">
        <link rel="stylesheet" href="./style/<?= basename($_SERVER["PHP_SELF"],".php")?>/style.css">
	</head>

<header>
    
    <div class="header_title">  
        <p>La Roue Tourne</p>
    </div>
    <div class="header_navigation">
        <a href="./index.php">
            <span>Accueil</span>
        </a>
        <a href="./nosBoutiques.php">
            <span>Nos boutiques</span>
        </a>
        <a href="">
            <span>Acheter</span>
        </a>
    </div>
    <div class="header_interaction">
        <a href="">
            <img src="./assets/accueil/Search.svg" alt="Profil" class="img_accueil">
        </a>
        <a href="./panier.php" aria-label="Voir le panier">
            <img src="./assets/accueil/Panier.png" alt="Panier" class="img_accueil">
        </a>
        <a href="<?php echo $path; ?>">
            <img src="./assets/accueil/Profil.png" alt="Profil" class="img_accueil">
        </a>
    </div>
</header>
=======
if (session_status() !== PHP_SESSION_ACTIVE) session_start();

$path = "./login.php";

if (isset($_GET["msg"])) {
?>
    <script>
        alert("<?= $_GET["msg"] ?>")
    </script>
<?php
}

if (isset($_SESSION['client_email'])) {
    $path = "./profil.php";
}

require_once './components/bd.php';

$req = $pdo->prepare("SELECT * FROM Categorie WHERE idCategorieMere IS NULL");
$req->execute();

$meres = $req->fetchAll();

$req = $pdo->prepare("SELECT * FROM Categorie WHERE idCategorieMere IN (SELECT idCategorie FROM Categorie WHERE idCategorieMere IS NULL)");
$req->execute();

$filles = $req->fetchAll();

$pageName=basename($_SERVER["PHP_SELF"], ".php");
?>
<!DOCTYPE html>
<html>

<head>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./style/header/style.css">
    <link rel="stylesheet" href="./style/<?= $pageName ?>/style.css">
</head>

<header>

    <div class="head">
        <div class="header_title">
            <p>La Roue Tourne</p>
        </div>
        <div class="header_navigation">
            <a href="./index.php">
                <span <?= $pageName == "index" ? 'class="currentPage"':"" ?>>Accueil</span>
            </a>
            <a href="./nosBoutiques.php">
                <span <?= $pageName == "nosBoutiques" ?  'class="currentPage"' : "" ?>>Nos boutiques</span>
            </a>
            <div class="acheter" onmouseenter='document.getElementById("optionsAchat").hidden=false;' onmouseleave='document.getElementById("optionsAchat").hidden=true;'>
                <a href="./articles.php">
                    <span <?= $pageName == "articles" ?  'class="currentPage"' : "" ?>>Acheter</span>
                </a>
                <div id="optionsAchat" hidden>
                    <div class="meres" id="meres">
                        <?php
                        foreach ($meres as $mere) {
                        ?>
                            <a class="mere" id="mere<?= trim($mere["idCategorie"])?>" href="./articles.php?categories=<?= $mere["idCategorie"] ?>" onmouseover='fillWith(" <?= $mere["idCategorie"] ?> ")'><?= $mere["name"] ?></a>
                        <?php
                        }
                        ?>
                    </div>
                    <div id="filles"></div>
                </div>
            </div>

        </div>
        <div class="header_interaction">
            <form action="articles.php" method="get">
                <input id="searchField" type="text" name="search" placeholder="Rechercher" />
                <div id="tofill">
                    <a href="javascript:openSearchBar()">
                        <img src="./assets/accueil/Search.svg" alt="Profil" class="img_accueil">
                    </a>
                </div>
            </form>
            <a href="./panier.php" aria-label="Voir le panier">
                <img src="./assets/accueil/Panier.png" alt="Panier" class="img_accueil">
            </a>
            <a href="<?= $path ?>">
                <img src="./assets/accueil/Profil.png" alt="Profil" class="img_accueil">
            </a>
        </div>
    </div>




    <script>
        var filles = <?= json_encode($filles) ?>;
        var prevMere="";
        function fillWith(idMere) {
            var htmlCode = '';

            filles.forEach(element => {
                if (idMere == element.idCategorieMere)
                    htmlCode += '<a href="./articles.php?categories=' + element.idCategorie + '"><p class="fille">' + element.name + "</p></a>";
            });

            var mere=document.getElementById(prevMere);
            if(mere!=null){
                mere.style.textDecoration="";
            }


            prevMere="mere" +idMere.trim();

            var mere=document.getElementById(prevMere);
            mere.style.textDecoration="underline";
            mere.style.textDecorationColor="darkblue";

            
            document.getElementById('filles').innerHTML = htmlCode;
        }

        function openSearchBar() {
            document.getElementById("tofill").innerHTML = '<input type="image" name="submit" src="./assets/accueil/Search.svg" class="img_accueil" alt="submit"/>';
            document.getElementById("searchField").style.opacity = 1;
        }
    </script>
</header>
>>>>>>> Stashed changes
