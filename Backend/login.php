<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $mobile = $_POST['mobile'];
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $stmt = $link->prepare("SELECT * FROM users WHERE mobile = :mobile");
  $stmt->execute(array("mobile" => $mobile));
  $result = $stmt->fetch(PDO::FETCH_ASSOC);

  if ($result) {
    $query = $link->query("SELECT name FROM users WHERE mobile = '$mobile'");
    $name = $query->fetchColumn();
    $json = array(
     'status' => "successful",
     'name' => $name
    );
    echo json_encode($json);
    return;
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
