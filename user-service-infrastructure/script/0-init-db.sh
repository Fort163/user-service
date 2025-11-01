#!/bin/bash


psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'user_service'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE user_service"

psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'user_service_test'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE user_service_test"
