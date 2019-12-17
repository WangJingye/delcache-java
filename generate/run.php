<?php
spl_autoload_register(function ($classname) {
    if (file_exists('vendor/' . str_replace('\\', '/', $classname) . '.php')) {
        require_once 'vendor/' . str_replace('\\', '/', $classname) . '.php';
    } else if (file_exists('vendor/' .str_replace('\\', '/', $classname) . '.php')) {
        require_once 'vendor/' . str_replace('\\', '/', $classname) . '.php';
    }
});
if (method() == 'POST') {
    $data = $_POST;
    if ($data['type'] == 'show-table') {
        try {
            $fields = Db::table($data['table'])->getFields();
            echo json_encode(['code' => 200, 'data' => array_keys($fields)]);
        } catch (Exception $e) {
            echo json_encode(['code' => 400, 'message' => $e->getMessage()]);
        }
        die;
    } else {
        if (isset($data['fcomment'])) {
            Generate::instance($data)->run();
        }
    }
}
$view = 'view.php';
include $view;


function method()
{
    return strtoupper($_SERVER['REQUEST_METHOD']);
}