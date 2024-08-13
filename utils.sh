#!/bin/bash
set -e

err() {
    echo "Error: $*" >>/dev/stderr
}

function build_jar() {
    if [ ! -f "$product/$1.jar" ]; then
        javac -d classes --module-path $product $(find src/$1 -name "*.java") src/$1/module-info.java 
        jar --create --file $product/$1.jar -C classes .
        rm -r classes
        echo "  $1.jar is created"
    else 
        echo "  $1.jar is ready"
    fi
}

function check_external_module() {
    if [ $1 == "paymentgateway.payment.multicurrencysupport" ]; then
        cp external/gson.jar $product/
        echo "  add external module gson"
    fi
}