#!/usr/bin/env bash

#java -Xmx1024m MainKt.class $@
java -jar out.jar $@ 2> /dev/null
