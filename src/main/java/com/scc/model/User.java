package com.scc.model;

/** A row of the users table. */
public record User(int id, String username, String password, String roles, String status) {
}
