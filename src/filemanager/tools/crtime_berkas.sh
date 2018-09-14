#!/bin/bash

berkas="$2"
password="$1"

device=$(df "$berkas" | (read a; read a b; echo "$a"))
inode=$(stat -c %i "$berkas")

echo "$password" | sudo --stdin debugfs -R "stat <$inode>" "$device"