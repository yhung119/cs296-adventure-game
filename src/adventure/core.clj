(ns adventure.core
  (:require [clojure.string :as str]
            [clojure.core.match :refer [match]]
               )
  (:gen-class))

(def the-map
  {
   :bedroom {:desc "There is a bed, and a blanket that is pretty effective in putting you to sleep. Next to the pillow, there is a cellphone that you used as an alarm clock."
           :title "the bedroom "
           :dir {:south :hallway1
                  }
           :content #{"cellphone" "blanket" "pillow"}
           }

   :hallway1 {:desc "It's wide, but not too wide"
              :title "hallway in front of your room"
              :dir {:north :bedroom
                    :south :hallway2
                    :east :bathroom
                    :west :room2}
              :content #{}
            }

   :room2 {:desc "It's your roommate's room, better not touch anything, however, you have noticed the cheatsheat you lended him yesterday laying on his desk"
           :title "Roommate's room "
           :dir {:east :hallway1}
           :content #{"cheatsheet"}
         }

   :bathroom {:desc "There isn't much to do here"
              :title "the bathroom "
              :dir {:west :hallway1}
              :content #{}
            }
   :living_room {:desc "There is a TV, laptop, a calculator, and your key. But your key is locked by an evil puzzle that your roommate made last night. He thought you couldn't solve it, but you should reach for your potential and really, he didn't know you that well. The Master of puzzle."
              :title "the living room "
              :todo "east to the hallway"
              :dir {:east :hallway2
                  }
              :puzzle [
                    {:question "Fill in the blank. The following is the lyrcis from a song by Taylor Swift: I don't know about you. But I feel like __."
                     :answer "22"
                     :hint ""}
                    {:question "Which place are you if you race over the second place?(leave answer in a number)"
                     :answer "2"
                     :hint ""}
                    {:question "How many stones did it take to complete the pyramids?"
                     :answer "1"
                     :hint ""}
                    {:question "1 11 21 1211 111221, what is the next number to this pattern?"
                     :answer "312211"
                     :hint ""}
                    {:question "What is the remainder when 2 ^ 212 divdes 11 ?"
                     :answer "4"
                     :hint ""}
              ]
              :content #{"calculator"}
            }

   :hallway2 {:desc "It is very short, but not too short. "
              :title "the hallway next to the door"
              :todo "north: bedroom, south: door, east: kitchen, west: living room"
              :dir {:north :hallway1
                    :south :door
                    :east :kitchen
                    :west :living_room }
              :content #{}}

   :kitchen {:desc "There is a meal on the table. You should do something with it."
              :title "the kitchen "
              :todo "west to the hallway"
              :dir {:west :hallway2
               }
              :content #{"meal"}}

   :door {:desc "The outside is like a jungle, and you are just a chicken in the wild. Once you go outside, you can still come back, but you... might not be you anymore."
          :title "the door "
          :todo "north to the hallway"
          :dir {:north :hallway2
                :south :road
            }
          :content #{}}

    :road {:desc "It's a road connecting your house to the Siebal building. There is a lot of stones and pokeballs on the way"
           :title "the road"
           :dir { :north :door
                  :south :siebal
             }
           :content #{"pokeball", "stone"}}

    :eceb {:desc "The building is new, and there is a new coffee store called byte inside"
           :title "ECEB "
           :dir {:north :siebal
                 :in :byte}
           :content #{}
         }

    :byte {:desc "In case you didn't know, they sell coffee here. get/pick one if you want"
           :title "ECE Byte Coffee Shop"
           :dir {:out :eceb}
           :content #{"coffee"}}

    :siebal {:desc "You are here! The exam is ready for you at the basement ... if you are ready. However, roam around the building wherever you want. The Acm office is here too"
            :title "Siebal "
            :dir {:north :road
                  :down :siebal0
                  :south :eceb
                  :east :s_quad
                }
            :content #{"cookies"}
    }

    :siebal0 {:desc "I have been expecting you. Fight me, if you are ready.. which you never will be hahahaha!"
              :title "Siebal Basement"
              :dir {:up :siebal}
              :content #{}}

    :s_quad {:desc "how much you wish if you could just lay on the grass and take a nap"
             :title "the South Quad"
             :dir {
               :east :grainger
               :west :siebal
               }
             :content #{}}
    :grainger {:desc "Everyone is preparing for the exams, so there isn't much seats left here, maybe try to find one?"
               :title "the Grainger Library"
               :dir {
                  :west :s_quad
                  :north :green
                  :east :bus
                  :find :seat
                  :in :seat
               }
               :content #{}
             }
    :seat {:desc "There is a girl sitting in front of you, a really cute girl. You should try to talk to her, not like you will lose anything. If not you may leave after you study"
           :title "A seat in Grainger"
           :dir {
                :leave :grainger
                :out :grainger
             }
            :content #{}
           }
    :green {:desc "it's green street, anything else other than bars here?"
            :title "Green Street"
            :dir {:south :grainger
                  :west :bar
                  }
                :content #{}}

    :bar {:desc "What do you think you can do here? Get some alochol? Well good guess"
          :title "Bar"
          :todo "drink"
          :dir { :east :green}
          :content #{"alcohol"}
          }

    :bus {:desc "Welcome to the UIUC bus station that can take you to Chicago in just a second but does not exist in reality..."
          :title "the Bus Station"
          :dir {
             :west :grainger
             :take :chicago
            }
          :content #{}
          }
    :chicago {:desc "Welcome to Chicago, there is a bus that can take you back to UIUC in just a sec but doesn't exist in reality...\n However, this place seems much nicer than campus"
              :title "Chicago"
              :dir {
                :take :bus
                :south :chinatown
                :north :willestower
                }
            :content #{}
              }
    :chinatown {:desc "You can buy a lot of foods here, and there is a Ktv store inside if you want to sing"
                :title "ChinaTown"
                :dir {
                    :north :chicago
                    :in :ktv
                  }
                :content #{}
                }
    :ktv {:desc "Looks like you can sing here, there is a ktv machine and a mic that can be picked up"
          :title "Ktv"
          :dir {
               :out :chinatown
            }
          :content #{"mic"}}

    :willestower {:desc "You are at the top of WillesTower. You looked through the telescope and the view is astonishing, it gives you a feeling that the exam has been blown away by the fresh air up here. Suddenly, a crazy thought ran through your head.. What if I jump down the tower?"
                  :title "WillesTower"
                  :dir { :south :chicago}
                  :content #{"telescope"}}

       })
(def exam
  {:hp 200
   :att 20
   :defeat 0})
(def help "Useful Command:\n \n Directions:\n\n south/north/west/east/up/down/in/out/find/leave/take\n\n Actions:\n\n pick(get)/drop/puzzle/sleep/eat/drink/jump/talk/fight and some hidden actions\n\n Status:\n\n status(display your status)/inventory(display your inventory items)\n\n Fighting:\n\n You need to 'fight' the monster before can do anything below\n attack(damange is equal to your skill points)\n use(use the items in your inventory...some might not be useful)\n You can also use command like inventory or status\n\n Abbreviation:\n\n 'i' for inventory\n 's' for status \n 'att' for attack\n\n Note 1:\n\n The hidden action teleport/tp takes a place for destination but some might not work because we aren't allow to have space when we name our locations\n\n Note 2:\n\n If you lost to the exam, you will lose all of your items\n")
(def firstTime "Welcome to the game, if this is your first time you might be confused about what to pick up or what to drop. The object in the descriptions of the room can sometimes be picked up, but some can't. Some verbs take one argument, for example, pick calculator. I think you are all set now. To get all the commands, type help.")
(def adventurer
  {:location :bedroom
   :name ""
   :hp 100
   :skill 10
   :social 10
   :inventory #{}
   :before #{}
   :fight 0})

;function for talk
(defn talk [adv]
  (let [curr-room (adv :location)]
  (if (= curr-room :seat)
      (if (>= (adv :social) 50)(do (println "The girl seemed to be interested in you... after a few weeks of talking you guys got together... You got a girlfriend")(update-in adv [:inventory] #(conj % "girlfriend")))
                               (do (println "The girl doesn't seem interested in you...")adv)
                               )
    (do (println "No one to talk to here...")adv))))

;the function for sleep which require the adv to be in the bedroom and restore the health to full
(defn sleep [adv]
 (let [curr-room (adv :location)]
   (if (= curr-room :bedroom)
     (do (println "You are at full health")(assoc-in adv [:hp] 100))
     (do (println "You can't sleep here. Go back to the bedroom")adv))))

(defn status [adv mon]
  (if (< (adv :hp) 1) (do (println "You have moved back to the bedroom")(sleep (assoc-in (assoc-in adv [:fight] 0) [:location] :bedroom)))
  (let [fight (adv :fight)
        de (mon :defeat)]
      (if (and (= fight 1)(= de 0)) adv
  (let [location (adv :location)]
    (print (str "You are at " (-> the-map location :title)))
    (when-not ((adv :before) location)
      (print (-> the-map location :desc) ) )
    (update-in adv [:before] #(conj % location)))))))

;the function for "status" which prints out the adv's current status
(defn adv_status [adv]
  (do (println (str "Name: "(-> adv :name)". HP: "(-> adv :hp) ". Skill Point: " (-> adv :skill) ". Social Point: " (-> adv :social)))adv))

;fight : get into fighting mode
(defn fighting [adv mon]
  (let [curr-room (get-in adv [:location])
        de (mon :defeat)]
      (if (not (= curr-room :siebal0)) (do (println "There is no exam to fight here")adv)
          (if (= de 0)(do (println "Get ready! You are fighting the exam")(assoc-in adv [:fight] 1))
                      (do (println "You have already defeated the exam")adv)))))

;function for jump
(defn jump [adv]
  (let [curr-room (adv :location)]
   (if (not (= curr-room :willestower)) (do (println "You can't jump here")adv)
      (do (println "Ahhhhhhhh! You jump down the tower...\n The next second when you opened your eyes again, you were on your bed. Was it just a dream? As you were questioning, you reached for your pocket and found a m&m. Hmm, did this m&m saved me?\n You obtained a magic m&m and a new skill... teleport/tp")
      (update-in (assoc-in adv [:location] :bedroom) [:inventory] #(conj % "magic m&m"))))))

;go to the room given location
(defn go [dir adv]
  (let [curr-room (get-in adv [:location])
        inv (get-in adv [:inventory])]
   (if-let [dest (get-in the-map [curr-room :dir dir])]
     (if (and (= dest :door)(not (inv "key"))) (do (println "You tried to go through the door but you forgot your key. Your key is in the living room in case you have forgotten.")adv) (assoc-in adv [:location] dest))
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
              (if (inv "key") (do (println "You already have the key") adv)
                                    (do (println "You have obtained the key, try not to drop it")(update-in adv [:inventory] #(conj % "key")))))
              (if (inv "key") (do (println "You already have the key but you may try again, for fun")adv)
                              (do (println "Opps, you got it wrong this time")adv))
         ))))

;the function for drinking, which increase the social point of adv
(defn drink [adv obj]
  (let [inv (get-in adv [:inventory])]
    (if (inv (name obj))
      (case (name obj)
        "alcohol"  (do (println "That was a nice "(str (name obj))" maybe I should get another one.")(update-in (update-in adv [:social] + 5) [:inventory] #(disj % "alcohol")))
        "coffee"   (do (println "That was a nice "(str (name obj))" maybe I should get another one.")(update-in (update-in adv [:skill] + 5) [:inventory] #(disj % "coffee")))
        (do (println "You can't drink that") adv))
     (do (println "You don't have "(str (name obj))" tho")adv))))

;cook
; (defn cook [adv]
;   (let [curr-room (get-in adv [:location])]
;     (if (= curr-room :kitchen) (do (println "You have obtained a fully cooked egg")(update-in adv [:inventory] #(conj % "fully cooked egg")))
;                                (do (println "Hmm shouldn't you be cooking in the kitchen")adv))))

;eat
(defn eat [adv]
    (let [inv (get-in adv [:inventory])]
        (if (inv "meal") (do (println "You have eaten the meal")(update-in (update-in adv [:inventory] #(disj % "meal")) [:skill] + 10))
                                     (do (println "You don't have anyting to eat")adv))))

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
    (if (contains? inv "magic m&m")
      (if (contains? the-map location)
        (do (println (str "You have teleproted to the "(name location))) (assoc-in adv [:location] location))
        (do (println "You can't go that way")adv))
      (do (println "You don't have the thing that allow you to do that yet")adv))))

;monster attacked!
(defn exam_respond [adv mon num obj]
    (let [curr-room (get-in adv [:location])
          att (rand-int (+ (mon :att) 1))
          hp (get-in adv [:hp])
          mon_hp (get-in mon [:hp])]
          (if (and (< mon_hp 50)(= obj "pokeball")) (assoc-in (update-in (update-in adv [:inventory] #(conj % "exam")) [:inventory] #(disj % obj)) [:fight] 0)
          (if (< (- mon_hp num) 1) (do (println "You have defeated the exam")(assoc-in (update-in adv [:inventory] #(disj % obj)) [:fight] 0))
          (if (< (- hp att) 1) (do (println "You have lost all of your health")(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
          (cond
            (= att 0) (do (println "The exam missed, your health is now"(str (- hp att)))(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
            (<= att 5) (do (println "The exam used the Big Three, your health is now"(str (- hp att)))(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
            (<= att 10) (do (println "The exam used KdTree, your health is now"(str (- hp att)))(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
            (<= att 15) (do (println "The exam used Prim's Algorithm, your health is now"(str (- hp att)))(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
            :else (do (println "The exam used some CS241 materials, your health is now"(str (- hp att)))(update-in (update-in adv [:inventory] #(disj % obj)) [:hp] - att))
          ))))))

;the function for "status" which prints out the adv's current status
(defn adv_status_mon [adv mon]
  (do (println (str "Name: "(-> adv :name)". HP: "(-> adv :hp) ". Skill Point: " (-> adv :skill) ". Social Point: " (-> adv :social)))mon))

;attack_mon
(defn attack [adv mon]
  (let [skill (adv :skill)]
  (println "You attacked the exam, the exam lost" (str (adv :skill))"hp")
  (update-in mon [:hp] - skill)))

;print inv in fighting stage
(defn print_inventory_mon [adv mon]
  (do (println (seq (adv :inventory)))
    mon))

;use objects to att
(defn using [adv mon obj]
  (let [inv (adv :inventory)
        mon_hp (mon :hp)]
   (if (inv (name obj))
    (case (name obj)
      "calculator" (do (println "The exam lost 20 hp")(update-in mon [:hp] - 20))
      "pokeball" (if (>= mon_hp 50)(do (println "You tried to catch the exam... but you failed")mon) (do (println "1...2...3 You have caught the exam")(assoc-in mon [:defeat] 1)))
      "stone" (do (println "The exam lost 50 hp")(update-in mon [:hp] - 50))
      "coffee" (do (println "You have gain 10 attack damage")mon)
      "mic" (do (println "Your voice was powerful enought that exam lost 30 hp")(update-in mon [:hp] - 30))
      "cheatsheet" (do (println "It was super effective, the exam lost 70 hp")(update-in mon [:hp] - 70))
      "telescop" (do (println "You looked through the exam, the exam lost 15 hp")(update-in mon [:hp] - 15))
      "blanket" (do (println "You have recovered 30 health")mon)
      "pillow" (do (println "You have recovered 10 health")mon)
      (do (println (str (name obj))"was not very effective")mon))
    (do (println "Hmm you don't have" (str (name obj))) mon)
      ) ) )

(defn respond_monster [inst adv mon]
  (let [state (adv :fight)
        mon_hp (mon :hp)]
  (if (= state 0)mon
  (if (< mon_hp 1) mon
  (if (contains? inst 1)
    (match [(inst 0)]
      [:use] (using adv mon (inst 1))
      [_] (do
          (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
          mon)
    )
    (match inst
      [:i] (print_inventory_mon adv mon)
      [:inventory] (print_inventory_mon adv mon)
      [:s] (adv_status_mon adv mon)
      [:status] (adv_status_mon adv mon)
      [:attack] (attack adv mon)
      [:att] (attack adv mon)
      _ (do
          (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
          mon) )
    )))))

(defn respond [inst adv mon]
  (let [state (adv :fight)
        inv (adv :inventory)
      ]
  (if (= state 1 )
    (let [mon_hp (mon :hp)
          skill (adv :skill)]
    (cond
    (= (inst 0) :att) (exam_respond adv mon skill "")
    (= (inst 0) :attack) (exam_respond adv mon skill "")
    (= (inst 0) :use)   (if (inv (name (inst 1)))
                            (case (name (inst 1))
                             "calculator" (exam_respond adv mon 20 "calculator")
                             "stone" (exam_respond adv mon 50 "stone")
                             "mic" (exam_respond adv mon 30 "mic")
                             "cheatsheet" (exam_respond adv mon 70 "cheatsheet")
                             "pokeball" (exam_respond adv mon 0 "pokeball")
                             "coffee" (exam_respond (update-in adv [:skill] + 10) mon 0 "coffee")
                             "telescope" (exam_respond adv mon 0 "telescope")
                             "blanket" (exam_respond (update-in adv [:hp] + 30)mon 0 "blanket")
                             "pillow" (exam_respond (update-in adv [:hp] + 10)mon 0 "pillow")
                             adv)
                             adv)
    :else  adv) )
  (if (contains? inst 1)
  (match [(inst 0)]
          [:pick] (pick (inst 1) adv)
          [:get] (pick (inst 1) adv)
          [:drop] (discard (inst 1) adv)
          [:tp] (tp (inst 1) adv)
          [:drink] (drink (inst 1) adv)
          [:teleport] (tp (inst 1) adv)
          [_](do
              (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
              adv)
          )
  (match inst
         [:north] (go :north adv)
         [:south] (go :south adv)
         [:west] (go :west adv)
         [:east] (go :east adv)
         [:up] (go :up adv)
         [:down] (go :down adv)
         [:in] (go :in adv)
         [:out] (go :out adv)
         [:find] (go :find adv)
         [:leave] (go :leave adv)
         [:take] (go :take adv)
         [:i] (print_inventory adv)
         [:inventory] (print_inventory adv)
         [:content] (print_content adv)
         [:s] (adv_status adv)
         [:status] (adv_status adv)
         [:sleep] (sleep adv)
         [:puzzle] (puzzles adv)
         [:help] (do (println help)adv)
         [:first] (do (println firstTime) adv)
         [:fight] (fighting adv mon)
         [:jump] (jump adv)
         [:eat] (eat adv)
         [:talk] (talk adv)


         _ (do
             (println (str "I'm sorry "(-> adv :name)". I cannot allow you to do that."))
             adv) )
    ) ) ) )

;monster's stat
(defn mon_stat [adv mon]
  (let [fight (adv :fight)
       monster_hp (get-in mon [:hp])]
    (if (< monster_hp 1) (assoc-in mon [:defeat] 1)
    (if (= fight 0) mon
  (let [curr-room (get-in adv [:location])
        ]
        (do (println "The monster's health is now"(str monster_hp) "\nWhat are you going to do next?")
        mon)
        )))))

(defn to-keywords [st]
   (mapv keyword (str/split st #" +")))

(defn -main
  [& args]
  (println "What is your name?")
  (let [n (read-line)
        adv-n (adventurer :name)
        adv' (assoc-in adventurer [:name] n)]
  (println (str "Good morning, "(str (-> adv' :name))"! How was your sleep? Welcome to The Exam Day. Get ready for the exam by studying or if you want to have fun, go around the campus. Explore the world! If this is your first time playing, type first for informations and help for commands."))
  (loop [the-m the-map
         the-mon exam
         the-a adv'
         ]
    (let [location (the-a :location)
          the-a' (status the-a the-mon)
          the-mon' (mon_stat the-a' the-mon)
          _      (if (= (the-a :fight) 0) (println " What do you wanna do?") (print ""))
          inst   (read-line) ]
      (recur the-m (respond_monster (to-keywords inst) the-a' the-mon') (respond (to-keywords inst) the-a' the-mon'))
      ) ) )
  )
