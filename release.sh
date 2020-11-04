#bin/bash
cd "$(dirname "$0")"

appname=''
keystore=''
output_path='apk_release'

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

storage ()
{
  if [[ ! -d $output_path ]]
  then
    mkdir $output_path
  fi
  cp app/build/outputs/apk/release/*.apk $output_path
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
storage