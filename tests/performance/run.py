#!/usr/bin/env python

__author__ = "Marius Schäffer"

import logging

import argparse

from priority import create_priority
from user import create
from workstation import create_workstation

BASE_ENDPOINT = "http://localhost:8080/workflow-manager-0.1-SNAPSHOT/"


def _init_argpase():
    parser = argparse.ArgumentParser(
        description="Script that modifies docstring of source files to display authors based on git history.")
    parser.add_argument('-v', '--verbose', dest='verbose', nargs="?", const=True, default=False,
                        help="Enables verbose output.")
    parser.add_argument('-u', '--url', dest='url', nargs="?", help="BASE URL.", default=BASE_ENDPOINT)
    parser.add_argument('-a', '--amount', required=True, dest='amount', nargs="?", help="Amount of test data to be created.")
    return parser.parse_args()


def create_user(i, url):
    username = f"user{i}"
    uname = create(username, url)
    if uname is not None:
        print(f"Created user {username}.")
    return uname


def test(args):
    # map username -> password
    for i in range(int(args.amount)):
        create_user(i, args.url)
        create_priority(i * 10, args.url)
        create_workstation(i, args.url)

def main():
    args = _init_argpase()
    logging.basicConfig()
    fmt = "%(levelname)s - %(message)s"
    if args.verbose:
        logging.basicConfig(format=fmt, level=logging.DEBUG)
    else:
        logging.basicConfig(format=fmt, level=logging.INFO)

    test(args)

if __name__ == "__main__":
    main()
