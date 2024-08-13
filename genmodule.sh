#!/bin/bash
set -e

source ./utils.sh

function gen_class() {
    FILE="$DIR/$impl_file"
    cp src/$1/${1//./\/}/$impl_file $FILE
    sed -i "1s/.*/package $MOD;/" "$FILE"
}

function composedelta() {
    while IFS== read key value || [[ -n "$value" ]]; do
        if [ "$1" == "$key" ]; then
            for i in $(echo $value | tr "," "\n"); do
                check_external_module $i
                build_jar  $i
                echo "    requires $i;" >> $MODINFO

                if [ "$impl_file" == "" ]; then
                    impl_file_full_path=$(find "src/$i" -type f -name '*Impl.java' -print -quit)
                    impl_file=$(basename "$impl_file_full_path")
                fi
            done
            echo "}" >> $MODINFO;
        fi
    done < "$CK_FILE"
}

CK_FILE="ck.properties"
MOD=$1
product=$2
DIR="src/$MOD/$(echo $MOD | tr "." "/")"

if [ -d "$DIR" ]; then 
    rm -r $DIR; 
fi

mkdir -p $DIR
MODINFO="src/$MOD/module-info.java"
core_module=$(echo "$1" | sed 's/\.[^.]*$/\.core/')
impl_file=""

value=$(grep "^$1=" ck.properties | cut -d'=' -f2)
if [ "$value" == "" ]; then
    err "The configuration for $1 does not exist" && exit;
else last_value=$(echo "$value" | awk -F',' '{print $NF}')
fi

echo "module $MOD {
    exports $MOD;
    requires $core_module;" > $MODINFO

composedelta $1

if [ "$impl_file" == "" ]; then
    err "Implementation file does not exist" && exit;
else gen_class $last_value
fi
