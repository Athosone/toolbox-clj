java ?= $(shell which java)

build:
	clojure -T:build ci

install: ./target/net.clojars.athosone/toolbox.jar
	echo "#!$(java) -jar" > target/toolbox
	cat  ./target/net.clojars.athosone/toolbox.jar >> target/toolbox
	chmod +x target/toolbox
	sudo mv target/toolbox /usr/local/bin/toolbox


./target/net.clojars.athosone/toolbox.jar: build