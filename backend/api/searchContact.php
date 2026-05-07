<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

require_once '../service/ContactService.php';

$keyword = $_GET['keyword'] ?? '';

if (empty($keyword)) {
    echo json_encode([]);
    exit;
}

$service = new ContactService();
$contacts = $service->searchContacts($keyword);
echo json_encode($contacts);