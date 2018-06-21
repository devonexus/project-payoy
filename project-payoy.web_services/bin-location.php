<?php
	
	require 'bin.php';
	
	
	
	$function = "";
	
	 if(isset($_POST['function'])){
        
             $function = $_POST['function'];
        
         }
	
	$binObject = new Bin();
	if($function == 'retrieve'){
           $json_array = $binObject->getCoordinates();
	   echo json_encode($json_array);
        }
	

	
?>