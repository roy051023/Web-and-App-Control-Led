<?php
   	//user information
   	$host = "140.120.13.180";
   	$user = "05";
   	$pass = "tD9upZuaWyK5uKx4";

   	//database information
   	$databaseName = "iot";
   	$tableName = "switch";

   	//Connect to mysql database
   	$con = mysql_connect($host,$user,$pass);
   	$dbs = mysql_select_db($databaseName, $con);

    //Query database for data
   	$search=mysql_query("SELECT status FROM $tableName"); //check 目前狀態是關or開
   	$result=mysql_fetch_row($search);

   	//echo $result[0];
      if ($result[0] == "on"){
         echo "^on!";
      }else{
         echo "^off!";
      }
      
?>