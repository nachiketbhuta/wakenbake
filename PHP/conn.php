<?php
require __DIR__.'/vendor/autoload.php';
use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;

$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/projectdata-48e3a7d57744.json');
$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();
   ?>