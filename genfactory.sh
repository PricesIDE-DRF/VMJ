package=$1
interface=$2
DIR="src/${package}.core/$(echo $package | tr "." "/")"
FILE="${interface}Factory.java"
sed -e "s/\[packagename]/$1/" -e "s/\[interfacename]/$2/g" TemplateFactory.java > $DIR/$FILE
echo "Factory class $FILE is created"