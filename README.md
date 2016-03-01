# Spark + Postgres Example
Example of Spark + PostgreSQL on Movielens data

Compute top 20 movies of Movielens data (ml-100k), using Spark and PostgreSQL. 
Saprk is a standalone mode cluster in Docker.

**RUN:**  
1. `sbt assembly`  
2. wire the containers and download the dataset.  
```sudo docker-compose up -d```  
3. import data to database.  
`sudo docker-compose run myapp java -cp /root/SparkMovielens-assembly-1.0.jar data.Import`  
4. compute top ratings.  
```
sudo docker-compose run myapp /usr/spark/bin/spark-submit --master spark://master:7077 --class compute.Run /root/SparkMovielens-assembly-1.0.jar
```

