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
    $sortingarray;
	$database = $firebase->getDatabase();
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	//$database->getReference('vendors')
    
    $count = 0;
	$sortedArray = array();
	$key_arr = array();
	foreach($vendors as $key =>$value)
	{	
		$key_arr[$count] = $key;
		$count++;
	}
	for($i=0;$i<($count-1);$i++) {
			for($j=$i+1;$j<$count;$j++)
			{
				$vendor = $vendors[$key_arr[$i]];
				$vendor1 = $vendors[$key_arr[$j]];
				if($vendor['name'] >= $vendor1['name']) {
					//Swap
					$swap = $vendor;
					$vendor=$vendor1;
					$vendor1=$swap;
				}
				//echo json_encode($vendors);
				//echo"<br>
			}
	}
	?>