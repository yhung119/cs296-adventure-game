(ns adventure.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]
               )
  (:gen-class))

(def the-map
  {
   :bedroom {:desc "There is a bed, and a warm warm warm blanket that is pretty effective in putting you to sleep"
           :todo "south to the hallway "
           :title "in the bedroom"
           :dir {:south :hallway
                  }
           :content {:egg "egg"
                     :what "what"}
           }
   :living_room {:desc "There is a TV, laptop, and a good life. But you shouldn't have a good life. "
              :title "in the living room"
              :todo "east to the hallway"
              :dir {:east :hallway
                  }
              :content {:calculator "calculator"}
            }
   :hallway {:desc "It is very short, but not too short. "
              :title "the hallway"
              :todo "north: bedroom, south: door, east: kitchen, west: living room"
              :dir {:north :bedroom
                    :south :door
                    :east :kitchen
                    :west :living_room }
              :content {}}
   :kitchen {:desc "It looks like you could cook food here. "
              :title "the kitchen"
              :todo "west to the hallway"
              :dir {:west :hallway
               }
              :content ["egg" "ggwi"]}
   :door {:desc "a door"
          :title "a door"
          :todo "north to the hallway"
          :dir {:north :hallway
                :south :eceb
                :east :siebal

            }
          :content {}}
    :eceb {:desc "a good place, but worse than Siebal"
           :title "ECEB"
           :todo "go back to the door"
           :dir {:north :door}
           :content {}
         }
    :siebal {:desc "go there and study! :)"
            :title "Siebal"
            :todo "west to the door"
            :dir {:west :door}
            :content {}
    }
   })

(def adventurer
  {:location :bedroom
   :name ""
   :hp 0
   :skill 0
   :social 0
   :inventory #{}
   :before #{}})

(defn status [adv]
  (let [location (adv :location)
        inventory (adv :inventory)]
    (print (str "You are at " (-> the-map location :title)"."))
    (when-not ((adv :before) location)
      (print (-> the-map location :desc) ) )
    (update-in adv [:before] #(conj % location))))
(defn adv_status [adv]
  (do (println (str (-> adv :location)" "(-> adv :name)" "(-> adv :hp) " " (-> adv :skill) " " (-> adv :social)))adv))
(defn go [dir adv]
  (let [curr-room (get-in adv [:location])]
   (if-let [dest (get-in the-map [curr-room :dir dir])]
     (assoc-in adv [:location] dest)
     (do (println "You cannot go that direction. ")
         adv) )))

(defn pick [obj adv]
  (let [curr-room (get-in adv [:location])
       inv (get-in adv [:inventory])]
   (if-let [dest (get-in the-map [curr-room :content obj])]
    (if (contains? inv dest) (do (println "You already have the item") adv)
        (update-in adv [:inventory] #(conj % dest)))
     (do (println "There is no such item. ")
        adv) )))

(defn dropItem [obj adv]
    (if-let [dest (get-in adv [:inventory obj])]
      (update-in adv [:inventory] #(dissoc % dest))
      (do (println "You don't have such item")
       adv)))

(defn print_inventory [adv]
    (do (println (seq (adv :inventory)))
      adv))

(defn print_content [adv]
    (let [location (adv :location)]
    (do (println (str (-> the-map location :content)))adv)))

(defn tp [location adv]
    )

(defn sleep [adv]
    (do (println "You are at full health")(assoc-in adv [:hp] 100)))

(defn respond [inst adv]
  (if (contains? inst 1)
  (match [(inst 0)]
          [:pick] (pick (inst 1) adv)
          [:dropItem] (dropItem (inst 1) adv)
          [_](do
              (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
              adv)
          )
  (match inst
         [:north] (go :north adv)
         [:south] (go :south adv)
         [:west] (go :west adv)
         [:east] (go :east adv)
         [:inventory] (print_inventory adv)
         [:content] (print_content adv)
         [:status] (adv_status adv)
         [:sleep] (sleep adv)
         _ (do
             (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
             adv) )
    ) )

(defn to-keywords [st]
   (mapv keyword (str/split st #" +")))

(defn -main
  [& args]
  (println "What is your name?")
  (let [n (read-line)
        adv-n (adventurer :name)
        adv' (assoc-in adventurer [:name] n)]
  (println (str "Good morning, "(str (-> adv' :name))"! How was your sleep? "))
  (loop [the-m the-map
         the-a adv']
    (let [location (the-a :location)
          the-a' (status the-a)
          _      (println (str " What do you wanna do? You can go "  (str(-> the-m location :todo))))
          inst   (read-line) ]
      (recur the-m (respond (to-keywords inst) the-a'))
      ) ) )
  )
; (println "What do you want to do? You can go" (str (-> the-m (the-a :location) :title)))
