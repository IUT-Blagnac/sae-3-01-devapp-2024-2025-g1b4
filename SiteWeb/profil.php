<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Profil</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./style/profil/style.css">
</head>
    <?php
        include './components/header.php'
    ?>
<body>
    <?php        
        if (isset($_POST['logout'])) {
            session_unset();
            session_destroy();
            header("Location: index.php");
            exit();
        }

        if (!isset($_SESSION['client_permission'])) {
            $roleReq = $pdo->prepare("SELECT role FROM Client WHERE email=:mail");
            $roleReq->execute(["mail" => $_SESSION["client_email"]]);

            $role = $roleReq->fetch();
            echo $role['role'];
            $_SESSION['client_permission'] = $role['role'];
        }
    ?>

    <div class="profil">
        <div class="left_menu">
            <?php
                if ($_SESSION['client_permission'] == 'admin') {
                    echo '<button class="dashboard-btn" type="button" onclick="window.location.href=\'admin.php\'">Accéder au dashboard</button>';

                    // Gérer les rôles si l'utilisateur est owner
                    if ($_SESSION['client_permission'] == 'owner') {
                        echo '<button class="" type="button" onclick="window.location.href=\'?action=manage-clients\'">Créer un compte administrateur</button>';
                    }
                }

                if (isset($_GET['action']) && $_GET['action'] === 'manage-clients') {
                    require_once './components/bd.php';
                    $stmt = $pdo->query("SELECT idClient, email, role FROM Client");
                    echo "<h2>Gérer les rôles</h2>";
                    echo "<table>";
                    echo "<tr><th>Email</th><th>Rôle</th><th>Action</th></tr>";
                
                    while($row = $stmt->fetch()) {
                        echo "<tr>";
                        echo "<td>".htmlspecialchars($row['email'])."</td>";
                        echo "<td>".htmlspecialchars($row['role'])."</td>";
                        echo "<td>
                                <form method='POST' action=''>
                                    <input type='hidden' name='userId' value='".htmlspecialchars($row['idClient'])."'>
                                    <input type='hidden' name='currentRole' value='".htmlspecialchars($row['role'])."'>
                                    <input type='submit' name='toggleRole' value='Changer le rôle'>
                                </form>
                              </td>";
                        echo "</tr>";
                    }
                    echo "</table>";
                }
                
                // Traitement du changement de rôle
                if (isset($_POST['toggleRole'])) {
                    require_once './components/bd.php';
                    $currentRole = $_POST['currentRole'];
                    $newRole = ($currentRole === 'admin') ? 'client' : 'admin';
                    $update = $pdo->prepare("UPDATE Client SET role = :newRole WHERE idClient = :id");
                    $update->execute([
                        'newRole' => $newRole,
                        'id' => $_POST['userId']
                    ]);
                    header("Location: profil.php?action=manage-clients");
                    exit();
                }
            ?>
            <form class="div_form" method="POST">
                <button type="submit" name="logout" class="lm_deconnexion">Déconnexion</button>
            </form>
        </div>

        <div class="global_page"></div>
    </div>
</body>
</html>
