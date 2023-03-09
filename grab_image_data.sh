#! /bin/sh

# ./grab_image_data.sh input_dir tif

input_dir="$1"
image_ext="$2"


IFS="
"
for path in $(find "$input_dir" -name "*.$image_ext" -printf '%P\n')
do
    wh=$(identify -format "%w,%h" "$input_dir/$path")
    echo "$path,$wh"
done
