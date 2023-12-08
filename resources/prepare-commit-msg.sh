#!/bin/bash
set -eou pipefail

# If the co-author file exists and is not empty, then add the co-authors to the commit message
if [ -s .git/hooks/co-authors.txt ]; then
  echo "" >> $1
  cat .git/.co-authors >> $1
fi