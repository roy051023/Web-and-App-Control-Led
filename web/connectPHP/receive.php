<?php
  
  $light = $_GET['value'];


  //user information
  $host = "140.120.13.180";
  $user = "05";
  $pass = "tD9upZuaWyK5uKx4";

  //database information
  $databaseName = "iot";
  $tableName = "light";


  //Connect to mysql database
  $con = mysql_connect($host,$user,$pass);
  $dbs = mysql_select_db($databaseName, $con);


  //Query database for data
  $result = mysql_query("insert into `$databaseName`.`$tableName` (`data`) VALUES ('$light')");

  //store matrix
  if($result==1)
    echo "success";
  else
    echo "error";
?>