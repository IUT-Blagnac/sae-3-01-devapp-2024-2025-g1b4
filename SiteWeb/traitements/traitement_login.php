<?php
    session_start();
    require_once '../components/bd.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $email = htmlspecialchars($_POST['email']);
        $password = htmlspecialchars($_POST['password']);

        // Récupérer l'utilisateur avec son email depuis la base de données
        $result = $pdo->prepare("SELECT idClient, email, password, prenom FROM Client WHERE email = :email");
        $result->execute(['email' => $email]);

        $client = $result->fetch();

        if ($client) {
            
            if (password_verify($password, $client['password'])) {
                
                echo "Connexion réussie !";

                if (isset($_POST['remember-me'])) {
                    setcookie('client_email', $email, time() + 60 * 60 * 24 * 7);
                } else {
                    setcookie('client_email', $email, time() - 1);
                }

                $_SESSION["idClient"]=$client["idClient"];
                $_SESSION['client_email'] = $email;
                $_SESSION['client_prenom'] = $client['prenom'];

                header("Location: ../index.php");
            } else {
                
                echo "Mot de passe incorrect.";
                header("Location: ../login.php?error=30");
            }
        } else {
            
            echo "Aucun utilisateur trouvé avec cet email.";
            header("Location: ../login.php?error=40");
        }
    }
?>
