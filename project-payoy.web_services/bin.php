<?php
	include_once 'db-connect.php';
	
	class Bin{
		
		private $db;
		private $db_table = "basuraju_db.bin";
		private $db_table_location = "basuraju_db.location";
		
	
		
		public function __construct(){
            $this->db = new DbConnect();
        }

        public function updateBin($binId, $binName, $userId){

			$isExisting = $this->isBinNameExist($binName, $userId);
			
			if($isExisting){
				$json['success'] = 0;
				$json['message'] = "Duplicate bin name.";
			}else{
				$query 	= "UPDATE ".$this->db_table." SET bin_name = '$binName' WHERE bin_id = '$binId'";
				$result = mysqli_query($this->db->getDb(), $query);
			
				if($result == 1){
					$json['success'] = 1;
				
				} else{
					$json['success'] = 2;
					$json['message'] = "Error in updating bin";
				}
				mysqli_close($this->db->getDb());
			}

			return $json;
		}

           
           public function retrieveBinName($userId){
			$json = array();
			$query = "SELECT bin_name FROM ".$this->db_table." WHERE user_id = '$userId' AND bin_status = 'Undeployed'";
			$result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result)){
				$bin_name = $row['bin_name'];
				$json[] = array("binName"=>$bin_name);
			}
			
			return $json;
		}
          public function getCoordinates(){
			$json = array();
			$query = "SELECT * FROM ".$this->db_table_location;
			$result = mysqli_query($this->db->getDb(), $query);
			if(mysqli_num_rows($result) > 0){
				while($row = mysqli_fetch_assoc($result)){
					$json['lat'] = $row['latitude'];
					$json['long'] = $row['longitude'];
				}
			}else{
				$json['lat'] = "0";
				$json['long'] = "0";
			}
            return $json;
			
		}//Get bin coordinates
		public function deleteBin($binId, $userId){
			$query = "DELETE FROM ".$this->db_table." WHERE bin_id = '$binId' AND user_id = '$userId'";
			$result = mysqli_query($this->db->getDb(), $query);
			if($result == 1){
			
				$json['success'] = 1;
				$json['undeployedBinCount'] = "Goes here";
			} else{
				$json['success'] = 0;
			}
			mysqli_close($this->db->getDb());
			return $json;
		}
       public function retrieveBin($userId, $status){
			$json = array();
			$solution = array();
			global $bin_id;
			global $bin_name;
			global $bin_ip_address;
			
			$query = "SELECT * FROM ".$this->db_table." WHERE user_id = '$userId' AND bin_status = '$status'";
			$result = mysqli_query($this->db->getDb(), $query);
			
			
			while($row = mysqli_fetch_assoc($result)){
				$bin_status 		= $row['bin_status'];
				$bin_id 			= $row['bin_id'];
				$bin_name 			= $row['bin_name'];
		
				
				//Put key value pairs inside an array, and store in an array
				$json[] = array("binId"=>$bin_id, "binName"=>$bin_name, "binStatus"=>$bin_status);
			}
		
			return $json;
		}
		public function isBinNameExist($binName, $userId){
				
            $query = "SELECT * FROM ".$this->db_table." WHERE bin_name = '$binName' AND user_id = '$userId'";
            
            $result = mysqli_query($this->db->getDb(), $query);
            
            if(mysqli_num_rows($result) > 0){
                
                mysqli_close($this->db->getDb());
                
                return true;
                
            }
               
            return false;
            
        }//Check for duplicate username

		public function registerBin($bin_name, $userId){
			$isExisting = $this->isBinNameExist($bin_name, $userId);
			
			if($isExisting){
				$json['success'] = 0;
				$json['message'] = "Duplicate bin name.";
			}else{
				$query = "INSERT into ".$this->db_table."(bin_name, bin_status, user_id) values('$bin_name', 'Undeployed', '$userId')";
				$result = mysqli_query($this->db->getDb(), $query);
				if($result == 1){
					$json['success'] = 1;
				
				} else{
					$json['success'] = 2;
					$json['message'] = "Error in creating bin";
				}
				mysqli_close($this->db->getDb());
			}
			return $json;
		}
		
		
		
	}

?>