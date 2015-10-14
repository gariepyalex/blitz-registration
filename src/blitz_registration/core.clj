(ns blitz-registration.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]])
  (:gen-class))

(def PORT (Integer/parseInt (System/getenv "PORT")))

(defn date-to-sec-since-1970
  [year month day]
  (let [date (doto
               (java.util.Calendar/getInstance)
               (.set year month day))]
    (int (/ (.getTimeInMillis date) 1000))))

(def team-info {:teamName "'(:kiss-my-lisp)"
                :teamMembers [
                              {:firstName "Alexandre"
                               :lastName "Gariépy"
                               :email "gariepy.alex@gmail.com"
                               :phoneNumber "418-265-6851"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2016 04 31)
                               :inCharge true}

                              {:firstName "David"
                               :lastName "Arel"
                               :email "david.arel.1@ulaval.ca"
                               :phoneNumber "418-934-8766"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2017 04 31)
                               :inCharge false}

                              {:firstName "Adam"
                               :lastName "B.-Bolduc"
                               :email "adam.b.bolduc@gmail.om"
                               :phoneNumber "418-930-8569"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2017 04 31)
                               :inCharge false}

                              {:firstName "David"
                               :lastName "Landry"
                               :email "davidlandry93@gmail.com "
                               :phoneNumber "418-265-4830"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Informatique"
                               :dateProgramEnd (date-to-sec-since-1970 2016 04 31)
                               :inCharge false}
                              ]
                })


(defn indices-of-paragraphs-matching-word
  [word paragraphs]
  (let [word-regex (re-pattern (str "(?i).*" word ".*"))]
    (into [] (map first
                  (filter #(re-matches word-regex (second %)) paragraphs)))))

(defn handler
  [request]
  (let [{word-to-search "q" paragraphs "paragraphs"} (:body request)
        indices (indices-of-paragraphs-matching-word word-to-search paragraphs)]
    (response (assoc team-info :matchedParagraphs indices))))

(def app
  (->
    handler
    wrap-json-response
    wrap-json-body))

(defn -main
  []
  (jetty/run-jetty app {:port PORT}))
