package com.scc.model;

/** Data model for a row in the users table. */
public record User(int id, String username, String password, String roles, String status) {
}
