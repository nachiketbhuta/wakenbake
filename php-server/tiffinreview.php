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
    if ($_SERVER["REQUEST_METHOD"] == "POST")
	{
		//getting review of the vendor by the particular username using post request
		$review = $_POST["review"];
	    $username = $_POST["usrname"];
		//$vendorid = $_POST("vendorid");
		$vendorname = $_POST["vendorname"];
		$postdata = array("");
		$postRef = $database->getReference("reviews")->push([
			'username' => $username,
			'review' => $review,
			'jointname' => $vendorname,
		]);
		//pushing the data into the database
	}
?>