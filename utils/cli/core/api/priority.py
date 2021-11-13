from core.api.wrapper import Wrapper


class PriorityWrapper(Wrapper):
    def __init__(self, conf):
        super(PriorityWrapper, self).__init__(conf)

    def list_all(self):
        base_url = self._conf.get_value('base_url')
        response = self._request.get(f"{base_url}/api/{self.API_VERSION}/priority")
        return response



