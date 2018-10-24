<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $mobile = $_POST['mobile'];
  $email = $_POST['email'];
  $casenumber = $_POST['case_number'];
  $table = $mobile."_"."cases";
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $statement = $link->prepare("UPDATE $table SET case_client_email = :email WHERE case_number = :casenum");
  $statement->execute(array(
    "email" => $email,
    "casenum" => $casenumber
  ));

  if ($statement && ($statement->rowCount()>0)) {
    $json = array(
      'status' => "success"
    );
    echo json_encode($json);
  }
  else {
    $json = array(
      'status' => "failed"
    );
    echo json_encode($json);
  }

}

else {
    echo "Error! Do Not Worry. We Are Working On It.";
}

?>
