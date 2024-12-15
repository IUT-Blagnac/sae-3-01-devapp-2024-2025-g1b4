<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="./style/login/login.css">
		<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
  		<link rel="stylesheet" href="./style/header/style.css">
	</head>
	<?php include './components/header.php'?>
	
	<body>
		<div class="login-container">
			<h1>Connexion</h1>
			<form action="./traitements/traitement_login.php" method="POST" class="login-form">
				<div class="form-group">
					<?php
						if (isset($_COOKIE['client_email'])) {
							echo '<input type="email" id="email" name="email" placeholder="E-mail ou numéro de téléphone" value="' . $_COOKIE['client_email'] . '" required>';
						} else {
							echo '<input type="email" id="email" name="email" placeholder="E-mail ou numéro de téléphone" required>';
						}
					?>
				</div>
				<div class="form-group">
					<input type="password" id="password" name="password" placeholder="Mot de passe" required>
				</div>
				<div class="form-group remember-me">
                <input type="checkbox" id="remember-me" name="remember-me" value>
                <label for="remember-me">Se souvenir de moi</label>
            	</div>
           		<button type="submit" class="login-btn">Se connecter</button>
				<p class="register-link">
					Pas encore de compte ? <a href="./register.php">S'inscrire</a>
				</p>
			</form>
		</div>
		<button class="dashboard-btn" type="button" onclick="window.location.href='admin.php'">Accéder au dashboard</button>
	</body>
</html>



