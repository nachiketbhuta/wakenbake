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
   	$i=0;
   	$query="";
	$database = $firebase->getDatabase();
	//gets the database of the current service account
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
		//gets the reference of the current database at the particular child node
	$query = $_GET["search"];
	//getting the search query by get request
	$count = strlen($query);
	//counts the length of the query string
	$substring = "";
	$name = "";
	$item = "";
	$substring1 = "";
	$itemcount = 0;
	$namecount = 0;
	$max = 0;
	$flag = 0;
	$json_array = array();
	foreach($vendors as $key =>$value)
	{
		//loops through each and every child vendor
		$flag = 0;
		$item = $value['fooditems'];
		//stores the string value of each vendor's fooditems into a variable
		$itemcount = strlen($item);
		//stores the length of the string of food items of each vendor
		$name = $value['name'];
		//stores the string value of each vendor's name into a variable
		$namecount = strlen($name);
		//stores the length of the string of name of each vendor
		if($itemcount>$namecount)
		{
			$max = $itemcount;
		}
		else
		{
			$max = $namecount;
		}
		//this if-else block is used to count the maximum length of two string name and string fooditems to increase the optimization
		for($i=0;$i<$max;$i++)
		{
			//loops throught each and every character of the string
			$substring = substr($value['name'],$i,$count);
			$substring1 = substr($value['fooditems'],$i,$count);
			//takes the substring till ith character
			if(strcmp($substring,$query)==0 || strcmp($substring1,$query)==0)
			{
				//compares the substring and the query
				if($flag == 0) {
					array_push($json_array,$value);
				//pushing the whole content child of vendor into the array
					$flag = 1;
					//using flag variable not to repeat the output vendors list 
				}
			}
			else
			{
			}
		}	
		
	}
	$myarr = array("data" => $json_array);
	//adding data key in associative array and passing its value of array containing all the vendors list
	header('Content-type: application/json');
	//changes the header type to application/json
	echo json_encode($myarr);
	//printing the output in json format
?>

