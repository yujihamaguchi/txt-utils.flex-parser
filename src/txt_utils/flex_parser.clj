(ns txt-utils.flex-parser
  (:require [instaparse.core :as insta])
  (:use [clojure.pprint]
        [clojure.java.io :as io])
  (:import [java.util.zip GZIPInputStream]
           [java.util.zip GZIPOutputStream]))

(defmacro bench [expr cnt]
  `(str 
    (quot
      (reduce +
        (for [_# (range ~cnt)]
          (let [start-time# (System/currentTimeMillis)]
            ~expr
            (- (System/currentTimeMillis) start-time#)))) ~cnt) "msec"))

(defn slurp-gzip [gzip-file-name]
  (with-open [in (GZIPInputStream. (io/input-stream gzip-file-name))]
    (slurp in)))

(defn spit-gzip [gzip-file-name contents]
  (with-open [w (-> gzip-file-name
                    io/output-stream
                    GZIPOutputStream.
                    io/writer)]
    (binding [*out* w]
      (print contents))))

(defn file-parse [ebnf-file-name file-name]
  ((insta/parser (slurp ebnf-file-name)) (slurp file-name)))

