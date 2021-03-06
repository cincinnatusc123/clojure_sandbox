(ns highlife.prob)

;;  if dist[i-1]<=random<=dist[i], return i
(defn ix-from-dist [dist random]
  (loop [loop-dist dist ix 0]
    (if (<= random (first loop-dist)) ix (recur (rest loop-dist) (inc ix)))))

;; turns scores: [s[0] s[1] ... s[n]] into [x[1] x[2] ... x[n+1]] s.t 0<=x[i]<=1 and x[i-1]<=x[i],
;; With x[0] (not part of the list) being 0, we have x[i] - x[i-1] = P( s[i-1] / SUM(scores) )
;; A function that takes a random number and returns an index (0-n) according to this distribution
;; is returned (ix-from-dist)
(defn make-dist [scores]
  (let [total (apply + scores)]
    (loop [prev-num 0
	   dist []
	   loop-scores scores]
      (if (empty? loop-scores)
	(partial ix-from-dist dist)
	(let [new-num (+ prev-num (/ (first loop-scores) total))]
	  (recur new-num (conj dist new-num) (rest loop-scores)))))))