<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $mobile = $_POST['mobile'];
  $note_text = $_POST['note_text'];
  $note_image = $_POST['note_image'];
  $case_number = $_POST['case_number'];
  $note_timestamp = $_POST['note_timestamp'];
  $table = $mobile."_"."notes";
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $statement = $link->prepare("INSERT INTO $table(note_text, note_image, case_number, note_timestamp) VALUES(:n_text, :n_image, :c_number, :n_timestamp)");
  $statement->execute(array(
    "n_text" => $note_text,
    "n_image" => $note_image,
    "c_number" => $case_number,
    "n_timestamp" => $note_timestamp
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
