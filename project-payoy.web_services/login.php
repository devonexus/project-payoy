<?php
    
    require 'user.php';
    
    $username = "";
    
    $password = "";
    
    if(isset($_POST['username'])){
        
        $username = $_POST['username'];
        
    }
    
    if(isset($_POST['password'])){
        
        $password = $_POST['password'];
        
    }
   
    
    
	
    $userObject = new User();
    

    
    if(!empty($username) && !empty($password)){
        
        //$hashed_password = md5($password);
        
        $json_array = $userObject->validateLogin($username, $password);
        
        echo json_encode($json_array);
    }
 ?>