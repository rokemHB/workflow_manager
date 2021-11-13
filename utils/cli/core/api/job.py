import json

from core.api.priority import PriorityWrapper
from core.api.wrapper import Wrapper


def _select(l, message):
    selected_index = int(input(message))

    if selected_index < 0 or selected_index > len(l):
        sel = None
        while sel not in ["y", "Y", "n", "N"]:
            sel = input("Index out of bounds. Try again?(y/n)")
        if sel in ["y", "Y"]:
            return _select(l)
        else:
            exit(0)
    return selected_index


class JobWrapper(Wrapper):
    def __init__(self, conf):
        super(JobWrapper, self).__init__(conf)

    def list_all(self):
        base_url = self._conf.get_value('base_url')
        response = self._request.get(f"{base_url}/api/{self.API_VERSION}/job")
        return response

    def update_priority(self):
        base_url = self._conf.get_value('base_url')
        job_url = f"{base_url}/api/{self.API_VERSION}/job"
        results = json.loads(self.list_all().content)
        for result in results:
            self._print_job(result, results.index(result))
        selected_index = _select(results, "Please enter the index of the job: ")
        selected_job = results[selected_index]
        selected_priority = self._select_priority()
        if selected_priority == selected_job.get("priority"):
            print("ERROR: Can't change priority since Job {} already has priority {}".format(selected_job.get("name"),
                                                                                             selected_priority.get(
                                                                                                 "name")))
            return
        selected_job["priority"] = selected_priority
        response = self._request.put(job_url, json=selected_job)
        if not response.status_code == 200:
            print("ERROR: Couldn't update priority for job: {}".format(selected_job.get("name")))
        else:
            print("SUCCESS: Priority of job {} updated to {}".format(selected_job.get("name"),
                                                                     selected_priority.get("name")))

    def _select_priority(self):
        priority_api = PriorityWrapper(self._conf)
        priorities = json.loads(priority_api.list_all().content)
        for p in priorities:
            self._print_priority(p, priorities.index(p))
        selected_index = _select(priorities, "Please enter the index of the priority: ")
        selected_priority = priorities[selected_index]
        return selected_priority

    def _print_priority(self, priority, index):
        print("{}) Name: {} mit Priorität {}".format(index, priority.get("name"), priority.get("value")))

    def _print_job(self, job, index):
        priority = job.get("priority").get("value")
        print("{}) Job: {} mit Priorität {}".format(index, job.get("name"), priority))
