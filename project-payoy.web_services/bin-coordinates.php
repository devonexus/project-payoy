<?php
	
	require 'location.php';
	
	if(isset($_POST['requestType'])){
		$requestType = $_POST['requestType'];
	}
		
	if(isset($_POST['lat'])){	
		$latitude = $_POST['lat'];
	}
	
	if(isset($_POST['long'])){	
		$longitude = $_POST['long'];
	}
	
	$locationObject = new Location();
	
	
	
		
	
	
	/*
	**
	** This will determine the type of request sent 
	**
	*/
	if(isset($_POST['requestType'])){
		if($requestType === 'retrieve'){
			$json_array = $locationObject->retrieveCoordinates();
			echo json_encode($json_array);
		} else if($requestType == 'clear'){
			$json_array = $locationObject->clearLocationTable();
			echo json_encode($json_array);
		}
	}else{
		$json_array = $locationObject->insertCoordinates($latitude, $longitude);
		echo json_encode($json_array);
	}
	
?>