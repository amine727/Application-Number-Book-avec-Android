<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

require_once '../service/ContactService.php';

$service = new ContactService();
$contacts = $service->getAllContacts();
echo json_encode($contacts);