(ns currency-clojure.core-test
  (:require [clojure.test :refer :all]
            [currency-clojure.core :refer :all]))

(deftest last-three-test
  (testing "last-three"
    (is (= "1" (last-three "1")))
    (is (= "12" (last-three "12")))
    (is (= "123" (last-three "123")))
    (is (= "234" (last-three "1234")))
    (is (= "345" (last-three "12345")))
    (is (= "456" (last-three "123456")))
    (is (= "567" (last-three "1234567")))))

(deftest convert-decimal-test
  (testing "convert-decimal"
    (is (= " and 25/100" (convert-decimal 25)))
    (is (= "" (convert-decimal "")))))

(deftest validate-test
  (testing "validate"
    (is (= nil (validate "1")))
    (is (= nil (validate "1.23")))
    (is (= "Negative amounts not allowed." (validate "-1.23")))
    (is (= "Amount not recognized." (validate "1.234")))
    (is (= "Amount not recognized." (validate "1A2")))))

(deftest chunkify-test
  (testing "chunkify"
    (is (= '("1") (chunkify "1")))
    (is (= '("1" "234") (chunkify "1234")))
    (is (= '("1" "234" "567") (chunkify "1234567")))))

(deftest pronounce-base-test
  (testing "pronounce-base"
    (is (= "", (pronounce-base "")))
    (is (= "", (pronounce-base "0")))
    (is (= "", (pronounce-base "00")))
    (is (= "", (pronounce-base "000")))
    (is (= "one", (pronounce-base "1")))
    (is (= "one", (pronounce-base "01")))
    (is (= "one", (pronounce-base "001")))
    (is (= "ten", (pronounce-base "10")))
    (is (= "twenty", (pronounce-base "20")))
    (is (= "eleven", (pronounce-base "11")))
    (is (= "twenty-two", (pronounce-base "22")))
    (is (= "one hundred twenty-three", (pronounce-base "123")))
    (is (= "one hundred one", (pronounce-base "101")))))

(deftest pronounce1-test
  (testing "pronounce1"
    (is (= "one" (pronounce1 '("1"))))
    (is (= "one hundred twenty-three" (pronounce1 '("123"))))))

(deftest pronounce-test
  (testing "pronounce"
    (is (= "One dollar" (pronounce "1")))
    (is (= "One hundred twenty-three dollars" (pronounce "123")))
    (is (= "One hundred twenty-three and 45/100 dollars" (pronounce "123.45")))))
