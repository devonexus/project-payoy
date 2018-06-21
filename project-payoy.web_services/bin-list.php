<?php
	
	require 'bin.php';
	
	
	
	
	$binId = "";
    $userId = "";
	$status = "";
	if(isset($_POST['userId'])){
        
        $userId  = $_POST['userId'];   
    }
	if(isset($_POST['binId'])){
        
		$binId = $_POST['binId'];
        
    }
	if(isset($_POST['binStatus'])){
        
		$status = $_POST['binStatus'];
        
    }
	

	$binObject = new Bin();
	
	if(!empty($status) && !empty($userId)){
		$json_array = $binObject->retrieveBin($userId, $status);
		echo json_encode($json_array);
	} 
	else if(!empty($binId) && !empty($userId)){
		$json_array = $binObject->deleteBin($binId, $userId);
		echo json_encode($json_array);
	} 
	
	
?>