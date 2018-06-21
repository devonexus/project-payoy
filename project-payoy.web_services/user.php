<?php
    
    include_once 'db-connect.php';
    
    class User{
        
        private $db;
        
        private $db_table = "basuraju_db.user";
        
        public function __construct(){
            $this->db = new DbConnect();
        }
		
        public function validateLogin($username, $password){
			$json = array();
			global $check_username;
			global $check_password;
			global $check_fullname;
			global $check_image;
			global $check_email; 
			global $check_firstName;
			global $check_lastName;
			global $check_middleInitial;
			global $check_password;
			global $check_userid;
			$query = "SELECT * FROM user WHERE user_username = '$username'";
            
            $result = mysqli_query($this->db->getDb(), $query);
			while($row = mysqli_fetch_assoc($result))
			{
				$check_userid			= $row['user_id'];
				$check_username 		= $row['user_username'];
				$check_password 		= $row['user_password'];
				$check_fullname 		= $row['user_lname']." ".$row['user_fname']." ".$row['user_minitial'];
				$check_image			= $row['user_image'];
				$check_email 			= $row['user_email'];
				$check_firstName 		= $row['user_fname'];
				$check_lastName  		= $row['user_lname'];
				$check_middleInitial 	= $row['user_minitial'];
			
			}
			
			
			if($username == $check_username){
				if($password == $check_password){
					$json['success'] 	= 1;
					$json['userId'] = "".$check_userid;
					$json['message'] 	= "Successfully logged in";
					$json['password'] 	= "".$check_password;
					$json['username'] 	= "".$check_username;
					$json['fullname'] 	= "".$check_fullname;
					$json['image'] 		= "".$check_image;
					$json['emailAddress']		= "".$check_email;
					$json['firstName'] 	= "".$check_firstName;
					$json['lastName'] 	=  "".$check_lastName;
					$json['middleInitial'] = "".$check_middleInitial;
					$json['password'] = "".$check_password	;
				} 
				else if($password != $check_password){
					$json['success'] = 0;
					$json['message'] = "Incorrect password";
				}
			}
			else if($username != $check_username){
				
				$json['success'] = 2;
				$json['message'] = "Username does not exist";
			}
 
	
			return $json;
		}
		
		 
		
		 public function isLoginExist($username, $password){
            
            $query = "SELECT * FROM User WHERE user_username = '$username' AND user_password = '$password'";
            
            $result = mysqli_query($this->db->getDb(), $query);
         
			
            if(mysqli_num_rows($result) > 0){
               
                mysqli_close($this->db->getDb());
                
                
                return true;
                
            } 
			
			
            
            mysqli_close($this->db->getDb());
            
            return false;
            
        }//Check existing user credentials
		
		
        public function loginUsers($username, $password){
            
			$json = array();
			
          
		
            $canUserLogin = $this->isLoginExist($username, $password); //Correct username and password
			
			if($canUserLogin){
                
                $json['success'] = 1;
                $json['message'] = "Successfully logged in";
                
            }else{
				
					$json['success'] = 0;
					$json['message'] = "Incorrect password";
				
			}
		
            return $json;
        }
		public function updateExistingUserExceptImage($lname, $fname, $minitial, $email, $username, $password){
			  
						global $check_username;
						global $check_password;
						global $check_fullname;
						global $check_image;
						global $check_email; 
						global $check_firstName;
						global $check_lastName;
						global $check_middleInitial;
					
					
						
						//$query = "UPDATE ".$this->db_table." SET user_lname = '$lname', user_fname = '$fname', user_minitial = '$minitial', user_email = '$email', user_password = '$password' WHERE user_username = '$username'";
						$query = "UPDATE ".$this->db_table." SET user_lname = '$lname', user_fname = '$fname', user_minitial = '$minitial', user_email = '$email', user_password = '$password' WHERE user_username = '$username'";
						$updated = mysqli_query($this->db->getDb(), $query);
						$affected = mysqli_affected_rows($this->db->getDb());
						
						if($affected == 1){
							$sqlstmt = "SELECT * FROM user WHERE user_username = '$username'";
							$result = mysqli_query($this->db->getDb(), $sqlstmt);
							while($row = mysqli_fetch_assoc($result))
							{
								$check_username 		= $row['user_username'];
								$check_fullname 		= $row['user_lname']." ".$row['user_fname']." ".$row['user_minitial'];
								$check_image			= $row['user_image'];
								$check_email 			= $row['user_email'];
								$check_firstName 		= $row['user_fname'];
								$check_lastName  		= $row['user_lname'];
								$check_middleInitial 	= $row['user_minitial'];
								$check_password			= $row['user_password'];
							}
								$json['success'] 	= 1;
								$json['message'] = "Successfully updated the user";
								$json['password'] 	= "".$check_password;
								$json['username'] 	= "".$check_username;
								$json['fullname'] 	= "".$check_fullname;
								$json['image'] 		= "".$check_image;
								$json['emailAddress']		= "".$check_email;
								$json['firstName'] 	= "".$check_firstName;
								$json['lastName'] 	=  "".$check_lastName;
								$json['middleInitial'] = "".$check_middleInitial;
							
							
						} else{
							$json['success'] = 0;
							$json['message'] = "Username does not exists";
						
						} 
						
						
					
						mysqli_close($this->db->getDb());
        
                
          
            	
			return $json;
		}
		public function isUsernameExist($userName){
				
	            $query = "SELECT * FROM ".$this->db_table." WHERE user_username = '$userName'";
	            
	            $result = mysqli_query($this->db->getDb(), $query);
	            
	            if(mysqli_num_rows($result) > 0){
	                
	                mysqli_close($this->db->getDb());
	                
	                return true;
	                
	            }
	               
	            return false;
            
        	}	
		
		public function updateExistingUser($lname, $fname, $minitial, $email, $username, $password, $path){
			  
						global $check_username;
						global $check_password;
						global $check_fullname;
						global $check_image;
						global $check_email; 
						global $check_firstName;
						global $check_lastName;
						global $check_middleInitial;
						global $check_password;
					
					
						
						$query = "UPDATE ".$this->db_table." SET user_lname = '$lname', user_fname = '$fname', user_minitial = '$minitial', user_email = '$email', user_password = '$password', user_image = '$path' WHERE user_username = '$username'";
						//$query = "UPDATE ".$this->db_table." SET user_lname = '$lname', user_fname = '$fname', user_minitial = '$minitial', user_email = '$email', user_password = '$password' WHERE user_username = '$username'";	
						$updated = mysqli_query($this->db->getDb(), $query);
						$affected = mysqli_affected_rows($this->db->getDb());
						
						if($affected == 1){
							$sqlstmt = "SELECT * FROM user WHERE user_username = '$username'";
							$result = mysqli_query($this->db->getDb(), $sqlstmt);
							while($row = mysqli_fetch_assoc($result))
							{
								$check_username 		= $row['user_username'];
								$check_password 		= $row['user_password'];
								$check_fullname 		= $row['user_lname']." ".$row['user_fname']." ".$row['user_minitial'];
								$check_image			= $row['user_image'];
								$check_email 			= $row['user_email'];
								$check_firstName 		= $row['user_fname'];
								$check_lastName  		= $row['user_lname'];
								$check_middleInitial 	= $row['user_minitial'];
								$check_password			= $row['user_password'];
							}
								$json['success'] 	= 1;
								$json['message'] = "Successfully updated the user";
								$json['password'] 	= "".$check_password;
								$json['username'] 	= "".$check_username;
								$json['fullname'] 	= "".$check_fullname;
								$json['image'] 		= "".$check_image;
								$json['emailAddress']		= "".$check_email;
								$json['firstName'] 	= "".$check_firstName;
								$json['lastName'] 	=  "".$check_lastName;
								$json['middleInitial'] = "".$check_middleInitial;
								$json['password'] = "".$check_password	;
							
							
						} else{
							$json['success'] = 0;
							$json['message'] = "Username does not exists";
						
						} 
						
						
					
						mysqli_close($this->db->getDb());
        
                
          
            	
			return $json;
		}
		
		
		public function createNewRegisterUser($lname, $fname, $minitial, $email, $username, $password, $path){
            
			
            $isExisting = $this->isUsernameExist($username);
            
            if($isExisting){
                
                $json['success'] = 0;
                $json['message'] = "Username already exists.";
            }
            
            else{
					
					
					
						$query = "INSERT into ".$this->db_table."(user_lname, user_fname, user_minitial, user_email, user_username, user_password, user_image) values('$lname', '$fname', '$minitial', '$email', '$username', '$password', '$path')";					
						$inserted = mysqli_query($this->db->getDb(), $query);
								
						if($inserted == 1){
							$json['success'] = 1;
							$json['message'] = "Successfully registered the user";
						}else{
							$json['success'] = 2;
							$json['message'] = "Error in registering. Probably the username/email already exists";
						}
					
						mysqli_close($this->db->getDb());
        
                
            }
            	
			return $json;
            
        }//Create new users
		
    }
    ?>