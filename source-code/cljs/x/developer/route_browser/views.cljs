
(ns x.developer.route-browser.views
    (:require [fruits.hiccup.api  :as hiccup]
              [fruits.map.api     :as map]
              [fruits.pretty.api  :as pretty]
              [fruits.string.api  :as string]
              [re-frame.extra.api :as r]
              [ugly-elements.api  :as ugly-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-filter-field
  ; @ignore
  []
  [ugly-elements/text-field {:label       "Filter"
                             :placeholder "/my-route"
                             :on-change-f #(r/dispatch  [:set-item! [:ui :developer :route-browser/meta-items :filter-term] %])
                             :value       @(r/subscribe [:get-item  [:ui :developer :route-browser/meta-items :filter-term]])}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-list-item
  ; @ignore
  ;
  ; @param (keyword) route-id
  ; @param (map) route-props
  [route-id route-props]
  [:<> [ugly-elements/horizontal-separator {}]
       [ugly-elements/label {:content    (str route-id)}]
       [ugly-elements/label {:content    (pretty/mixed->string route-props)
                             :text-color :muted}]])

(defn route-list
  ; @ignore
  []
  (let [client-routes @(r/subscribe [:x.router/get-client-routes])
        filter-term   @(r/subscribe [:get-item [:ui :developer :route-browser/meta-items :filter-term]])]
       (letfn [(filter-f [{:keys [route-template]}] (string/starts-with? route-template filter-term))
               (put-f    [[route-id route-props]] [route-list-item route-id route-props])]
              (let [filtered-routes (map/filter-values client-routes filter-f)]
                   (hiccup/put-with [:div {:style {:display :flex :flex-direction :column}}]
                                    filtered-routes put-f)))))

                   ; TODO
                   ; Make the route list alphabetically ordered by route-template values!

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Route browser for client-side routes handled by the 'x.router' module.
  ;
  ; @usage
  ; [route-browser]
  []
  [:<> [ugly-elements/style-scope]
       [route-filter-field]
       [route-list]])
