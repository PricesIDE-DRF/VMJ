#!/bin/bash
set -e

source ./utils.sh

function check_configuration() {
    if ! grep -q "^$1=" "$CK_FILE"; then
        err "The configuration for $1 does not exist" && exit;
    fi
}

function check_module() {
    value=$(grep "^$1=" "$CK_FILE" | cut -d'=' -f2)
    count=$(echo "$value" | awk -F, '{print NF}')
    if [ "$count" -gt 1 ]; then
        sh ./genmodule.sh $1 $product
        echo "  generate module $1"
    fi

    check_external_module $1
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
        check_configuration $reqprod
        check_module $reqprod
        build_jar $reqprod
    done

    echo "requirement building done"
    build_product
}

CK_FILE="ck.properties"
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
    else check_configuration $product; build_module $product
    fi
else
    if [ ! -d "src/$1" ]; then  
        err "product does not exist" && exit;
    else mkdir $product; build_product_requirement $product
    fi
fi
