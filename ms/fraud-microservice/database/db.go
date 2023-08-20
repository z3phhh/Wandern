package database

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"fraud-microservice/config"
)

func Connect(cfg *config.Config) (*sql.DB, error) {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", cfg.Host, cfg.Port, cfg.User, cfg.Password, cfg.Dbname)
	return sql.Open("postgres", psqlInfo)
}