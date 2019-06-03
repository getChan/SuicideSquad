#! /bin/bash

python3 rekognition.py > rekognition.txt
rm ./uploads/ex.jpg
chmod 775 rekognition.txt
