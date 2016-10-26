#!/usr/bin/env bash
cp main.kt asdf.asdf
mv main.kt asdf.out
echo "" >> *.kt
cat *.kt >> asdf.out
mv asdf.asdf main.kt
mv asdf.out out.kt
kotlinc out.kt -include-runtime -d out.jar
rm out.kt
