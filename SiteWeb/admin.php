<?php 

    require_once './components/bd.php';

    session_start();

    $roleReq = $pdo->prepare("SELECT role FROM Client WHERE email=:mail");

    $roleReq->execute(["mail" => $_SESSION["client_email"]]);
    
    $role = $roleReq->fetch();

    if($role["role"]=='client')
        header("Location: index.php?msg=".urlencode("Vous devez être administrateur!"));


    include './components/header.php';
    ?>

<body>
    <div class="square-container1">
        <div class="squareAdd">
            <a href="dashboard.php"><img src="./assets/admin/plus.png" alt="Image" class="squareAdd-img"></a>
        </div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>

    </div>
    <div class="square-container2">
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>
        <div class="square"></div>

    </div>

</body>

</html>