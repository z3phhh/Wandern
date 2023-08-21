package main

import (
	"log"
	"net/http"
	"fraud-microservice/config"
	"fraud-microservice/database"
	"fraud-microservice/handlers"
	"encoding/json"
	"fmt"
	"bytes"
)

const configFileName = "config.json"

type ServiceInfo struct {
	System             string `json:"system"`
	DeploymentID       string `json:"deploymentId"`
	Address            string `json:"address"`
	Port               int    `json:"port"`
	HealthEndpoint     string `json:"healthEndpoint"`
	IsBalancingEnabled bool   `json:"isBalancingEnabled"`
}

func RegisterWithAgent(agentEndpoint string, serviceInfo ServiceInfo) error {
	body, err := json.Marshal(serviceInfo)
	if err != nil {
		return err
	}

	resp, err := http.Post(agentEndpoint, "application/json", bytes.NewBuffer(body))
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("Failed to register with agent, received status code: %d", resp.StatusCode)
	}
	return nil
}

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
    r.HandleFunc("/health", handlers.HealthCheckHandler)

	serviceInfo := ServiceInfo{
        System:             "fraud-microservice",
        DeploymentID:       "v2.0.0",
        Address:            "localhost",
        Port:               8090,
        HealthEndpoint:     "/health",
        IsBalancingEnabled: true,
    }

    err = RegisterWithAgent(cfg.AgentEndpoint, serviceInfo)
    if err != nil {
        log.Fatalf("Failed to register with agent: %v", err)
    }

	log.Println("Server started on :8090")
	http.ListenAndServe(":8090", r)//change port here if needed
}