(ns sorting.core
  (:gen-class))

(defn cas
  "Compare elements `i` and `j` of array `arr`, swapping them if they are out of order.
  Returns the new array." 
  [arr [i j]]
  (if (< (arr j) (arr i))
    (assoc arr i (arr j)
           j (arr i))
    arr))

(defn multi-cas
  "Takes an array a vector of swapping pairs and perform all the swaps needed."
  [arr swaps]
  (reduce cas arr (partition 2 swaps)))

(def n6 [[0 1 2 3 4 5] [0 2 3 5] [1 4] [0 1 2 3 4 5] [1 2 3 4] [2 3]])

(defn n-sort
  [arr sv]
  (reduce multi-cas arr sv)) 

(defn add-bit
    [v]
    [(conj v 0) (conj v 1)])

(defn zero-one
    [n]
    (if (= n 0)
          [[]]
          (mapcat add-bit (zero-one (- n 1)))))


(defn sorted
   [v]
    (apply <= v))


(defn nele
    [tmp]
    (+ 1 (apply max (map #(apply max %) tmp))))


(defn verify
    [tmp]
    (every? sorted (map n-sort (zero-one (nele tmp)) (repeat tmp))))

(defn bubble-sort [xs]
 (letfn [(bubble [acc x]
           (if (seq acc)
             (if (> 0 (compare x (peek acc)))
               (conj (pop acc) x (peek acc))
               (conj acc x))
             [x]))]
   (loop [xs xs]
     (let [bubbled (reduce bubble [] xs)]
       (if (= xs bubbled)
         bubbled
         (recur bubbled))))))
     
