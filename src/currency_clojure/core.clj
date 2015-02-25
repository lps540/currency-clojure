(ns currency-clojure.core
  (:gen-class))

(def ones-map
  (hash-map
    "0" ""
    "1" "one"
    "2" "two"
    "3" "three"
    "4" "four"
    "5" "five"
    "6" "six"
    "7" "seven"
    "8" "eight"
    "9" "nine"))

(def teens-map
  (hash-map
    "0" "ten"
    "1" "eleven"
    "2" "twelve"
    "3" "thirteen"
    "4" "fourteen"
    "5" "fifteen"
    "6" "sixteen"
    "7" "seventeen"
    "8" "eighteen"
    "9" "nineteen"))

(def tens-map
  (hash-map
    "0" ""
    "2" "twenty"
    "3" "thirty"
    "4" "forty"
    "5" "fifty"
    "6" "sixty"
    "7" "seventy"
    "8" "eighty"
    "9" "ninety"))

(def cluster-map
  (hash-map
    1 ""
    2 "thousand"
    3 "million"
    4 "billion"
    5 "trillion"))

(defn convert-decimal [s]
  (if (= s "") "" (str " and " s "/100")))

(defn last-three [s]
  (let [c (count s)]
    (if (< c 4) s (subs s (- c 3) c))))

(defn validate [s]
  (cond (.startsWith s "-") "Negative amounts not allowed."
        (not (.matches s "\\d*(\\.\\d\\d)?")) "Amount not recognized."
        (> (count s) 13) "Amount too large, quadrillion not supported."
        :else nil))

(defn chunkify1 [s]
  (cons (last-three s)
        (if (> (count s) 3)
          (chunkify1 (.substring s 0 (- (count s) 3))))))

(defn chunkify [s]
  (reverse (chunkify1 s)))

(defn pronounce-cluster [s]
  (let [size (count s)]
    (cond
      (= size 3)
        (if (= (subs s 0 1) "0")
          (pronounce-cluster (subs s 1))
          (str (ones-map (subs s 0 1)) " hundred " (pronounce-cluster (subs s 1))))

      (= size 2)
        (cond (= (subs s 0 1) "0") (pronounce-cluster (subs s 1))
              (= (subs s 0 1) "1") (teens-map (subs s 1))
              (= (.trim (subs s 1)) "0") (str (tens-map (subs s 0 1)) (pronounce-cluster (subs s 1)))
              :else (str (tens-map (subs s 0 1)) "-" (pronounce-cluster (subs s 1))))

      (= size 1) (ones-map s)
      (= size 0) "")))

(defn pronounce1 [list]
  (let [size (count list)]
    (cond (= size 1) (pronounce-cluster (first list))
          (= (first list) "000") (str (pronounce-cluster (first list)) (pronounce1 (rest list)))
          :else (str (pronounce-cluster (first list)) " " (cluster-map size) " " (pronounce1 (rest list))))))

(defn pronounce
  [currency]
  (let [s (.replaceAll currency "," "")
        errors (validate s)]
    (if errors
      errors
      (let [index (.indexOf s ".")
            first-part (if (> index -1) (subs s 0 index) s)
            decimal-part (if (> index -1) (subs s (+ index 1) (+ index 3)) "")
            t (str (.trim (pronounce1 (chunkify first-part))) (convert-decimal decimal-part))]

        (cond (= t "") "Zero dollars"
              (= t "one") "One dollar"
              :else (str (.toUpperCase (subs t 0 1)) (subs t 1) " dollars"))))))

(defn prompt []
  (println "Enter number (q to exit): ")
  (read-line))

(defn -main
  "Convert number to currency"
  [& args]
  (loop [line (prompt)]
    (if (not (= line "q"))
      (do
          (println (pronounce line))
          (recur (prompt))))))
