(ns blitz-registration.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]])
  (:gen-class))

(def PORT 5000)

(defn date-to-sec-since-1970
  [year month day]
  (let [date (doto
               (java.util.Calendar/getInstance)
               (.set year month day))]
    (int (/ (.getTimeInMillis date) 1000))))

(def team-info {:teamName "Ainsi va la vim"
                :teamMembers [
                              {:firstName "Alexandre"
                               :lastName "Gariépy"
                               :email "gariepy.alex@gmail.com"
                               :phoneNumber "418-265-6851"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2016 05 31)
                               :inCharge true}

                              {:firstName "Alexandre"
                               :lastName "Gariépy"
                               :email "gariepy.alex@gmail.com"
                               :phoneNumber "418-265-6851"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2016 05 31)
                               :inCharge false}

                              {:firstName "Alexandre"
                               :lastName "Gariépy"
                               :email "gariepy.alex@gmail.com"
                               :phoneNumber "418-265-6851"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2016 05 31)
                               :inCharge false}

                              {:firstName "Alexandre"
                               :lastName "Gariépy"
                               :email "gariepy.alex@gmail.com"
                               :phoneNumber "418-265-6851"
                               :educationalEstablishment "Université Laval"
                               :studyProgram "Génie logiciel"
                               :dateProgramEnd (date-to-sec-since-1970 2016 05 31)
                               :inCharge false}
                              ]
                })


(defn indices-of-paragraphs-matching-word
  [word paragraphs]
  (let [word-regex (re-pattern (str "(?i).*(?:\\s|^)" word "(?:\\s|$).*"))]
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
