<?php
require __DIR__.'/vendor/autoload.php';
	use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;
    use Kreait\Firebase;

$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/serviceAccount.json');
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
	$database = $firebase->getDatabase();
	$ref = $database->getReference('/vendors');
	$vendors = $ref->getValue();
	foreach ($vendors as $value) {
		
	}
?>
