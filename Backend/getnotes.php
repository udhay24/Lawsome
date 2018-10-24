<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  header("Content-Type: application/json");
  require_once('database.php');

  $mobile = $_POST['mobile'];
  $casenumber = $_POST['case_number'];
  $table = $mobile."_"."notes";
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $stmt = $link->prepare("SELECT * FROM $table WHERE case_number = :cm");
  $stmt->execute(array(':cm' => $casenumber));
  $result = $stmt->fetchAll();
  echo json_encode($result);

}

else {
  echo "Error! Do Not Worry. We Are Working On It.";
}

?>
