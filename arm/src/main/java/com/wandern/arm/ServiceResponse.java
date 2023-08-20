package com.wandern.arm;

import org.springframework.http.HttpStatus;

public record ServiceResponse<T>(T body, HttpStatus status) {}
