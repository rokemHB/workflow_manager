import json

from core.api.wrapper import Wrapper


class ProcedureWrapper(Wrapper):
    def __init__(self, conf):
        super(ProcedureWrapper, self).__init__(conf)

    def list_all(self):
        base_url = self._conf.get_value('base_url')
        response = self._request.get(f"{base_url}/api/{self.API_VERSION}/procedure")
        return response

    def evaluate(self):
        response = self.list_all()
        response_str = self._decode(response.content)
        response_json = json.loads(response_str)
        return response_json

