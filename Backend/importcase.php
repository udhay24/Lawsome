<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  header('Content-Type: application/json');
  $court_type = $_POST['court_type'];
  $court_id = $_POST['court_id'];
  $case_type = $_POST['case_type'];
  $case_type = urlencode($case_type);
  $case_number = $_POST['case_number'];
  $case_year = $_POST['case_year'];
  $curl = curl_init();

  curl_setopt_array($curl, array(
    CURLOPT_URL => "https://libraapi.vakilsearch.com/api/v1/court_cases/case_results?court_type=$court_type&court_id=$court_id&case_type_id=$case_type&case_number=$case_number&case_year=$case_year",
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_ENCODING => "",
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "GET",
    CURLOPT_HTTPHEADER => array(
      "apitoken: SwyRUGyipNqzoykYdlLeAkeSCWNbusuJtcFVRDGGbnKogrozzL",
      "authorization: Token token=3z4nWriUVMew2-o8c4S4",
      "cache-control: no-cache",
      "content-type: application/json",
      "firmuserid: 36400",
      "userid: 83452"
    ),
  ));

  $response = curl_exec($curl);
  $err = curl_error($curl);
  curl_close($curl);

  if ($err) {
    echo "cURL Error #::" . $err;
  }
  else {
    echo $response;
  }

}

else {
  echo "Error! Do Not Worry. We Are Working On It.";
}


?>
