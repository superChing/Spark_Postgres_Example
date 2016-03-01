FROM gettyimages/spark
MAINTAINER superching

RUN cd /root \
  && mkdir -p data \
  && apt-get install -y unzip \
  && curl http://files.grouplens.org/datasets/movielens/ml-100k.zip -o ./data/ml-100k.zip \
  && cd data \
  && unzip ./ml-100k.zip

COPY ./target/scala-2.10/SparkMovielens-assembly-1.0.jar  /root/SparkMovielens-assembly-1.0.jar

WORKDIR /root

CMD java -cp /root/SparkMovielens-assembly-1.0.jar data.Import \
     && /usr/spark/bin/spark-submit \
      --master spark://master:7077 \
      --class compute.Run \
      /root/SparkMovielens-assembly-1.0.jar