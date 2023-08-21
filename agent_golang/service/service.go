package service

import (
	"agent_golang/config"
	"agent_golang/model"
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"time"
	"fmt"

	"io/ioutil"//remove this later
)


var registeredServices = make(map[string]model.ServiceInfo)
var localTopology = make(map[string]model.ServiceInfo)
var warnedServices = make(map[string]bool)

func RegisterServiceHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is supported", http.StatusBadRequest)
		return
	}

	// Decode the incoming request body into a serviceInfo object.
	var serviceInfo model.ServiceInfo
	if err := json.NewDecoder(r.Body).Decode(&serviceInfo); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Formulate a unique key for each service instance.
	serviceKey := serviceInfo.System + "-" + serviceInfo.DeploymentID
	registeredServices[serviceKey] = serviceInfo

	log.Printf("Registered instance %+v", serviceInfo)

	sendServiceInfoToMaster(serviceInfo)

	w.Write([]byte("Service registered"))
}


func sendServiceInfoToMaster(serviceInfo model.ServiceInfo) {
	data, err := json.Marshal(serviceInfo)
	if err != nil {
		log.Println("Error marshalling service info:", err)
		return
	}

	// POST request to master service to register this service.
	resp, err := http.Post(config.MasterServiceURL+"/register", "application/json", bytes.NewBuffer(data))
	if err != nil {
		log.Println("Error sending service info to master:", err)
		return
	}

	if resp.StatusCode >= 200 && resp.StatusCode < 300 {
		log.Println("Service registered successfully in master")
	} else {
		log.Printf("Service registration in master failed, status code: %d\n", resp.StatusCode)
	}
}

func checkServicesHealth() {
	client := &http.Client{Timeout: 5 * time.Second}
	healthStatusUpdates := make(map[string]bool)

	for _, serviceInfo := range localTopology {
		serviceKey := serviceInfo.System + "-" + serviceInfo.DeploymentID

		// If balancing is not enabled for a service, skip its health check.
		//serviceInfo.IsBalancingEnabled = true
		if !serviceInfo.IsBalancingEnabled {
			if _, present := warnedServices[serviceKey]; !present {
				log.Printf("Skipping health check for service %s as it has been manually removed from balancing", serviceKey)
				warnedServices[serviceKey] = true
			}
			continue
		}

		delete(warnedServices, serviceKey)

		// Construct the health check URL for the service.
		url := "http://" + serviceInfo.Address + ":" + fmt.Sprint(serviceInfo.Port) + serviceInfo.HealthEndpoint
		resp, err := client.Get(url)



		if err != nil {
			log.Printf("Error checking health of service %s: %s", serviceKey, err.Error())
			healthStatusUpdates[serviceKey] = false
			continue
		}

		if resp.StatusCode >= 200 && resp.StatusCode < 300 {
			log.Printf("Service %s is healthy", serviceKey)
			healthStatusUpdates[serviceKey] = true
		} else {
			log.Printf("Service %s is not healthy, status code: %d", serviceKey, resp.StatusCode)
			healthStatusUpdates[serviceKey] = false
		}

		
	}
	
	sendHealthStatusToMaster(healthStatusUpdates)
}


func GetLocalTopologyHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Only GET method is supported", http.StatusBadRequest)
		return
	}

	data, err := json.Marshal(localTopology)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write(data)
}

func updateLocalTopology() {
	client := &http.Client{Timeout: 10 * time.Second}

	// GET request to fetch the latest topology from master service.
	resp, err := client.Get(config.MasterServiceURL + "/getTopology")
	if err != nil {
		log.Println("Error fetching topology:", err)
		return
	}

	defer resp.Body.Close()

	// Read the entire response body into a byte slice
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println("Error reading response body:", err)
		return
	}

	// Convert the byte slice to a string and log it
	//log.Println("Raw Response Body:", string(bodyBytes))


	var responseBody map[string]model.ServiceInfo
	if err = json.Unmarshal(bodyBytes, &responseBody); err != nil {
		log.Println("Error decoding response:", err)
		return
	}

	// Compare with existing topology and log changes in balancing state
	for key, updatedService := range responseBody {
		currentService, exists := localTopology[key]
		if exists && currentService.IsBalancingEnabled != updatedService.IsBalancingEnabled {
			log.Printf("Balancing state for service %s changed to %v", key, updatedService.IsBalancingEnabled)
		}
	}


	// Update the local topology with the fetched data.
	localTopology = responseBody

	//log.Printf("Updated Local Topology: %+v\n", localTopology)

	log.Println("Local topology updated successfully")
}

func StartPeriodicTasks() {
	go func() {
		for {
			checkServicesHealth()
			time.Sleep(3 * time.Second)
		}
	}()

	go func() {
		for {
			updateLocalTopology()
			time.Sleep(1 * time.Second)
		}
	}()
}


func sendHealthStatusToMaster(healthStatusUpdates map[string]bool) {
	client := &http.Client{Timeout: 10 * time.Second}

	data, err := json.Marshal(healthStatusUpdates)
	if err != nil {
		log.Println("Error marshalling health status:", err)
		return
	}

	// POST request to master service to update health statuses.
	resp, err := client.Post(config.MasterServiceURL+"/updateHealthStatus", "application/json", bytes.NewBuffer(data))
	if err != nil {
		log.Println("Error sending health status to master:", err)
		return
	}

	defer resp.Body.Close()

	if resp.StatusCode < 200 || resp.StatusCode >= 300 {
		log.Printf("Error updating health status on master, received status code: %d", resp.StatusCode)
	}
}