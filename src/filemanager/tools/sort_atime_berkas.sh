#!/bin/bash

path_berkas="$1"

ls -ltu "$path_berkas" | while read a b c d e f g h i; do echo $i; done