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
			<form action="submit_login.php" method="POST" class="login-form">
				<div class="form-group">
					<input type="email" id="email" name="email" placeholder="E-mail ou numéro de téléphone" required>
				</div>
				<div class="form-group">
					<input type="password" id="password" name="password" placeholder="Mot de passe" required>
				</div>
				<div class="form-group remember-me">
                <input type="checkbox" id="remember-me" name="remember-me">
                <label for="remember-me">Se souvenir de moi</label>
            	</div>
           		<button type="submit" class="login-btn">Se connecter</button>
				<p class="register-link">
					Pas encore de compte ? <a href="./register.php">S'inscrire</a>
				</p>
			</form>
		</div>
	</body>
</html>



