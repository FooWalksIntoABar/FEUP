
<?php
if (!isset($_POST['username'])) die('username not set');
if (!isset($_POST['password'])) die('password not set');
include_once(__DIR__ . '/database/connection.php'); // connects to the database
include_once(__DIR__ . '/database/user.php'); // loads the functions responsible for the users table

include_once(__DIR__ . '/includes/init.php');

//if (!userExists($dbh,$_POST['username'])) // test if user exists
if (!userExists($_POST['username']))   // test if user exists
{
    global $dbh;
    try{
        register($dbh, $_POST['username'], $_POST['password'], $_POST['name'], $_POST['email'], $_POST['gender']);
    }catch (PDOException $e) {
        die($e->getMessage());
    }

}else{
    echo('Checking if user exists <br>');
}


//header('Location: ' . $_SERVER['HTTP_REFERER']);
header('location:index.php');

?>