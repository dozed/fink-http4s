#!/bin/bash

psql -U postgres -h 127.0.0.1 -d fink -a -1 -f drop.sql
psql -U postgres -h 127.0.0.1 -d fink -a -1 -f create.sql

