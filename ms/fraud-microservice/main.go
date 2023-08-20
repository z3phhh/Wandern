package main

import (
	"log"
	"net/http"
	"fraud-microservice/config"
	"fraud-microservice/database"
	"fraud-microservice/handlers"
)

const configFileName = "config.json"

func main() {
	cfg, err := config.Load(configFileName)
	if err != nil {
		log.Fatalf("Failed to load config: %s", err)
	}

	db, err := database.Connect(cfg)
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	r := http.NewServeMux()
	r.HandleFunc("/checkuser", handlers.CheckUserHandler(db))

	log.Println("Server started on :8090")
	http.ListenAndServe(":8090", r)//change port here if needed
}