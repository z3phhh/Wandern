package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"fraud-microservice/models"
)

func CheckUserHandler(db *sql.DB) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var req models.UserRequest
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			http.Error(w, "Bad Request", http.StatusBadRequest)
			return
		}

		//Sybil Logic
		//req.id ?= db.id -> fraud
		var id int
		err := db.QueryRow("SELECT id FROM users WHERE id = $1", req.ID).Scan(&id)
		var response models.FraudResponse
		if err == sql.ErrNoRows {
			response = models.FraudResponse{Result: "not fraud"}
		} else if err != nil {
			http.Error(w, "Failed to check database", http.StatusInternalServerError)
			return
		} else {
			response = models.FraudResponse{Result: "fraud"}
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}
}

func HealthCheckHandler(w http.ResponseWriter, r *http.Request) {
    w.WriteHeader(http.StatusOK)
    w.Write([]byte("Healthy"))
}