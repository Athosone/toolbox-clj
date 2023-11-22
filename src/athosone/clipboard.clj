(ns athosone.clipboard)

(import [java.awt.datatransfer StringSelection])

(defn get-clipboard []
  (-> (java.awt.Toolkit/getDefaultToolkit)
      (.getSystemClipboard)))

(defn copy [text]
  (let [^java.awt.datatransfer.StringSelection selection (StringSelection. text)]
    (.setContents (get-clipboard) selection selection)
    selection))

(copy "Hello, World!")





