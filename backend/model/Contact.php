<?php
class Contact {
    public $id;
    public $name;
    public $phone;
    public $source;
    public $created_at;

    public function __construct($name, $phone, $source = "mobile") {
        $this->name = $name;
        $this->phone = $phone;
        $this->source = $source;
    }
}