<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carte Interactive</title>
    <link rel="stylesheet" href="style/<?= basename(__FILE__, '.php'); ?>/NosBoutiquesCss.css">
</head>
<body>

    <?php
        include 'components/header.php';
        $imgPath="assets/".basename(__FILE__, '.php')."/";
    ?>


    <div class="carteInteractive">
        <div class="popUp" id="infoPopUp">
            <img src="<?= $imgPath ?>infoIcon.png" alt="infoIcon">
            <label>Infos!</label>
            <p><b>La roue tourne s'implante bientôt à Bordeaux!</b></p>
        </div>
        <div class="pointeur" id="paris">
            <img src="<?= $imgPath ?>pointeur.png" alt="Pointeur">
            <label><br>Paris</label>
            <div class="shopDescription"></div>
        </div>
        <div class="pointeur" id="lyon">
            <img src="<?= $imgPath ?>pointeur.png" alt="Pointeur">
            <label><br>Lyon</label>
            <div class="shopDescription"></div>
        </div>
        <div class="pointeur" id="angers">
            <img src="<?= $imgPath ?>pointeur.png" alt="Pointeur">
            <label><br>Angers</label>
            <div class="shopDescription"></div>
        </div>
        <div class="pointeur" id="toulouse">
            <img src="<?= $imgPath ?>pointeur.png" alt="Pointeur">
            <label><br>Toulouse</label>
            <div class="shopDescription"></div>
        </div>
        <div class="pointeur" id="agen">
            <img src="<?= $imgPath ?>pointeur.png" alt="Pointeur">
            <label><br>Agen</label>
            <div class="shopDescription"></div>
        </div>
        <div>
            <img id="cycliste" src="<?= $imgPath ?>cycliste.png" alt="cycliste">
        </div>
       

    </div>


</body>
</html>