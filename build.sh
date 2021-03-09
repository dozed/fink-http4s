#!/usr/bin/env bash

cd frontend-editor
npm run build
cd ..

cd frontend-site
npm run build
cd ..

sbt assembly

rm -rf app/editor/*
rm -rf app/site/*

mkdir -p app
cp -R frontend-editor/dist app/editor
cp -R frontend-site/dist app/site
cp target/scala-2.13/fink-assembly-*.jar app/backend.jar
