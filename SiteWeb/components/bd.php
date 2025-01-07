<?php
    $host = 'localhost';
    $dbname = 'R2024MYSAE3011';
    $username = 'R2024MYSAE3011';
    $password = 'nujXd8tM83G45J';
    $port = '3306';

    try {

        $pdo = new PDO("mysql:host=$host;dbname=$dbname;port=$port", $username, $password);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
   
    } catch (PDOException $e) {
   
        die("Erreur de connexion : " . $e->getMessage());
   
    }
?>
