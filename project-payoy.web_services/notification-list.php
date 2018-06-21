<?php
    
    require 'notification.php';
    
	
	
	//Store POST DATA to variables
	
   $message = "";
   $category = "";
   $notificationId = "";
   $userId = "";	

    if(isset($_POST['message'])){
        $message = $_POST['message'];
    }
    
    if(isset($_POST['category'])){
        
        $category = $_POST['category'];
        
    }
    if(isset($_POST['notificationId'])){
        $notificationId = $_POST['notificationId'];
    }
    
    if(isset($_POST['userId'])){
        $userId = $_POST['userId'];
    }
	

    $notificationObject = new Notification();
    
    // Registration
    
	if(!empty($message) && !empty($category)){
		$response = $notificationObject->insertNotificationMessages($message, $category);
		echo json_encode($response);
	}
 	else if(!empty($category)){
		if($category == "All"){
			$response = $notificationObject->retrieveAllUnreadNotifications();
			echo json_encode($response);
		}else if($category == "Battery Status"){
			$response = $notificationObject->retrieveUnreadNotificationsPerCategory($category);
			echo json_encode($response);
		} else if($category == "Bin Capacity"){
			$response = $notificationObject->retrieveUnreadNotificationsPerCategory($category);
			echo json_encode($response);
		}
	} else if(!empty($notificationId) && !empty($userId)){
             $response = $notificationObject->updateNotificationStatusToRead($notificationId, $userId);
	     echo json_encode($response);
        } 
	else{
		$response = $notificationObject->countUnreadNotifications();
		echo json_encode($response);
	}
	 
	
  
 ?>