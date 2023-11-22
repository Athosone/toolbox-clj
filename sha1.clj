#!/usr/bin/env bb
(defn sha1
  [s]
  (let [hashed (.digest (java.security.MessageDigest/getInstance "SHA-1")
                        (.getBytes s))
        sw (java.io.StringWriter.)]
    (binding [*out* sw]
      (doseq [byte hashed]
        (print (format "%02X" byte))))
    (str sw)))

(sha1 (first *command-line-args*))