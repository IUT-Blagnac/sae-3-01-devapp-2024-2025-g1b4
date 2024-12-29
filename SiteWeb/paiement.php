<?php
    session_start();
    include './components/bd.php';
    if (!isset($_SESSION['client_email'])) {
        header("Location: login.php?msg=" . urlencode("Vous devez vous connecter d'abord!"));
    }
    include './components/header.php';
?>
<body>
    <div class=main-container>
        <div class="main-container-left">
            <div class="delivery-container">
                <form class="delivery-form">
                    <h1>Livraison</h1>
                    <div class="form-group">
                        <label for="country">Pays/région</label>
                        <select id="country" class="input-field">
                            <option value="france">France</option>
                            <option value="belgium">Belgique</option>
                            <option value="switzerland">Suisse</option>
                        </select>
                    </div>

                    <div class="form-group-row">
                        <div class="form-group">
                            <input type="text" id="first-name" class="input-field" placeholder="Prénom">
                        </div>
                        <div class="form-group">
                            <input type="text" id="last-name" class="input-field" placeholder="Nom">
                        </div>
                    </div>

                    <div class="form-group">
                        <input type="text" id="address" class="input-field" placeholder="Adresse">
                    </div>

                    <div class="form-group-row">
                        <div class="form-group">
                            <input type="text" id="postal-code" class="input-field" placeholder="Code postal">
                        </div>
                        <div class="form-group">
                            <input type="text" id="city" class="input-field" placeholder="Ville">
                        </div>
                    </div>

                    <div class="form-group">
                        <input type="tel" id="phone" class="input-field" placeholder="Téléphone">
                    </div>
                </form>
            </div>






            <div class="payment-container">
                <h1>Paiement</h1>
                <p class="secure-text">Toutes les transactions sont sécurisées et chiffrées.</p>

                <div class="payment-method">
                    <label class="radio-option">
                        <input type="radio" name="payment" value="credit-card" checked>
                        <span class="radio-label">Carte bancaire</span>
                    </label>

                    <div class="credit-card-form">
                        <input type="text" placeholder="Numéro de carte" class="input-field">
                        <div class="input-group">
                            <input type="text" placeholder="Date d'expiration (MM/AA)" class="input-field">
                            <input type="text" id="code" placeholder="Code de sécurité" class="input-field">
                        </div>
                        <input type="text" placeholder="Nom sur la carte" class="input-field">
                        <label class="checkbox-option">
                            <input type="checkbox" checked>
                            Utiliser l'adresse d'expédition comme adresse de facturation
                        </label>
                    </div>

                    <label class="radio-option">
                        <input type="radio" name="payment" value="paypal">
                        <span class="radio-label">PayPal</span>
                    </label>

                    <!-- Boutons d'action -->
                    <div class="payment-buttons">
                        <button id="credit-card-btn" class="payment-btn" style="display:none;">Payer maintenant</button>
                        <button id="paypal-btn" class="payment-btn" style="display:none;">Payer avec PayPal</button>
                    </div>
                </div>







                <script>
                    // Fonction pour gérer l'affichage des boutons de paiement et du formulaire de carte bancaire
                    function showPaymentButton() {
                        // Cache les deux boutons par défaut
                        document.getElementById('credit-card-btn').style.display = 'none';
                        document.getElementById('paypal-btn').style.display = 'none';

                        // Récupère la méthode de paiement sélectionnée
                        var paymentMethod = document.querySelector('input[name="payment"]:checked').value;

                        // Affiche le bouton approprié et gère le formulaire de carte bancaire
                        if (paymentMethod === 'credit-card') {
                            document.getElementById('credit-card-btn').style.display = 'inline-block';
                            document.querySelector('.credit-card-form').style.display = 'flex'; // Affiche le formulaire de carte bancaire
                        } else if (paymentMethod === 'paypal') {
                            document.getElementById('paypal-btn').style.display = 'inline-block';
                            document.querySelector('.credit-card-form').style.display = 'none'; // Cache le formulaire de carte bancaire
                        }
                    }

                    // Écoute l'événement "change" sur les boutons radio
                    var paymentRadios = document.querySelectorAll('input[name="payment"]');
                    paymentRadios.forEach(function(radio) {
                        radio.addEventListener('change', showPaymentButton);
                    });

                    // Appelle la fonction au chargement de la page pour afficher le bon bouton initial
                    window.onload = showPaymentButton;
                </script>




            </div>
        </div>

        <div class="main-container-right">
            <div class="order-summary-container">
                <ul class="order-summary">
                    <li>Sous-total <span>0€</span></li>
                    <li>Expédition <span>GRATUIT</span></li>
                    <li class="total">Total TVA incluse <span>0€</span></li>
                </ul>
            </div>
        </div>
    </div>

</body>

</html>