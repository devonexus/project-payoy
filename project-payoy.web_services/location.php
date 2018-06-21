<?php
	include_once 'db-connect.php';
	
	class Location{
		
		private $db;
		private $db_table = "basuraju_db.location";
		
		
	
		
		public function __construct(){
            $this->db = new DbConnect();
        }
		
		
		public function insertCoordinates($latitude, $longitude){
			$query = "INSERT into ".$this->db_table."(latitude, longitude) values('$latitude', '$longitude')";
			$result = mysqli_query($this->db->getDb(), $query);
			if($result){
				$json['message'] = "Coordinates succesfully inserted";
				$json['result'] = 1;
			}else{
				$json['message'] = "Error";
				$json['result'] = 0;
			}
			
			
			return $json;
		}//add coordinates
		
		public function clearLocationTable(){
			$query = "TRUNCATE ".$this->db_table;
			$result = mysqli_query($this->db->getDb(), $query);
			if($result){
				$json['message'] = "Cleared Location Table";
				$json['result'] = 1;
			}else{
				$json['message'] = "Error";
				$json['result'] = 0;
			}
			
			
			return $json;
		}//Clear location table after deployment
        
		public function retrieveCoordinates(){
			$json = array();
			$query = "SELECT * FROM ".$this->db_table;
			$result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result)){
				//$bin_name = $row['bin_name'];
				//$json[] = array("binName"=>$bin_name);
				$json['lat'] = $row['latitude'];
				$json['long'] = $row['longitude'];
			}
			
			return $json;
		}//Retrieve bin coordinates
		
		
	}

?>