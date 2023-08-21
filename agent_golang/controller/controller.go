package controller

import (
	"net/http"
	"agent_golang/service"
)

func RegisterService(w http.ResponseWriter, r *http.Request) {
	service.RegisterServiceHandler(w, r)
}

func GetLocalTopology(w http.ResponseWriter, r *http.Request) {
	service.GetLocalTopologyHandler(w, r)
}