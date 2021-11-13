import requests


class AuthcRequest(object):
    def __init__(self, username, password):
        self._username = username
        self._password = password

    def get(self, url):
        return requests.get(self._update_url(url), auth=(self._username, self._password))

    def post(self, url, json=None):
        return requests.post(self._update_url(url), auth=(self._username, self._password), json=json)

    def put(self, url, json=None):
        return requests.put(self._update_url(url), auth=(self._username, self._password), json=json)

    @staticmethod
    def _update_url(url):
        return url.replace("//", "/").replace("http:/", "http://")
