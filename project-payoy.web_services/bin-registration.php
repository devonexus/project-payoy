<?php
	
	require 'bin.php';
	 

	$binName = "";
	$binId = "";
	$userId = "";
	if(isset($_POST['binId'])){
		 $binId = $_POST['binId'];
	}
	
	if(isset($_POST['binName'])){
        
        $binName = $_POST['binName'];
        
    }
	if(isset($_POST['userId'])){
        
        $userId  = $_POST['userId'];   
    }
	
	
	$binObject = new Bin();
	 
	if (!empty($binId) && !empty($binName) && !empty($userId)){
		$json_array = $binObject->updateBin($binId, $binName, $userId);
        echo json_encode($json_array);
	} 
	else if(!empty($userId) && !empty($binName) ){
        $json_array = $binObject->registerBin($binName, $userId);
        echo json_encode($json_array);
    }
	else{
		$json_array = $binObject->retrieveBinName($userId);
		echo json_encode($json_array);
	}
	
	
	 
	
	
?>