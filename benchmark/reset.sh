#!/bin/sh

. "./env/mysql.env.sh"
python3 db/mysql-reset.py
