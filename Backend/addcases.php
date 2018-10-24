<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  require_once('database.php');

  $mobile = $_POST['mobile'];
  $table = $mobile."_"."cases";
  $case_name = $_POST['case_name'];
  $case_nature = $_POST['case_nature'];
  $case_court = $_POST['case_court'];
  $case_type = $_POST['case_type'];
  $case_year = $_POST['case_year'];
  $case_counsel = $_POST['case_counsel'];
  $case_number = $_POST['case_number'];
  $case_filing_date = $_POST['case_filing_date'];
  $case_practice_area = $_POST['case_practice_area'];
  $case_client = $_POST['case_client'];
  $case_client_type = $_POST['case_client_type'];
  $case_description = $_POST['case_description'];
  $case_client_email = $_POST['case_client_email'];
  $case_client_mobile = $_POST['case_client_mobile'];
  $host = HOST;
  $user = USER;
  $db = DB;
  $pass = PASS;

  $link = new PDO("mysql:host=$host; dbname=$db", $user, $pass);
  $statement = $link->prepare("INSERT INTO $table(case_name, case_nature, case_court, case_type, case_year, case_counsel, case_number, case_filing_date, case_practice_area, case_client, case_client_type, case_description, case_client_email, case_client_mobile) VALUES(:c_name, :c_nature, :c_court, :c_type, :c_year, :c_counsel, :c_number, :c_filing_date, :c_practice_area, :c_client, :c_client_type, :c_description, :c_client_email, :c_client_mobile)");
  $statement->execute(array(
    "c_name" => $case_name,
    "c_nature" => $case_nature,
    "c_court" => $case_court,
    "c_type" => $case_type,
    "c_year" => $case_year,
    "c_counsel" => $case_counsel,
    "c_number" => $case_number,
    "c_filing_date" => $case_filing_date,
    "c_practice_area" => $case_practice_area,
    "c_client" => $case_client,
    "c_client_type" => $case_client_type,
    "c_description" => $case_description,
    "c_client_email" => $case_client_email,
    "c_client_mobile" => $case_client_mobile
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
