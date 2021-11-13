#!/usr/bin/python3
from core.api.job import JobWrapper
from core.api.procedure import ProcedureWrapper
from core.api.users import UserWrapper
from core.api.processchains import ProcessChainWrapper
from core.eval import evaluate_procedures
from helpers.argparser import parse_args
from helpers.config import Config

available_classes = ["User", "ProcessChain"]


def main():
    args = parse_args()
    conf = Config(args.config_file)
    if args.generate_config:
        return
    if args.list:
        handle_list(conf)
    if args.eval:
        handle_eval(conf)
    if args.update:
        handle_update(conf)
    if args.add:
        handle_add(conf)


def handle_update(conf):
    classes = ["Job"]

    for cls in classes:
        print(f"{classes.index(cls)}) {cls}")

    selected_index = _select_class(classes)
    if selected_index == classes.index("Job"):
        job_api = JobWrapper(conf)
        response = job_api.update_priority()


def handle_add(conf):
    classes = ["User"]
    for cls in classes:
        print(f"{classes.index(cls)}) {cls}")

    selected_index = _select_class(classes)
    if selected_index == classes.index("User"):
        user_api = UserWrapper(conf)
        response = user_api.add()
        user_api.print_response(response)


def handle_list(conf):
    classes = ["User", "ProcessChain", "Procedure", "Job"]
    for cls in classes:
        print(f"{classes.index(cls)}) {cls}")

    selected_index = _select_class(classes)
    if selected_index == classes.index("User"):
        user_api = UserWrapper(conf)
        response = user_api.list_all()
        user_api.print_response(response)
    elif selected_index == classes.index("ProcessChain"):
        chain_api = ProcessChainWrapper(conf)
        response = chain_api.list_all()
        chain_api.print_response(response)
    elif selected_index == classes.index("Procedure"):
        procedure_api = ProcedureWrapper(conf)
        response = procedure_api.list_all()
        procedure_api.print_response(response)
    elif selected_index == classes.index("Job"):
        job_api = JobWrapper(conf)
        response = job_api.list_all()
        job_api.print_response(response)


def handle_eval(conf):
    classes = ["Procedure"]
    for cls in classes:
        print(f"{classes.index(cls)}) {cls}")

    selected_index = _select_class(classes)
    if selected_index == classes.index("Procedure"):
        procedure_api = ProcedureWrapper(conf)
        response_json = procedure_api.evaluate()
        result = evaluate_procedures(response_json)


def _select_class(classes):
    selected_index = int(input("Please enter the number of the class: "))

    if selected_index < 0 or selected_index > len(classes):
        sel = None
        while sel not in ["y", "Y", "n", "N"]:
            sel = input("Index out of bounds. Try again?(y/n)")
        if sel in ["y", "Y"]:
            return _select_class(classes)
        else:
            exit(0)
    return selected_index


if __name__ == "__main__":
    main()
