<?php
    session_start();
    require_once '../components/bd.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $email = htmlspecialchars($_POST['email']);

        $result = $pdo->prepare("SELECT email FROM Client WHERE email = :email");
        $result->execute(['email' => $email]);

        $client = $result->fetch();

        if ($client) {
            echo "Nom d'utilisateur ou email déjà pris.";
            header("Location: ../register.php?error=1");
        } else {
            // Récupérer les informations du formulaire
            $codePostal = htmlspecialchars($_POST['postal-code']);
            $ville = htmlspecialchars($_POST['city']);
            $adress = htmlspecialchars($_POST['address']);
            $pwd = password_hash(htmlspecialchars($_POST['password']), PASSWORD_DEFAULT);
            $nom = htmlspecialchars($_POST['last-name']);
            $prenom = htmlspecialchars($_POST['first-name']);
            $tel = htmlspecialchars($_POST['tel']);
            $genre = $_POST['sex'];
            $jour = $_POST['birth-day'];
            $mois = $_POST['birth-month'];
            $annee = $_POST['birth-year'];

            // Appel de la procédure stockée insertClient
            $insert = $pdo->prepare("CALL insertClient(:codePostal, :ville, :adresse, :email, :password, :last_name, :first_name, :tel, :sex, :birth_day, :birth_month, :birth_year)");

            // Passer les paramètres directement dans execute
            if ($insert->execute([
                'codePostal' => $codePostal,
                'ville' => $ville,
                'adresse' => $adress,
                'email' => $email,
                'password' => $pwd,
                'last_name' => $nom,
                'first_name' => $prenom,
                'tel' => $tel,
                'sex' => $genre,
                'birth_day' => $jour,
                'birth_month' => $mois,
                'birth_year' => $annee
            ])) {
                echo "Insertion réussie !";

                // Récupérer le prénom de l'utilisateur pour la session
                $_SESSION['client_email'] = $email;
                $_SESSION['client_prenom'] = $prenom;
                
                header("Location: ../index.php");
            } else {
                $errorInfo = $insert->errorInfo();
                echo "Erreur lors de l'insertion : " . $errorInfo[2];
                header("Location: ../register.php?error=10");
            }
        }
    }
?>
