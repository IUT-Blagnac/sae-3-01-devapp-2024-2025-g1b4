<!doctype html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <title>Votre Panier</title>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="./style/header/style.css">
  <link rel="stylesheet" href="./style/panier/style.css">
  <script src="script.js"></script>
</head>
<body>
    <?php include './components/header.php'; ?>

    <div class="panier_center">

      <div class="main_container">
        <div class="panier_wrapper">
          <h1>Panier</h1>
          <div class="panier_empty">
              <p>Votre panier est vide</p>
              <button class="btn_achat" onclick="window.location.href='index.php'">Continuer mes achats</button>
          </div>
        </div>

        <aside class="recapitulatif_wrapper">
          <h2>Récapitulatif</h2>
          <ul>
            <li>Sous-total <span>0€</span></li>
            <li>Livraison <span>voir à l'étape suivante</span></li>
            <li class="total">Total TVA incluse <span>0€</span></li>
          </ul>
          <button class="btn_commande" onclick="window.location.href='paiement.php'">Poursuivre la commande</button>
        </aside>
      </div>
    </div>
</body>
</html>
