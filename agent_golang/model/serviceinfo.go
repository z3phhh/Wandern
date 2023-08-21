package model

type ServiceInfo struct {
	System            string `json:"system"`
	DeploymentID      string `json:"deploymentId"`
	Address           string `json:"address"`
	Port              int    `json:"port"`
	HealthEndpoint    string `json:"healthEndpoint"`
	IsBalancingEnabled bool  `json:"isBalancingEnabled"`
}