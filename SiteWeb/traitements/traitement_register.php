<?php
    session_start();
    require_once '../components/bd.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $email = htmlspecialchars($_POST['email']);

        $result = $pdo->prepare("SELECT email, password FROM Client WHERE email = :email");
        $result->execute(['email' => $email]);

        $client = $result->fetch();

        if ($client) {
            echo "Nom d'utilisateur ou email déjà pris.";
            header("Location: ../register.php?error=1");
        } else {

            $codePostal = htmlspecialchars($_POST['postal-code']);
            $ville = htmlspecialchars($_POST['city']);
            $adress = htmlspecialchars($_POST['address']);

            // Insertion dans la table Adresse
            $insertAdress = $pdo->prepare("INSERT INTO Adresse (codePostal, ville, adresse) VALUES (:codePostal, :ville, :adresse)");
            if ($insertAdress->execute([
                'codePostal' => $codePostal,
                'ville' => $ville,
                'adresse' => $adress
            ])) {
                // Si l'insertion dans la table Adresse réussit
                $lastInsertId = $pdo->lastInsertId();
                
                $pwd = password_hash(htmlspecialchars($_POST['password']), PASSWORD_DEFAULT);
                $nom = htmlspecialchars($_POST['last-name']);
                $prenom = htmlspecialchars($_POST['first-name']);
                $tel = htmlspecialchars($_POST['tel']);
                $genre = $_POST['sex'];
    
                $jour = $_POST['birth-day'];
                $mois = $_POST['birth-month'];
                $annee = $_POST['birth-year'];
    
                $date = sprintf('%04d-%02d-%02d', $annee, $mois, $jour);

                // Insertion dans la table Client
                $insert = $pdo->prepare("INSERT INTO Client (email, password, role, nom, prenom, numTel, genreC, dateNaissance, Adresse_idAdresse) VALUES (:email, :password, :role, :nom, :prenom, :numTel, :genreC, :dateNaissance, :Adresse_idAdresse)");
                if ($insert->execute([
                    'email' => $email,
                    'password' => $pwd,
                    'role' => 'client',
                    'nom' => $nom,
                    'prenom' => $prenom,
                    'numTel' => $tel,
                    'genreC' => $genre,
                    'dateNaissance' => $date,
                    'Adresse_idAdresse' => $lastInsertId
                ])) {
                    echo "Insertion réussie !";

                    $_SESSION['client_email'] = $email;
                    $_SESSION['client_prenom'] = $prenom;
                
                    header("Location: ../index.php");

                } else {
                    $errorInfo = $insert->errorInfo();
                    echo "Erreur lors de l'insertion : " . $errorInfo[2];
                    header("Location: ../register.php?error=10");
                }
            } else {
                $errorInfo = $insertAdress->errorInfo();
                echo "Erreur lors de l'insertion de l'adresse : " . $errorInfo[2];
                header("Location: ../register.php?error=20");
            }
        }
    }
?>
