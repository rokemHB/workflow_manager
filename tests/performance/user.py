#!/usr/bin/env python

__author__ = "Marius Schäffer"

import logging
import random

import names

import requests
from requests.auth import HTTPBasicAuth

AVAILABLE_ROLES = ["ADMIN", "PKP", "LOGISTIKER", "TRANSPORT", "TECHNOLOGE"]

PASSWORD = "abcABC123"

GEILES_MAPPING = {
    "0": "a",
    "1": "b",
    "2": "c",
    "3": "d",
    "4": "e",
    "5": "f",
    "6": "g",
    "7": "h",
    "8": "i",
    "9": "j"
}


def _select_role():
    return random.choice(AVAILABLE_ROLES)


def _create_user_payload(username, role):
    obj = {
        "username": f"{username}",
        "firstName": f"{names.get_first_name()}",
        "lastName": f"{names.get_last_name()}",
        "password": PASSWORD,
        "email": f"{username}@example.com",
        "roles": [
            f"{role}"
        ]
    }
    return obj


def create(username, url):
    role = _select_role()
    payload = _create_user_payload(username, role)
    response = requests.post(f"{url}api/v1/user", json=payload, auth=HTTPBasicAuth("admin", "kcb"))
    if not response.status_code == 200:
        logging.error(f"Couldn't create user {username}")
        return None
    return username, role
