(ns bench.core
  (:import (java.util Random))
  (:gen-class))

(def _keyRange 100)

(def _threadCount 300)

(def _repeat 100000)

(def mapref1 (atom {}))

#_(def _start (System/currentTimeMillis))

#_(def _agents (seq (repeat _threadCount (agent nil))))

(defn fn1 []
  (let [_random (new Random)
        _key (str "key" (.nextInt _random _keyRange))]
    (let [_oldValue (get @mapref1 _key)]
      (if (nil? _oldValue)
        (swap! mapref1 assoc _key 1)
        (swap! mapref1 assoc _key (+ 1 _oldValue))))))

(defn fn2 []
  (dotimes [_index _repeat]
    (fn1)))

(defn fn3 [_start]
  (println (- (System/currentTimeMillis) _start))
  (println @mapref1))

(defn -main [& args]
  #_(doseq [_agent _agents]
      (send-off _agent fn2))
  #_(doseq [_agent _agents]
      (await _agent))
  #_(fn3)
  (let [_start (System/currentTimeMillis)]
    (dorun (apply pcalls (repeat _threadCount fn2)))
    (fn3 _start))
  (shutdown-agents)
  (System/exit 0))
