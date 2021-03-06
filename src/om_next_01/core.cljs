(ns ^:figwheel-always om-next-01.core
  (:require
    [cljs.pprint]
    [goog.dom :as gdom]
    [cognitect.transit :as tt]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]))

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
       static om/IQuery                                     ;; IQuery must return a vector (or map of vectors when representing a union)
       (query [this]
              [:get/description])                           ;; one or more keywords can be fetched by the query reader (parser)
       Object                                               ;; methods declared below Object are associated with the JS Object
       (render [this]
               (let [{:keys [:get/description]} (om/props this)]
                 (dom/div nil
                          (dom/h3 nil description)
                          (dom/input
                            #js {:className "om-description"
                                 :value     description
                                 :onChange  (fn [e]
                                              (let [value (.. e -target -value)]
                                                (om/transact! this
                                                              `[(edit/description {:description ~value})])))})
                          (dom/label nil (str "There are " (.-length description) " characters."))))))

(defmulti reading om/dispatch)

(defmethod reading :get/description
  [{:keys [state]} key params]                              ;; environment {:state :parser} key params
  {:value (get @state :app/description)})

(defmulti mutating om/dispatch)

(defmethod mutating 'edit/description
  [{:keys [state]} _ {:keys [description]}]                 ;; :description received
  ;; :value is an optional query expression to help document stale keys that should be re-read after mutation for the client to action
  {:value [:get/description]}
  {:action (fn [] (swap! state assoc :app/description description))})

(defonce app-reconciler
         (om/reconciler
           {:state  app-state
            :parser (om/parser {:read reading :mutate mutating})}))

(om/add-root! app-reconciler
              UIView (gdom/getElement "ui"))

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
