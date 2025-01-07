<?php
// Inclure la connexion à la base
require '../components/bd.php'; 
require_once '../vendor/autoload.php'; // Charger Faker require_once 


// Initialiser Faker
$faker = Faker\Factory::create('fr_FR');

// Appeler les scripts d'insertion
//require 'insert_categories.php';
//require 'insert_clients.php';
//require 'insert_products.php';
//require 'insert_point_relais.php';
//require 'insert_orders.php';
require 'insert_avis.php';

echo "Tous les scripts ont été exécutés avec succès.";
?>
