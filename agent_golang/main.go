package main

import (
	"agent_golang/controller"
	"log"
	"net/http"
	"agent_golang/service"
)

func main() {

	http.HandleFunc("/register", controller.RegisterService)
	http.HandleFunc("/getLocalTopology", controller.GetLocalTopology)

	service.StartPeriodicTasks()

	log.Fatal(http.ListenAndServe(":8081", nil))

	
}