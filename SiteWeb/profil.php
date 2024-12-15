<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Profil</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./style/profil/style.css">
</head>
    <?php
        include './components/header.php'
    ?>
<body>
    <?php
        session_start();

        if (isset($_POST['logout'])) {
            session_unset();
            session_destroy();
            header("Location: accueil.php");
            exit();
        }
    ?>

    <div class="profil">
        <div class="left_menu">
            <form class="div_form" method="POST">
                <button type="submit" name="logout" class="lm_deconnexion">Déconnexion</button>
            </form>
        </div>

        <div class="global_page"></div>
    </div>
</body>
</html>
