package com.wandern.manager;

import org.springframework.http.HttpStatus;

public record ServiceResponse<T>(T body, HttpStatus status) {}
