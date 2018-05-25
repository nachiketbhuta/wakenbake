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
	$vendorname = "";
	$usrrat = "";
	$finalrat = "";
	$rat = "";
	if($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$vendorname = $_POST["vendor"];
		//echo $vendorname;
		$usrrat = $_POST["ratings"];
		//echo $usrrat;
		foreach ($vendors as $key => $value) 
	{

		if($vendorname === $value['name'])
		 {
		 	//echo $vendorname;
		 	//echo $value['name']."<br>";
		 	$rat = $value['ratings'];
		 	$finalrat = $rat + $usrrat;
		 	$finalrat = $finalrat/2;
		 	//echo $finalrat;
		 	//$key = $database->getReference('/vendors');//->getKey();
		 	//echo $key;


		 	$database->getReference('/vendors/'.$key.'/ratings')->set($finalrat);
		 }
		

	}

	
		 
		//$rat = $value['ratings'];
		//echo $rat;
		//echo json_encode($value);
		//echo "<br>";
	}

?>
