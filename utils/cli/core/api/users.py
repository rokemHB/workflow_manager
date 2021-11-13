import getpass
import json

from core.api.wrapper import Wrapper


def _parse_roles(roles):
    return [r for r in roles.split(" ")]


class UserWrapper(Wrapper):
    def __init__(self, conf):
        super(UserWrapper, self).__init__(conf)

    def list_all(self):
        base_url = self._conf.get_value('base_url')
        response = self._request.get(f"{base_url}/api/{self.API_VERSION}/user")
        return response

    def add(self):
        username = input("Please enter a username: ")
        firstname = input("Please enter a first name: ")
        lastname = input("Please enter a last name: ")
        e_mail = input("Please enter an email address: ")
        password = getpass.getpass("Please enter a password: ")
        roles = input("Please entere a space seperated list of the user roles (e.g. ADMIN PKP ...): ")
        parsed_roles = _parse_roles(roles)

        correct = self._get_boolean_input("All data correct? (y/n): ")

        if not correct:
            try_again = self._get_boolean_input("Try again? (y/n): ")
            if not try_again:
                exit(0)
            else:
                return self.add()
        else:
            base_url = self._conf.get_value('base_url')
            payload = self._get_dict(username, firstname, lastname, e_mail, password, parsed_roles)
            response = self._request.post(f"{base_url}/api/{self.API_VERSION}/user", json=payload)
            return response

    def _get_dict(self, username, firstname, lastname, e_mail, password, roles):
        obj = {
            'username': username,
            'firstName': firstname,
            'lastName': lastname,
            'email': e_mail,
            'password': password,
            'roles': roles,
            'preferredLocale': None,
            'pinned': False
        }
        return obj
