#!/bin/bash
set -eou pipefail

co_authors_file=".git/co-authors.txt"

# If the co-author file exists and is not empty, then add the co-authors to the commit message
if [ -s $co_authors_file ]; then
  echo "" >> $1
  cat $co_authors_file >> $1
fi