<?php
require __DIR__.'/vendor/autoload.php';
	use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;
    use Kreait\Firebase;

$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/ServiceAccount.json');
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
    $count=0;
	$database = $firebase->getDatabase();
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	foreach($vendors as $key =>$value)
	{
		$myarr = array($count=>$value['name']);	
		//$sortarr['name'] = $value['name'];
		//echo $myarr[$key]."<br>";
		$count++;
		asort($myarr);
		//asort($myarr);
	print_r($myarr);
	//echo "<br/>";
	}
	asort($myarr);
	echo "fdkhgr" ;
	foreach ($myarr as $key => $val) {
    echo "$key = $val\n";
}
	//asort($myarr);
	 //print_r(array_values($myarr));

	//$max = 
	//foreach($sortarr['name'])
	//{

	//}
	