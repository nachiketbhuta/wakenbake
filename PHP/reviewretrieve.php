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
	$ref = $database->getReference('/reviews');
	$reviews = $ref->getValue();
	//gets the reference of the current database at the particular child node
$vendorname;
$json_array= array();
$username;
if ($_SERVER["REQUEST_METHOD"] == "POST")
{
	//taking the vendorname's name as post request
	$vendorname = $_POST["vendorname"];

}
$reviews['username']['review']=  array();
//creating a new associative array of reviews
$arrayName = array();
//storing array
foreach($reviews as $key => $value)
{
	//loops through each and every child vendor
	if(isset($value['jointname']))
	{
		//checks whether there the joint name has a value in it
	if(strcmp($vendorname,$value['jointname'])==0)
	{
		//comparing the review database vendor name and the post request vendor name
		$username = $value['username'];
		//storing the value of the username key of database into a variable
		$review = $value['review'];
		//storing the value of the review key of database into a variable
		$newReview = array('username' => $username,'review' => $review);
		//storing value of username and review into the array
		array_push($arrayName,$newReview);
		//pushing the contents into another array
	}
}
}
		$json_array = array("data" =>$arrayName);
		//adding data key in associative array and passing its value of array containing all the vendors list
		echo json_encode($json_array);
		//printing the output in json format
		header('Content-type: application/json');
		//changes the header type to application/json
	
?>