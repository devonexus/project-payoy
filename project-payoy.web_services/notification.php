<?php
	include_once 'db-connect.php';
	
	class Notification{
		
		private $db;
		private $db_table = "basuraju_db.notification";
		
	
		
		public function __construct(){
            $this->db = new DbConnect();
        }
		 public function updateNotificationStatusToRead($notificationId, $userId){

			$query 	= "UPDATE ".$this->db_table." SET notification_status = 'Read', user_id = '$userId' WHERE user_id = 0 AND notification_id = '$notificationId'";
			$result = mysqli_query($this->db->getDb(), $query);
			
			if($result == 1){
				$json['success'] = 1;
				$json['message'] = "Notification is changed to read status";
			
			} else{
				$json['success'] = 0;
			}
			mysqli_close($this->db->getDb());
			return $json;
		}

		public function insertNotificationMessages($message, $category){
			$query = "INSERT into ".$this->db_table."(notification_message, notification_date, notification_time, notification_category, notification_status) values('$message', CURDATE(), CURTIME(), '$category', 'Unread')";
			$result = mysqli_query($this->db->getDb(), $query);
			if($result == 1){
				$json['message'] = "Notification sent";
			}else{
				$json['message'] = "Notification unsent";
			}
			
			return $json;
		}
		
		public function retrieveUnreadNotificationsPerCategory($category){
			$json = array();
			$query = "SELECT * FROM ".$this->db_table." WHERE notification_category = '$category' AND notification_status = 'Unread'";
			$result = mysqli_query($this->db->getDb(), $query);
			
			
				while($row = mysqli_fetch_assoc($result)){
									$notificationId                 = $row['notification_id'];
					$notificationTitle 		= $row['notification_category'];
					$notificationDate 		= "".date("m/d/Y", strtotime($row['notification_date']));
					$notificationTime		= "".date("h:i:s A", strtotime($row['notification_time']));
					$notificationMessage	= $row['notification_message'];
					//Put key value pairs inside an array, and store in an array
					$json[] = array("notificationId"=>$notificationId, "notificationMessage"=>$notificationMessage, "notificationDate"=>$notificationDate, "notificationTime"=>$notificationTime, "notificationTitle"=>$notificationTitle);
				}
			
			
			return $json;
		}
		public function retrieveAllUnreadNotifications(){
			$query = "SELECT * FROM ".$this->db_table." WHERE notification_status = 'Unread'";
			$result = mysqli_query($this->db->getDb(), $query);
			
			while($row = mysqli_fetch_assoc($result)){
				$notificationTitle 		= $row['notification_category'];
				$notificationDate 		= "".date("m/d/Y", strtotime($row['notification_date']));
				$notificationTime		= "".date("h:i:s A", strtotime($row['notification_time']));
				$notificationMessage	= $row['notification_message'];
				//Put key value pairs inside an array, and store in an array
				$json[] = array("notificationMessage"=>$notificationMessage, "notificationDate"=>$notificationDate, "notificationTime"=>$notificationTime, "notificationTitle"=>$notificationTitle);
			}
		
			return $json;
		}
		public function countUnreadNotifications(){
			$query = "SELECT COUNT(*) AS notification_count FROM ".$this->db_table." WHERE notification_status = 'Unread'";
			$result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result)){
				$count = $row['notification_count'];
			}
			$json['notificationCount'] =  $count;
			return $json;
		}
		
	}

?>