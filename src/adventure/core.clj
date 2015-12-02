(ns adventure.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]
               )
  (:gen-class))
; [clojure.core.match :refer [match]]
; [clojure.string :as str]
(def the-map
  {:bedroom{:desc "There is a bed, and a warm warm warm blanket that is pretty effective in putting you to sleep"
           :todo "south to the hallway "
           :title "in the bedroom"
           :dir {:south :hallway
                  }
           :content [:pen]
           } 
   :living_room {:desc "There is a TV, laptop, and a good life. But you shouldn't have a good life. "
              :title "in the living room"
              :todo "east to the hallway"
              :dir {:east :hallway
                  }
              :content [:calculator]
            }
   :hallway {:desc "It is very short, but not too short. "
              :title "the hallway"
              :todo "north: bedroom, south: door, east: kitchen, west: living room"
              :dir {:north :bedroom
                    :south :door
                    :east :kitchen
                    :west :living_room }
              :content []}
   :kitchen {:desc "It looks like you could cook food here. "
              :title "the kitchen"
              :todo "west to the hallway"
              :dir {:west :hallway
               }
              :content [:egg]}
   :door {:desc "a door"
          :title "a door"
          :todo "north to the hallway" 
          :dir {:north :hallway
                :south :eceb
                :east :siebal

            }
          :content []}
    :eceb {:desc "a good place, but worse than Siebal"
           :title "ECEB"
           :todo "go back to the door"
           :dir {:north :door}
         }
    :siebal{:desc "go there and study! :)"
            :title "Siebal"
            :todo "west to the door"
            :dir {:west :door}
    }
   })

(def adventurer
  {:location :bedroom
   :inventory #{}
   :before #{}})

(defn status [adv]
  (let [location (adv :location)
        inventory (adv :inventory)]
    (print (str "You are at " (-> the-map location :title) " and your inventory have. "))
    (when-not ((adv :before) location)
      (print (-> the-map location :desc) ) )
    (update-in adv [:before] #(conj % location))))

(defn go [dir adv]
  (let [curr-room (get-in adv [:location])]
   (if-let [dest (get-in the-map [curr-room :dir dir])]
     (assoc-in adv [:location] dest)
     (do (println "You cannot go that direction. ")
         adv) )))

(defn pick [dir adv]
  (let [curr-room (get-in adv [:location])]
   (if-let [dest (get-in the-map [curr-room :content 0])]
     (update-in adv [:content] dest)
     (println "You cannot go that direction. ")) ))

(defn respond [inst adv]
  (match inst
         [:north] (go :north adv)
         [:south] (go :south adv)
         [:west] (go :west adv)
         [:east] (go :east adv)
         [:pick] (pick inst adv)
         _ (do 
             (println "I'm sorry Dave.  I cannot allow you to do that.")
             adv) ) )

(defn to-keywords [st]
   (mapv keyword (str/split st #" +")))

(defn -main
  [& args]
  (println "Good morning! Fellow UIUC student, how was your sleep? Did you say you slept well eh? oh well I will make sure today is your night mare, get ready kiddo")
  (loop [the-m the-map
         the-a adventurer]
    (let [location (the-a :location)
          the-a' (status the-a)
          _      (println "What do you wanna do? You can go"  (str(-> the-m location :todo))) 
          inst   (read-line) ]
      (recur the-m (respond (to-keywords inst) the-a'))
      ) ) )
; (println "What do you want to do? You can go" (str (-> the-m (the-a :location) :title)))
