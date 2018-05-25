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

if ($_SERVER["REQUEST_METHOD"] == "POST")
{
	$input=$_POST['vendor'];
	//Input validation for string
	$rootSnapshot = $database->getReference("/")->getSnapshot();
	if($rootSnapshot->hasChild($input) === true)
	{
	$vendorReference = $database->getReference($input);
	echo json_encode($vendorReference->getValue());
	}
	else
	{	
		$error = array("msg"=>"No Vendor Found");
		//$error->msg="No Vendors Found";
		$myJson=json_encode($error);
		echo $myJson; 
	}
}
//$database->getReference('vendors')
  // ->set([
     //  'name' => 'damodar',
     //  'number' => '7738877342',
      // 'fooditems' => 'South Indian and Beverages',
      //]);

if ($_SERVER["REQUEST_METHOD"] == "POST")
{
	//requesting input of food joints by post method
	$input1 = $_POST['name'];
	$input2 = $_POST['number'];
	$input3 = $_POST['fooditems'];
	$rating = $_POST['ratings'];

$postData = array("");
$postRef = $database->getReference('vendors')->push([
	'name' => $input1 ,
	'number' => $input2 ,
	'fooditems' => $input3 ,
	'ratings' => $rating,
]);
//$rating = database->getreference('/vendors/$postKey');
echo $rating;

$postKey = $postRef->getKey();
echo $postKey;
}
$count = 1;
if($_SERVER["REQUEST_METHOD"] == "POST")
{
	$vendor = $_POST['vendors'];
	$key = $database->getReference('/vendors/'.$postKey);
	echo $key;
}
$key1 = $database->getReference('/vendors/'.$postKey.'/name');
if ($_SERVER["REQUEST_METHOD"] == "POST")
{
	$ref = $database->getReference('/vendors');
	$ven = $ref->getValue();
	foreach ($ven as $value) {
		if($value===$vendor)
		{
			$usrrat = $_POST['userrating'];
			$count++;
			$frating = $rating+$usrrat;
			$frating = $frating/$count;
			echo $frating;
		}
	}
}
?>
