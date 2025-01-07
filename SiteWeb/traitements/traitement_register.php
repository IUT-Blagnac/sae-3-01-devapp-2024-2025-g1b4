<?php 
    require_once './components/bd.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $email = htmlspecialchars($_POST['email']);


        $result = $pdo->prepare("SELECT email, password FROM Client WHERE email = :email");
        $result->execute(['email' => $email]);

        $client = $result->fetch();

        if ($client) {
            echo "Nom d'utilisateur ou email déjà pris.";
        } else {

            $pwd = htmlspecialchars($_POST['password']);
            $nom = htmlspecialchars($_POST['last-name']);
            $prenom = htmlspecialchars($_POST['first-name']);
            $tel = htmlspecialchars($_POST['tel']);
            $genre = $_POST['sex'];

            $jour = $_POST['birth-day'];
            $mois = $_POST['birth-month'];
            $annee = $_POST['birth-year'];

            $date = sprintf('%02d-%02d-%04', $jour, $mois, $annee);

            $insert = $pdo->prepare("INSERT INTO Client (email, password, role, nom, prenom, numTel, genreC, dateNaissance) VALUES (:email, :password, :role, :nom, :prenom, :numTel, :genreC, :dateNaissance)");
            $insert->execute([
                'email' => $email,
                'password' => $pwd,
                'role' => 'client',
                'nom' => $nom,
                'prenom' => $prenom,
                'numTel' => $tel,
                'genreC' => $genre,
                'dateNaissance' => $date
            ]);

            if ($executed) {
                $lastInsertId = $pdo->lastInsertId();
                echo "Insertion réussie !";
            } else {
                $errorInfo = $insert->errorInfo();
                echo "Erreur lors de l'insertion : " . $errorInfo[2];
            }

        }
    }
?>