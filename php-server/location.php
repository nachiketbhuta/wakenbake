<?php
require __DIR__.'/vendor/autoload.php';
	use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;
    use Kreait\Firebase;
//including all the preloaded functional files
$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/ServiceAccount.json');
//includes the firebase service account json file
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
	$database = $firebase->getDatabase();
	//gets the database of the current service account
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	//gets the reference of the current database at the particular child node
	$usrlat ="";
	$usrlon ="";
	$endlat1 = "";
	$endlon1 = "";
	$endlat2 = "";
	$endlon2 = "";
	$json_array = array();
		$usrlat = $_GET["usrlat"];
		$usrlon = $_GET["usrlon"];
		//getting user location
		foreach($vendors as $key =>$value)
		{
			//loops through each and every child vendor
			$endlat1 = $usrlat+0.009009009;
			$endlon1 = $usrlon+0.009009009;
			$endlat2 = $usrlat-0.009009009;
			$endlon2 = $usrlon-0.009009009;
			//adding up the equivalent degree of latitude and longitude of 1km radius 
			
		if($value['latitude']<=$endlat1 && $value['longitude']<=$endlon1 && $value['latitude']>=$endlat2 && $value['longitude']>=$endlon2)
		{
			//checking if the vendor is in 1km raidus of the user's location
			array_push($json_array,$value);
			//pushing the whole content child of vendor into the array
		}
		}
		$myarr = array("data" => $json_array);
		//adding data key in associative array and passing its value of array containing all the vendors list
		header('Content-type: application/json');
		//changes the header type to application/json
		echo json_encode($myarr);
		//printing the output in json format
	?>