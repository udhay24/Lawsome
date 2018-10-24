<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $name = $_POST['name'];
  $mobile = $_POST['mobile'];
  $table = $mobile."_"."cases";
  $notes = $mobile."_"."notes";
  $tasks = $mobile."_"."tasks";
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);

  $sql = "CREATE TABLE IF NOT EXISTS $table (
                id int(11) AUTO_INCREMENT PRIMARY KEY,
                case_name varchar(500) NOT NULL,
                case_nature varchar(50) NOT NULL,
                case_court varchar(50) NOT NULL,
                case_type varchar(50) NOT NULL,
                case_year varchar(50) NOT NULL,
                case_counsel varchar(50) NOT NULL,
                case_number varchar(500) NOT NULL,
                case_filing_date varchar(50) NOT NULL,
                case_practice_area varchar(50) NOT NULL,
                case_client varchar(50) NOT NULL,
                case_client_type varchar(50) NOT NULL,
                case_description varchar(500) NOT NULL,
                case_client_email varchar(50) NOT NULL,
                case_client_mobile varchar(50) NOT NULL
              )";

$makenotes = "CREATE TABLE IF NOT EXISTS $notes (
                id int(11) AUTO_INCREMENT PRIMARY KEY,
                note_text varchar(500) NOT NULL,
                note_image varchar(50) NOT NULL,
                case_number varchar(50) NOT NULL,
                note_timestamp varchar(50) NOT NULL
              )";

$makenotes = "CREATE TABLE IF NOT EXISTS $notes (
                id int(11) AUTO_INCREMENT PRIMARY KEY,
                note_text varchar(500) NOT NULL,
                note_image varchar(50) NOT NULL,
                case_number varchar(50) NOT NULL,
                note_timestamp varchar(50) NOT NULL
              )";

$makenotes = "CREATE TABLE IF NOT EXISTS $notes (
                id int(11) AUTO_INCREMENT PRIMARY KEY,
                note_text varchar(500) NOT NULL,
                note_image varchar(50) NOT NULL,
                case_number varchar(50) NOT NULL,
                note_timestamp varchar(50) NOT NULL
              )";

  $link->exec($sql);
  $link->exec($makenotes);

  if ($link) {

      $statement = $link->prepare("INSERT INTO users(name, mobile) VALUES(:name, :mobile)");
      $statement->execute(array(
        "name" => $name,
        "mobile" => $mobile
      ));

      if ($statement) {
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
