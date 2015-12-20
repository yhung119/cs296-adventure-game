(ns adventure.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]
               )
  (:gen-class))

(def the-map
  {
   :bedroom {:desc "There is a bed, and a warm warm warm blanket that is pretty effective in putting you to sleep"
           :todo "south to the hallway "
           :title "in the bedroom "
           :dir {:south :hallway
                  }
           :content #{"cellphone"}
           }
   :living_room {:desc "There is a TV, laptop, and a good life. But you shouldn't have a good life."
              :title "in the living room "
              :todo "east to the hallway"
              :dir {:east :hallway
                  }
              :puzzle [
                    {:question "Fill in the blank. The following is the lyrcis from a song by Taylor Swift: I don't know about you. But I feel like __."
                     :answer "22"}
                    {:question "Which place are you if you race over the second place?(leave answer in a number)"
                     :answer "2"}
                    {:question "How many stones did it take to complete the pyramids?"
                     :answer "1"}
                    {:question "1 11 21 1211 111221, what is the next number to this pattern?"
                     :answer "312211"}
                    {:question "What is the remainder when 2 ^ 212 divdes 11 ?"
                     :answer "4"}

              ]
              :content #{"calculator", "key"}
            }
   :hallway {:desc "It is very short, but not too short. "
              :title "the hallway"
              :todo "north: bedroom, south: door, east: kitchen, west: living room"
              :dir {:north :bedroom
                    :south :door
                    :east :kitchen
                    :west :living_room }
              :content #{}}
   :kitchen {:desc "It looks like you could cook food here. "
              :title "the kitchen "
              :todo "west to the hallway"
              :dir {:west :hallway
               }
              :content #{"egg"}}
   :door {:desc "a door"
          :title "a door "
          :todo "north to the hallway"
          :dir {:north :hallway
                :south :eceb
                :east :siebal
            }
          :content #{}}
    :eceb {:desc "a good place, but worse than Siebal"
           :title "ECEB "
           :todo "go back to the door"
           :dir {:north :door}
           :content #{}
         }
    :siebal {:desc "go there and study! :)"
            :title "Siebal "
            :todo "west to the door"
            :dir {:west :door}
            :content #{}
    }
    :bar {:desc "What do you think you can do here?"
          :title "Bar"
          :todo "drink"
          :dir { :east :door}
          :content #{}
  }
       })

(def adventurer
  {:location :bedroom
   :name ""
   :hp 100
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

;the function for "status" which prints out the adv's current status
(defn adv_status [adv]
  (do (println (str "Name: "(-> adv :name)". HP: "(-> adv :hp) ". Skill Point: " (-> adv :skill) ". Social Point: " (-> adv :social)))adv))

;go to the room given location
(defn go [dir adv]
  (let [curr-room (get-in adv [:location])
        inv (get-in adv [:inventory])]
   (if-let [dest (get-in the-map [curr-room :dir dir])]
     (if (and (= dest :door)(not (inv "key"))) (do (println "You forgot your key. Your key is in the living room in case you have forgotten.")adv) (assoc-in adv [:location] dest))
     (do (println "You cannot go that direction. ")
         adv) )))

;helper function for puzzles
;randomly generate a number to pull out the puzzle, if the answer is correct we return true, false otherwise
(defn give_puzzle [n puz]
  (let [num (rand-int n)
       ques (get-in puz [num :question])
       ans (get-in puz [num :answer])
       _ (println (str ques))
       answer (read-line)]
  (if (= answer ans)true false)))

;get the puzzle in that room(for general purposes). If the user already has a key don't grant them the key
;(even though it doesn't really mater = =)
(defn puzzles [adv]
    (let [curr-room (get-in adv [:location])
          puz (get-in the-map [curr-room :puzzle])
          inv (get-in adv [:inventory])]
    (if (nil? puz)
        ;no puzlle
        (if (inv "key")(do (println "You already have a key. Why do you want to get another one?")adv)adv)
        ;there is a puzzle
        (if (give_puzzle (count puz) puz)
          (let [cont (get-in the-map [curr-room :content])]
              (if (inv (name :key)) (do (println "You already have the key") adv)
                                    (do (println "You have obtained the key")(update-in adv [:inventory] #(conj % "key")))))
              (if (inv (name :key)) (do (println "You already have the key but you may try again, for fun")adv)adv)
         ))))

(defn drink [adv])

;pick up the object in the given room. if the object already existed in the inventory, we don't update in the inventory
;(even though it doesn't really matter = =)
(defn pick [obj adv]
  (let [curr-room (get-in adv [:location])
        cont (get-in the-map [curr-room :content])
        inv (get-in adv [:inventory])]
   (if (cont (name obj))
    (if (inv (name obj)) (do (println "You already have the item") adv)
        (do (println (str "You have pick up a(n) "(name obj)))(update-in adv [:inventory] #(conj % (name obj)))))
     (do (println "There is no such item. ")
        adv) )))

;remove the item from the inventory, tried to insert into the content of map, but feel like it might be unnecessary
(defn discard [obj adv]
    (let [inv (get-in adv [:inventory])]
    (if (inv (name obj))(do (println (str "You have dropped " (name obj)))(update-in adv [:inventory] #(disj %(name obj))))
                        (do (println "You don't have such item")adv))))

;the function for "inventory" which prints out the items in the inventory
(defn print_inventory [adv]
    (do (println (seq (adv :inventory)))
      adv))

;the function ofr "content" which prints out the content in the adv's location
(defn print_content [adv]
    (let [location (adv :location)]
    (do (println (str (-> the-map location :content)))adv)))

;the function for teleport which require an item (right now called huehue)
(defn tp [location adv]
    (let [inv (adv :inventory)]
    (if (contains? inv "huehue")
      (if (contains? the-map location)
        (do (println (str "You have teleproted to the "(name location))) (assoc-in adv [:location] location))
        (do (println "You can't go that way")adv))
      (do (println "You don't have the thing that allow you to do that yet")adv))))

;the function for sleep which require the adv to be in the bedroom and restore the health to full
(defn sleep [adv]
    (let [curr-room (adv :location)]
    (if (= curr-room :bedroom)
    (do (println "You are at full health")(assoc-in adv [:hp] 100))
    (do (println "You can't sleep here. Go back to the bedroom")adv))))

(defn respond [inst adv]
  (if (contains? inst 1)
  (match [(inst 0)]
          [:pick] (pick (inst 1) adv)
          [:drop] (discard (inst 1) adv)
          [:tp] (tp (inst 1) adv)
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
         [:puzzle] (puzzles adv)
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
  (println (str "Good morning, "(str (-> adv' :name))"! How was your sleep? Welcome to The Exam Day. Get ready for the exam by studying or if you want to have fun, go around the campus. Explore the world!"))
  (loop [the-m the-map
         the-a adv']
    (let [location (the-a :location)
          the-a' (status the-a)
          _      (println (str " What do you wanna do? You can go "  (str(-> the-m location :todo))))
          inst   (read-line) ]
      (recur the-m (respond (to-keywords inst) the-a'))
      ) ) )
  )
