#!/bin/sh
#!/usr/bin/python
python3 ./config.py > config.txt
rm -rf ./uploads/*
chmod 775 config.txt
