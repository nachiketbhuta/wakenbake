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
		//requesting input of food joints by post method
		$name = $_POST["name"];
		$number =$_POST["number"];
		$fooditem =$_POST["fooditem"];
		$rating = $_POST["rating"];
		$postdata = array("");

//pushing every data of the vendors with vendors as root element and then unique id of each vendor and inside that the data
		$postRef = $database->getReference("tiffin")->push([
			'name' => $name,
			'number' => $number,
			'fooditems' => $fooditem,
			'ratings' => $rating,
		]);
	}
	header('Content-type: application/json');
	//changes the header type to application/json
?>