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

function check_module() {
    if [ $1 == "paymentgateway.payment.multicurrencysupport" ]; then
        cp external/gson.jar $product/
        echo "  add external module gson"
    fi
}

function build_module() {
    echo "build module $1"
    javac -d classes  --module-path lib $(find src/$1 -name "*.java") src/$1/module-info.java
    jar --create --file lib/$1.jar -C classes .
    rm -r classes
    echo "Module $1 is ready"
}

function build_product() {
    echo -e "building the product: $mainclass"
    javac -d classes  --module-path $product $(find src/$product -name "*.java") src/$product/module-info.java 
    jar --create --file $product/$mainclass.jar --main-class $product.$mainclass -C classes .
    rm -r classes
    echo "Product $mainclass is ready"
}

function build_product_requirement() {
    echo " -- checking requirement -- "
    product=$1
    targetpath="src/$product/module-info.java"
    req=$(cat $targetpath | grep "requires \( transitive | static \)\?"| awk '{print $2}' | cut -d';' -f 1 )
    for reqprod in $req; do
        echo -e "building requirement for $mainclass: $reqprod"
        check_module $reqprod
        build_jar $reqprod
    done

    echo "requirement building done"
    build_product
}

product=$1
mainclass=$2
if [ -d "$1" ]; then 
    rm -r $1;
fi
if [ -d "classes" ]; then 
    rm -r classes;
fi 
if [ ! -d "lib" ]; then 
    mkdir -p lib;
fi 
if [ -z "$mainclass" ]; then
    if [[ $1 =~ "paymentgateway.product" ]]; then 
        err "Please specify the main class in the product" && exit;
    elif [ ! -d "src/$1" ]; then 
        err "module does not exist" && exit;
    else build_module $product
    fi
else
    if [ ! -d "src/$1" ]; then  
        err "product does not exist" && exit;
    else mkdir $product; build_product_requirement $product
    fi
fi
