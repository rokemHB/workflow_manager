def _print_result(result):
    for r in result:
        if result[r] > 0:
            print("Prozesschitt {}, Durchschnittliche Bearbeitungszeit: {} ".format(r, _format_process_time(result[r])))


def _format_process_time(process_time_minutes):
    hours = process_time_minutes // 60
    minutes = process_time_minutes % 60
    return "{}h:{}m".format(hours, minutes)


def evaluate_procedures(procedures):
    result = {}
    step_count = {}
    for procedure in procedures:
        state_history = procedure.get("stateHistory")
        if state_history is not None and len(state_history.get("stateExecs")) > 0:
            step_name = procedure.get("processStep").get("name") if procedure.get("processStep") is not None else None
            result = evaluate_procedure(procedure, result=result)
            if step_name in step_count.keys():
                step_count[step_name] += 1
            else:
                step_count[step_name] = 1
    result = {r: result[r] // step_count[r] for r in result}
    _print_result(result)
3

def evaluate_procedure(procedure, result={}):
    step_name = procedure.get("processStep").get("name") if procedure.get("processStep") is not None else None
    for state_exec in procedure.get("stateHistory").get("stateExecs"):
        transition_time = state_exec.get("transitionTime")
        if step_name in result.keys():
            result[step_name] += transition_time if transition_time >= 0 else 0
        else:
            result[step_name] = transition_time if transition_time >= 0 else 0
    return result
