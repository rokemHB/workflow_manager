#!/usr/bin/env python
import logging
import random

import names

__author__ = "Marius Schäffer"

import requests
from requests.auth import HTTPBasicAuth


def _create_workstation_payload(i):
    obj = {
        "name": f"Workstation {i}",
        "broken": random.choice([True, False]),
        "active": random.choice([True, False]),
        "users": [],
        "position": f"Workstation {i}"
    }
    return obj


def create_workstation(i, url):
    payload = _create_workstation_payload(i)
    response = requests.post(f"{url}api/v1/workstation", json=payload, auth=HTTPBasicAuth("admin", "kcb"))
    if response.status_code == 200:
        print(f"Created priority for workstation {i}.")
    elif "message" in response.content:
        print(f"Couldn't create workstation with number {i}")
