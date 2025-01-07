<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="./style/register/register.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./style/header/style.css">
</head>

<?php
include './components/header.php';
require_once './components/bd.php';
?>

<body>
    <div class="form-container">
        <h1>Créer un compte</h1>
        <form action="./traitements/traitement_register.php" method="POST">

            <div class="name-container">
                <input type="text" id="first-name" name="first-name" placeholder="Prénom" required>
                <input type="text" id="last-name" name="last-name" placeholder="Nom" required>
                <select id="sex" name="sex" required>
                    <option value="" disable selected>Genre</option>
                    <option value="H"> Homme </option>
                    <option value="F"> Femme </option>
                </select>
            </div>

            <label for="birthdate-container">Date de naissance</label>
            <div class="birthdate-container">
                <select id="birth-day" name="birth-day" required>
                    <option value="" disabled selected>Jour</option>

                    <script>
                        // Remplir les jours
                        const daySelect = document.getElementById('birth-day');
                        for (let day = 1; day <= 31; day++) {
                            const option = document.createElement('option');
                            option.value = day;
                            option.textContent = day;
                            daySelect.appendChild(option);
                        }
                    </script>
                </select>

                    <select id="birth-month" name="birth-month" required>
                        <option value="" disabled selected>Mois</option>
                        <option value="1">Janvier</option>
                        <option value="2">Février</option>
                        <option value="3">Mars</option>
                        <option value="4">Avril</option>
                        <option value="5">Mai</option>
                        <option value="6">Juin</option>
                        <option value="7">Juillet</option>
                        <option value="8">Août</option>
                        <option value="9">Septembre</option>
                        <option value="10">Octobre</option>
                        <option value="11">Novembre</option>
                        <option value="12">Décembre</option>
                    </select>

                    <select id="birth-year" name="birth-year" required>
                        <option value="" disabled selected>Année</option>

                        <script>
                            const yearSelect = document.getElementById('birth-year');
                            const currentYear = new Date().getFullYear();
                            for (let year = currentYear; year >= 1900; year--) {
                                const option = document.createElement('option');
                                option.value = year;
                                option.textContent = year;
                                yearSelect.appendChild(option);
                            }
                        </script>
                    </select>
            </div>

            <!-- Mail, Mot de passe et Confirmer mot de passe -->
            <div class="account-container">
                <input type="email" id="email" name="email" placeholder="nom@example.com" required>
                <input type="tel" id="tel" name="tel" placeholder="Numéro de télephone" pattern="[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}" required>
                <input type="password" id="password" name="password" placeholder="Mot de passe" required>
                <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirmer le mot de passe" required>
            </div>

            <!-- Adresse, Complément, Code postal, Ville, Pays -->
            <label for="address-container">Adresse</label>
            <div class="address-container">
                <input type="text" id="address" name="address" placeholder="Adresse" required>
                <input type="text" id="address-complement" name="address-complement" placeholder="Complément d'adresse" C>
                <input type="text" id="postal-code" name="postal-code" placeholder="Code postal" required>
                <input type="text" id="city" name="city" placeholder="Ville" required>
                <input type="text" id="country" name="country" placeholder="Pays" required>
            </div>

            <button type="submit">S'inscrire</button>
        </form>
    </div>
</body>

</html>