<?php
require __DIR__.'/vendor/autoload.php';
	use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;
    use Kreait\Firebase;

$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/ServiceAccount.json');
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
	$database = $firebase->getDatabase();
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	$usrlat ="";
	$usrlon ="";
	$endlat1 = "";
	$endlon1 = "";
	$endlat2 = "";
	$endlon2 = "";
	$json_array = array();
		$usrlat = $_GET["usrlat"];
		$usrlon = $_GET["usrlon"];
		//foreach ($vendors as $key => $value) 
		//{
		//	if($usrlat+0.009009009 || $usrlon+0.009009009 || $usrlat-0.009009009 || $usrlon-0.009009009)
		//}
		foreach($vendors as $key =>$value)
		{
			$endlat1 = $usrlat+0.009009009;
			$endlon1 = $usrlon+0.009009009;
			$endlat2 = $usrlat-0.009009009;
			$endlon2 = $usrlon-0.009009009; 
			
		if($value['latitude']<=$endlat1 && $value['longitude']<=$endlon1 && $value['latitude']>=$endlat2 && $value['longitude']>=$endlon2)
		{
			array_push($json_array,$value);
			//echo json_encode($value)."<br>";
		}
		//else
		//{
		//	echo json_encode("nonearby places")."<br>";
		//}
			//echo $usrlat;
			//echo $usrlon;
		}
		$myarr = array("data" => $json_array);
	header('Content-type: application/json');
	echo json_encode($myarr);

	%>