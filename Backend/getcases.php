<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $mobile = $_POST['mobile'];
  $table = $mobile."_"."cases";
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $stmt = $link->prepare("SELECT * FROM $table");
  $stmt->execute();
  $result = $stmt->fetchAll();
  echo json_encode($result);

}

else {
  echo "Error! Do Not Worry. We Are Working On It.";
}

?>
