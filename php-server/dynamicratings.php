<?php
require __DIR__.'/vendor/autoload.php';
	use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;
    use Kreait\Firebase;
//including all the preloaded functional files
$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/serviceAccount.json');
//includes the firebase service account json file
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
	$database = $firebase->getDatabase();
	//gets the database of the current service account
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	//gets the reference of the current database at the particular child node
	$vendorname = "";
	$usrrat = "";
	$finalrat = "";
	if($_SERVER["REQUEST_METHOD"] == "POST")
	{
		//requesting user's ratings by post method
		$vendorname = $_POST['vendors'];
		$usrrat = $_POST['ratings'];
		foreach ($vendors as $key => $value) 
	{
		//loops through each and every child vendor
		if($vendorname === $value['name'])
		 {
		 	//checks if the vendorname in the database is equal to entered user id
		 	$rat = $value['ratings'];
		 	$finalrat = $rat + $usrrat;
		 	$finalrat = $finalrat/2;
		 	//takes the aggregate of user entered rating and pre loaded rating in the database
		 	$database->getReference('/vendors/'.$key.'/ratings')->set($finalrat);
		 	//updates the aggregate rating in the database
		 	//echo json_encode($finalrat);
		 	$json_array = array("ratings" => $finalrat);
		 	echo json_encode($json_array);
		 }
		

	}
	}
header('Content-type: application/json');
//changes the header type to application/json
?>
