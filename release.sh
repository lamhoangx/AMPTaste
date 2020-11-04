#bin/bash

appname=''
keystore=''

usage()
{
    echo "usage: build_script [[-n app_name ] [-k keystore_path]] | [-h]]"
}

build ()
{
  echo "Building $appname | $keystore"
  ./gradlew clean -Pappname=$appname -Pkeystore=$keystore
  ./gradlew cleanBuildCache -Pappname=$appname -Pkeystore=$keystore
  ./gradlew assembleRelease -Pappname=$appname -Pkeystore=$keystore
}

while [ "$1" != "" ]; do
    case $1 in
        -n | --appname )        shift
                                appname=$1
                                ;;
        -k | --keystore )       shift
                                keystore=$1
                                ;;
        -h | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

build