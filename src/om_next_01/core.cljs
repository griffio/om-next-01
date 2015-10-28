(ns ^:figwheel-always om-next-01.core
  (:require
    [cljs.pprint]
    [goog.dom :as gdom]
    [cognitect.transit :as tt]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [clojure.test.check :as ck]
    [clojure.test.check.generators :as ckgs]
    [clojure.test.check.properties :as ckps]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;;===============
;; Greeting is a basic stateless component
;;===============
(defui Greeting
       Object
       (render [this]
               (dom/div nil (get (om/props this) :title))))

(def greeting (om/factory Greeting))

(js/React.render
  (greeting {:title "Welcome to Om Next!"})
  (gdom/getElement "greeting"))

(defonce app-state (atom {:app/description "Description"}))

(defui UIView
       static om/IQuery
       (query [this]
              [:get/description])
       Object
       (render [this]
               (let [{:keys [:get/description]} (om/props this)]
                 (dom/input
                   #js {:className (str "om-" description)
                        :value description
                        :onChange  (fn [_]
                                     (om/transact! this
                                                   `[(edit/description {:description (.. e -target -value)})]))}))))

(defmulti reading om/dispatch)

(defmethod reading :get/description
  [{:keys [state]} key params]
  {:value (:app/description @state key)})

(defmulti mutating om/dispatch)

(defmethod mutating 'edit/description
  [{:keys [state]} _ {:keys [description]}]
  {:value  [:app/description]
   :action (fn []
             (swap! state update-in
                    [:app/description] description))})

(def app-reconciler
  (om/reconciler
    {:state  app-state
     :parser (om/parser {:read reading :mutate mutating})}))

(om/add-root! app-reconciler
              UIView (gdom/getElement "ui"))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )