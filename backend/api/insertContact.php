<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

require_once '../service/ContactService.php';

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['name']) || !isset($data['phone'])) {
    echo json_encode(["success" => false, "message" => "Données manquantes"]);
    exit;
}

$service = new ContactService();
$result = $service->insertContact($data['name'], $data['phone'], $data['source'] ?? 'mobile');
echo json_encode($result);