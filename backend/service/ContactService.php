<?php
require_once '../config/Database.php';
require_once '../model/Contact.php';

class ContactService {
    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->getConnection();
    }

    public function insertContact($name, $phone, $source = "mobile") {
        $stmt = $this->conn->prepare(
            "INSERT INTO contact (name, phone, source) VALUES (:name, :phone, :source)"
        );
        $stmt->bindParam(':name', $name);
        $stmt->bindParam(':phone', $phone);
        $stmt->bindParam(':source', $source);

        if ($stmt->execute()) {
            return ["success" => true, "message" => "Contact inséré avec succès"];
        } else {
            return ["success" => false, "message" => "Erreur lors de l'insertion"];
        }
    }

    public function getAllContacts() {
        $stmt = $this->conn->prepare("SELECT * FROM contact ORDER BY name ASC");
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function searchContacts($keyword) {
        $stmt = $this->conn->prepare(
            "SELECT * FROM contact WHERE name LIKE :kw OR phone LIKE :kw ORDER BY name ASC"
        );
        $kw = "%" . $keyword . "%";
        $stmt->bindParam(':kw', $kw);
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
}