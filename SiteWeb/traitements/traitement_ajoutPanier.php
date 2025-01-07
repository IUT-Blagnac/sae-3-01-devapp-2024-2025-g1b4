<?php

    session_start();

    if(!isset($_GET["idProd"]))
        header("Location: ./../index.php");

    if($_GET["isAvailable"]!="oui"){
        header("Location: ./../consultProd.php?idProduit=". $_GET["idProd"] . "&msg=". urlencode("Produit à court de stock dans cette taille et couleur!"));
        exit();
    }
    
    if(isset($_SESSION["idClient"])){

        require_once "components/bd.php";

        $req=$pdo->prepare("INSERT INTO Panier(Client_id, quantite, ProduitTailleCouleur_Produit_idProduit, ProduitTailleCouleur_Couleur_idCouleur, ProduitTailleCouleur_Taille_idTaille) 
                                VALUES (:idClient, :qte, :idProd, :idCouleur, :idTaille)");

        $req->execute(["idClient" => $_SESSION["idClient"],"qte"=> $_GET["qte"], "idProd" => $_GET["idProd"], "idCouleur"=> $_GET["couleur"], "idTaille" => $_GET["taille"]]);


    }else{

        $newRow=true;

        foreach($_SESSION["panierTemporaire"] as &$panier){
            if($panier["produit"]==$_GET["idProd"] && $panier["couleur"]==$_GET["couleur"] && $panier["taille"]==$_GET["taille"]){
                $panier["qte"]+=$_GET["qte"];
                $newRow=false;
            }
        }

        if($newRow)
            $_SESSION["panierTemporaire"][]=[
                "produit"=>$_GET["idProd"],
                "qte" => $_GET["qte"],
                "couleur"=> $_GET["couleur"],
                "taille" => $_GET["taille"]
            ];

    }
    

    header("Location: ./../panier.php");

?>
