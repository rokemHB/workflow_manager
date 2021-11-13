from core.api.request import AuthcRequest


class Wrapper(object):
    API_VERSION = "v1"

    def __init__(self, conf):
        self._conf = conf
        username = conf.get_value("username", section="CREDENTIALS")
        password = conf.get_value("password", section="CREDENTIALS")
        self._request = AuthcRequest(username, password)

    def _decode(self, byte_string):
        return byte_string.decode('utf-8')

    def _encode(self, bytes):
        return bytes.encode("unicode_escape")

    def _get_boolean_input(self, message):
        sel = None
        while sel not in ["y", "Y", "n", "N"]:
            sel = input(f"{message}")
        return sel in ["y", "Y"]

    def print_response(self, response):
        print(f"Server responded: Status: [{response.status_code}] {self._decode(response.content)}")
