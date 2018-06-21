<?php
    
    require 'user.php';
    
	
	
	//Store POST DATA to variables

    $username 	= "";
    $password 	= "";
    $lname 		= "";
	$fname 		= "";
	$minitial	= "";
	$email		= "";
    $image_name = "";
	
	
	
    if(isset($_POST['username'])){
        
        $username = $_POST['username'];
        
    }
    
    if(isset($_POST['password'])){
        
        $password = $_POST['password'];
        
    }
    
	if(isset($_POST['lname'])){
        
        $lname = $_POST['lname'];
        
    }
	
	if(isset($_POST['fname'])){
        
        $fname = $_POST['fname'];
        
    }
	if(isset($_POST['minitial'])){
        
        $minitial = $_POST['minitial'];
        
    }
	
	if(isset($_POST['email'])){
        
        $email = $_POST['email'];
        
    }
    if(isset($_POST["encoded_string"])){
		
			$encoded_string = $_POST["encoded_string"];
			$image_name = $_POST["image_name"];
			
			$decoded_string = base64_decode($encoded_string);
			
			$path = 'images/'.$image_name;
			$absolutepath = 'http://basurajuan.x10host.com/'.$path;
			$file = fopen($path, 'wb');
			
			$is_written = fwrite($file, $decoded_string);
			fclose($file);
		
	
	}
	
		$userObject = new User();
    
		 
		if(!empty($image_name) && !empty($username) && !empty($password) && !empty($email) && !empty($lname) && !empty($fname) && !empty($minitial))
                {
		
			$json_update = $userObject->updateExistingUser($lname, $fname, $minitial, $email, $username, $password, $absolutepath);
			
			echo json_encode($json_update);     
		} else{
			$json_update = $userObject->updateExistingUserExceptImage($lname, $fname, $minitial, $email, $username, $password);
			echo json_encode($json_update);
		} 
		
 ?>