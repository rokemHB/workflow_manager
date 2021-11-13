#!/usr/bin/env python3
#-*- coding: utf-8 -*-

import argparse
import configparser
import os
from tempfile import mkstemp
from shutil import move

config = configparser.ConfigParser()


def load_config():
    path = '/'.join((os.path.abspath(__file__).replace('\\', '/')).split('/')[:-1])
    config.read(os.path.join(path, "i18n.cfg"))
    if "package" not in config["general"]:
        raise KeyError("'package' muss in i18n.cfg angegeben werden.")


def get_locales():
    if not config["general"]["locales"]:
        raise KeyError("Keine locales in der i18n.cfg gefunden.")
    return config["general"]["locales"].split(",")


def list_for_locale(locale):
    message_files_base_name = config["general"]["message_files"] or "messages"
    path = f"{create_folders()}{os.sep}{message_files_base_name}_{locale}.properties"
    if not os.path.isfile(path):
        print(f"Datei {path} nicht gefunden.")
    result = []
    with open(path, "r") as message_file:
        for line in message_file:
            result.append(f"{locale}:           {line}")
    return result


def find_messages(term):
    term = term.lower()
    result = []
    for locale in get_locales():
        messages_for_locale = list_for_locale(locale)
        for message in messages_for_locale:
            if term in message.lower():
                result.append(message)
    return result


def print_result(result):
    for res in result:
        print(res, end='')


def print_messages(locale):
    print(f"[{locale.upper()}]")
    for message in list_for_locale(locale):
        print(message, end='')


def add_label(label):
    path = create_folders()
    locales = get_locales()
    message_files_base_name = config["general"]["message_files"] or "messages"
    auto_prefix = True if config["general"]["auto_prefix"] == "True" or config["general"]["auto_prefix"] == "true" else False
    package = ""
    if auto_prefix:
        package = config["general"]["package"]
    for locale in locales:
        message = input(f"Text für Label: '{label}' und Locale: '{locale}': ")
        with open(f"{path}{os.sep}{message_files_base_name}_{locale}.properties", "a") as message_file:
            message_file.write(f"{package}.{label} = {message}\n" if auto_prefix else f"{label} = {message}\n")


def change_label(label):
    locales = get_locales()
    message_files_base_name = config["general"]["message_files"] or "messages"
    for locale in locales:
        path = f"{create_folders()}{os.sep}{message_files_base_name}_{locale}.properties"
        if not os.path.isfile(path):
            print(f"Datei {path} nicht gefunden.")
            continue
        replace_label_for_locale(path, label, locale)


def remove_label(label):
    locales = get_locales()
    message_files_base_name = config["general"]["message_files"] or "messages"
    for locale in locales:
        path = f"{create_folders()}{os.sep}{message_files_base_name}_{locale}.properties"
        if not os.path.isfile(path):
            print(f"Datei {path} nicht gefunden.")
            continue
        _remove_label(path, label)


def _remove_label(path, label):
    fh, abs_path = mkstemp()
    with open(fh, 'w') as new_file:
        with open(path) as old_file:
            for line in old_file:
                if line.startswith(label):
                    print(f"Label '{label}' entfernt.")
                    continue
                else:
                    new_file.write(line)
    os.remove(path)
    move(abs_path, path)


def replace_label_for_locale(path, label, locale):
    fh, abs_path = mkstemp()
    with open(fh, 'w') as new_file:
        with open(path) as old_file:
            for line in old_file:
                if line.startswith(label):
                    message = input(f"Text für Label: '{label}' und Locale: '{locale}': ")
                    new_file.write(f"{label} = {message}\n")
                else:
                    new_file.write(line)
    os.remove(path)
    move(abs_path, path)


def create_dir(path):
    if not os.path.exists(path):
        os.makedirs(path)
        print(f"Ordner {path} nicht vorhanden. Wird erstellt.")


def create_folders():
    path = '/'.join((os.path.abspath(__file__).replace('\\', '/')).split('/')[:-1])
    root_path = os.path.join(path, os.path.pardir)
    path_prefix = os.path.join(root_path, config["general"]["path"].replace("/", os.sep).replace("\\", os.sep) or "")
    package_paths = config["general"]["package"].strip().split(".")
    create_dir(path_prefix)
    create_dir(os.path.join(path_prefix, "src"))
    create_dir(os.path.join(path_prefix, "src", "main"))
    create_dir(os.path.join(path_prefix, "src", "main", "resources"))
    old_path = os.path.join(path_prefix, "src", "main", "resources")
    for path in package_paths:
        create_dir(os.path.join(old_path, path))
        old_path = os.path.join(old_path, path)
    create_dir(os.path.join(old_path, "i18n"))
    return os.path.join(old_path, "i18n")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--add", "-a", help="Füge ein Label hinzu.")
    parser.add_argument("--change", "-c", help="Ändert den Text eines Labels für alle konfigurierten Sprachen")
    parser.add_argument("--delete", "-d", help="Entfernt ein Label.")
    parser.add_argument("--list", "-l", help="Listet alle Label für LIST(Locale) auf.")
    parser.add_argument("--find", "-f", help="Sucht nach eingegebenen Werten in label und allen messages.")

    args = parser.parse_args()

    if args.add:
        add_label(args.add)
    elif args.change:
        change_label(args.change)
    elif args.delete:
        remove_label(args.delete)
    elif args.list:
        print_messages(args.list)
    elif args.find:
        print_result(find_messages(args.find))


if __name__ == "__main__":
    load_config()
    main()
