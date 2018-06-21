<?php

	require 'deployment.php';
	
	$binName = "";
	$userId = "";
	$binId = "";
	$location = ""; 
	if(isset($_POST['userId'])){	
		$userId = $_POST['userId'];
	}
		
	if(isset($_POST['binId'])){	
		$binId = $_POST['binId'];
	}
	
	if(isset($_POST['location'])){	
		$location = $_POST['location'];
	}
	if(isset($_POST['timeStart'])){	
		$timeStart = $_POST['timeStart'];
	}
	
	if(isset($_POST['binName'])){
		$binName = $_POST['binName'];
	} 
	$deploymentObject = new Deployment();
	
	
	
	if(!empty($binName)){
		$response = $deploymentObject->getBinId($binName);
		echo json_encode($response); 
	}else if(!empty($location) && !empty($binId) && !empty($userId)) {
		$response = $deploymentObject->insertDeployment($userId, $binId, $location);
		echo json_encode($response); 
	}
	else if(!empty($userId)){
		$response = $deploymentObject->retrieveData($userId);
		echo json_encode($response); 
	}else if(!empty($binId)){
		$response = $deploymentObject->stopBinDeployment($binId);
		echo json_encode($response); 
	}
	
	
?>

