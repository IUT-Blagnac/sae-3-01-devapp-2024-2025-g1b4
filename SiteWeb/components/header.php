<?php
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
