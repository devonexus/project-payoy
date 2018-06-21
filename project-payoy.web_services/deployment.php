<?php
	include_once 'db-connect.php';
	
	class Deployment{
		
		private $db;
		private $db_table = "basuraju_db.deployment";
		private $db_table_bin = "basuraju_db.bin";
		
	
		
		public function __construct(){
            $this->db = new DbConnect();
        }
		public function retrieveData($userId){
			$json = array();
			$query = "SELECT bin_name, deployment_date_start, deployment_date_end, deployment_time_start, deployment_time_end, location FROM bin INNER JOIN ".$this->db_table." ON ".$this->db_table_bin.".bin_id = ".$this->db_table.".bin_id WHERE ".$this->db_table.".user_id = '$userId'";
			$result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result))
			{

				$binName 		= $row['bin_name'];
				$dateStart 		= "".date("m/d/Y", strtotime($row['deployment_date_start']));
				$dateEnd 		= "".date("m/d/Y", strtotime($row['deployment_date_end']));
				$location 		= $row['location'];
				$timeStart		= "".date("h:i:s A", strtotime($row['deployment_time_start']));
				$timeEnd 		= "".date("h:i:s A", strtotime($row['deployment_time_end']));
				$json[] = array("binName"=>$binName, "dateStart"=>$dateStart, "dateEnd"=>$dateEnd, "location"=>$location, "timeStart"=>$timeStart, "timeEnd"=>$timeEnd);
					
			}
			return $json;	
		}
		public function stopBinDeployment($binId){
			$json = array();
			$query = "UPDATE ".$this->db_table." SET deployment_date_end = CURDATE(), deployment_time_end = CURTIME() WHERE bin_id = '$binId'";
			$result = mysqli_query($this->db->getDb(), $query);
			if($result){
				$query = "UPDATE ".$this->db_table_bin." SET bin_status = 'Undeployed' WHERE bin_id = '$binId'";
				$result = mysqli_query($this->db->getDb(), $query);
				if($result){
					$json['message'] = "Deployment successfully stopped and bin status returned to undeployed";
				    $json['success'] = 1; 
				}else{
					$json['message'] = "Error stopping deployment and updating bin status";
				    $json['success'] = 0; 
				}
			}else{
				$json['message'] = "Error stopping deployment and updating bin status";
				$json['success'] = 2; 
			}
			
			return $json;
		}
		public function insertDeployment($userId, $binId, $location){
			$query = "INSERT into ".$this->db_table."(user_id, bin_id, deployment_date_start, deployment_time_start, location) values('$userId', '$binId',CURDATE(), CURTIME(), '$location')";


			$result = mysqli_query($this->db->getDb(), $query);
			if($result){
				$sql = "UPDATE ".$this->db_table_bin." SET bin_status = 'Deployed' WHERE bin_id = '$binId'";
				$result = mysqli_query($this->db->getDb(), $sql);
				if($result){
					$json['message'] = "Deployment data inserted and bin Status is deployed";
				    $json['result'] = 1; 
				}else{
					$json['message'] = "Error deploying and updating bin status";
				    $json['result'] = 2; 
				}
				
			}else{
				$json['message'] = "Error";
				$json['result'] = 0;
			}
			
			return $json;
		}
		
		public function getBinId($binName){
			global $binId;
			$query = "SELECT bin_id FROM ".$this->db_table_bin." WHERE bin_name = '$binName'";
			$result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result)){
				$binId = $row['bin_id'];
			}
			
			
			$json['binId'] = "".$binId;
			return $json;
			
		}
	}	
		
?>