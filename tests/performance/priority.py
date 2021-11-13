#!/usr/bin/env python
import logging

import names

__author__ = "Marius Schäffer"

import requests
from requests.auth import HTTPBasicAuth


def _create_priority_payload(value):
    obj = {
        "name": f"{names.get_first_name()}",
        "value": f"{value}"
    }
    return obj


def create_priority(value, url):
    payload = _create_priority_payload(value)
    response = requests.post(f"{url}api/v1/priority", json=payload, auth=HTTPBasicAuth("admin", "kcb"))
    if response.status_code == 200:
        print(f"Created priority for value {value}.")
    elif "message" in response.content:
        print(f"Couldn't create priority for value {value}")