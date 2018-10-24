<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    header('Content-Type: application/json');
    $district = $_POST['district'];
    $curl = curl_init();

    curl_setopt_array($curl, array(
      CURLOPT_URL => "https://libraapi.vakilsearch.com/api/v1/court_cases/get_district_courts?district_id=$district&state_id=6",
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
      echo "cURL Error #:" . $err;
    } else {
      echo $response;
    }

}

else {
  echo "Error! Do Not Worry. We Are Working On It.";
}
